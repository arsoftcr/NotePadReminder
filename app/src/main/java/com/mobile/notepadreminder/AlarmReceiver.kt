package com.mobile.notepadreminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("alarm","************************received")

        if (intent?.action == "alarma") {
            val number = intent?.getStringExtra("ALARMA_ID_STRING")
            val description=intent?.getStringExtra("ALARMA_DESCRIPTION_STRING")
            val mp= MediaPlayer.create(context,R.raw.alarma)
            mp.start()

            if (context != null) {
                if (number != null) {
                    showNotification(context,number.toInt(),"Recordatorio: ${number.toInt()}","$description")
                }
            }
        }
    }

    private  fun showNotification(context: Context,idN:Int,title:String,text:String){
        try{
            val manager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val id= UUID.randomUUID().toString()
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
                val channel= NotificationChannel(id,
                    UUID.randomUUID().toString(), NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(channel)
            }
            var builder= NotificationCompat.Builder(context,id)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            manager.notify(idN,builder.build())

        }catch (exc:Exception){
            Toast.makeText(context,exc.toString(),Toast.LENGTH_LONG).show()
        }
    }
}
