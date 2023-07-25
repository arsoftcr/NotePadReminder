package com.mobile.notepadreminder

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.notepadreminder.data.Task
import com.mobile.notepadreminder.data.TaskDatabase
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AlarmReceiver : BroadcastReceiver() {


    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "alarma") {
            val number = intent?.getStringExtra("ALARMA_ID_STRING")
            val description = intent?.getStringExtra("ALARMA_DESCRIPTION_STRING")
            val mp = MediaPlayer.create(context, R.raw.alarma)
            mp.start()
            if (context != null) {
                if (number != null) {
                    //val pendingResult: PendingResult = goAsync()
                    //val asyncTask = Task(pendingResult, context, number.toInt())
                    //asyncTask.execute()
//                    showNotification(
//                        context,
//                        number.toInt(),
//                        "Recordatorio: ${number.toInt()}",
//                        "$description"
//                    )
                    GlobalScope.launch (Dispatchers.Main){
                        withContext(Dispatchers.IO){
                            val taskReceived=TaskDatabase.getDatabase(context).taskDao().selectOne(number.toInt())
                            TaskDatabase.getDatabase(context).taskDao().updateTask(taskReceived.id,1)

                            showNotification(
                                context,
                                number.toInt(),
                                "Recordatorio: ${taskReceived.title}",
                                "$description"
                            )
                        }
                    }

                }
            }
        }else{

            if (context != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    withContext(Dispatchers.IO){
                        val tasks=TaskDatabase.getDatabase(context).taskDao().select()
                        if (tasks.isNotEmpty()){
                            for (alarm in tasks) {
                                if (alarm.iscompleted==0){
                                    val intent= Intent(context,AlarmReceiver::class.java)
                                    intent.action="alarma"
                                    intent.putExtra("ALARMA_ID_STRING","${alarm.shuff}")
                                    intent.putExtra("ALARMA_DESCRIPTION_STRING", alarm.description)
                                    val pending=
                                        PendingIntent.getBroadcast(context,alarm.shuff,intent, PendingIntent.FLAG_IMMUTABLE)

                                    val alarmMgr= context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                                    alarmMgr?.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,alarm.time,pending)
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private class Task(
        private val pendingResult: PendingResult,
        private val context: Context,
        private val number: Int
    ) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            TaskDatabase.getDatabase(context).taskDao().deleteAfterAlarm(number)
            return toString().also { log ->
                Log.d("BACKGROUND", log)
            }
        }
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            pendingResult.finish()
        }
    }

    private fun showNotification(context: Context, idN: Int, title: String, text: String) {
        try {
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id = UUID.randomUUID().toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    id,
                    UUID.randomUUID().toString(), NotificationManager.IMPORTANCE_HIGH
                )
                manager.createNotificationChannel(channel)
            }

            val fullScreenIntent = Intent(context, MainActivity::class.java)
            val fullScreenPendingIntent = PendingIntent.getActivity(
                context, 0,
                fullScreenIntent, PendingIntent.FLAG_IMMUTABLE
            )

            var builder = NotificationCompat.Builder(context, id)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setFullScreenIntent(fullScreenPendingIntent, true)

            manager.notify(idN, builder.build())

        } catch (exc: Exception) {
            Toast.makeText(context, exc.toString(), Toast.LENGTH_LONG).show()
        }
    }
}
