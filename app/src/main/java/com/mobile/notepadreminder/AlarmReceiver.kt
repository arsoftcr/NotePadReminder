package com.mobile.notepadreminder

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
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mobile.notepadreminder.data.TaskDatabase
import com.mobile.notepadreminder.viewmodels.TaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("alarm", "************************received ${intent?.action}")

        if (intent?.action == "alarma") {
            val number = intent?.getStringExtra("ALARMA_ID_STRING")
            val description = intent?.getStringExtra("ALARMA_DESCRIPTION_STRING")
            val mp = MediaPlayer.create(context, R.raw.alarma)
            mp.start()
            if (context != null) {
                if (number != null) {
                    val pendingResult: PendingResult = goAsync()
                    val asyncTask = Task(pendingResult, context, number.toInt())
                    asyncTask.execute()
                    showNotification(
                        context,
                        number.toInt(),
                        "Recordatorio: ${number.toInt()}",
                        "$description"
                    )
                }
            }
        }else{
            val mp = MediaPlayer.create(context, R.raw.alarma)
            mp.start()

            if (context != null) {
                val pendingResult: PendingResult = goAsync()
                val number = (1..99999).shuffled().first()
                val asyncTask = Task(pendingResult, context, number)
                asyncTask.execute()
                showNotification(context, number, "Recordatorio: $number", "boot alarm")

            }
        }
    }

    private class Task(
        private val pendingResult: PendingResult,
        private val context: Context,
        private val number: Int
    ) : AsyncTask<String, Int, String>() {

        override fun doInBackground(vararg params: String?): String {
            if (context != null) {
                TaskDatabase.getDatabase(context).taskDao().deleteAfterAlarm(number)
            }
            return toString().also { log ->
                Log.d("BACKGROUND", log)
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            // Must call finish() so the BroadcastReceiver can be recycled.
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
