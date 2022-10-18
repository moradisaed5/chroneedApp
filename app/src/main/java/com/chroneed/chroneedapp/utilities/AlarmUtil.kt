package com.chroneed.chroneedapp.utilities

import android.app.*
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.AlarmNotify
import com.chroneed.chroneedapp.ui.setting.RingActivity
import java.util.*
import kotlin.math.hypot

class AlarmUtil {
    fun cancelAllAlarm(activity: Activity) {
        val calendar = Calendar.getInstance()
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java)
        intent.putExtra("alarm_message", "this message send from prescription")
        intent.putExtra("alarm_title", "this is my title")
        intent.putExtra("alarm_service_name", "this is service name")
        val pendingIntent = PendingIntent.getBroadcast(
            activity, CodeManager.REQUEST_ALARM_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }

    fun setAutoAlarm(title: String,description:String,sec: Int, activity: Activity) {
        val calendar = Calendar.getInstance()
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(activity, AlarmReceiver::class.java).apply {
            putExtra("alarm_message", description)
            putExtra("alarm_title", title)
            putExtra("alarm_service_name", title)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            activity, CodeManager.REQUEST_ALARM_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + (sec * 1000),
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
    public fun setAutoAlarms(alarms: List<AlarmNotify>,activity: Activity){
//        cancelAllAlarm(activity)
//        alarms.forEach{
//            setAutoAlarm(it.title,it.description, it.totalSecond,activity)
//        }
        setAutoAlarm("my title","my desc",1,activity)
    }
}


class AlarmReceiver : BroadcastReceiver() {
    companion object {
        const val NOTIFICATION_ID = 110
        const val NOTIFICATION_CHANNEL_ID = "1011"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val alarmMessage = intent.getStringExtra("alarm_message").toString()
        val alarmTitle = intent.getStringExtra("alarm_title").toString()
        val alarmServiceName = intent.getStringExtra("alarm_service_name").toString()
        createNotificationChannel(context, alarmServiceName)
        notifyNotification(context, alarmTitle, alarmMessage)
    }

    private fun createNotificationChannel(context: Context, serviceName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                serviceName,
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(notificationChannel)
        }
    }

    private fun notifyNotification(context: Context, title: String, message: String) {

        val activityActionIntent = Intent(context.applicationContext, RingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val activityActionPendingIntent: PendingIntent =
            PendingIntent.getActivity(context.applicationContext, 0, activityActionIntent, 0)

        // adding action for broadcast
        val broadcastIntent = Intent(context.applicationContext, MyBroadcastReceiver::class.java).apply {
            putExtra("action_msg", "some message for toast")
        }
        val broadcastPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(context.applicationContext, 0, broadcastIntent, 0)
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.ringtone)
        with(NotificationManagerCompat.from(context)) {
            var mp = MediaPlayer()
            mp = MediaPlayer.create(context, soundUri)
            mp.isLooping=false
            mp.start()
            val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel( true )
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("long notification content")
                )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //for adding action
                .addAction(R.drawable.ic_accept, "Activity Action", activityActionPendingIntent)
                .addAction(R.drawable.ic_camera, "Broadcast Action", broadcastPendingIntent)

            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            mNotificationManager!!.notify(
                System.currentTimeMillis().toInt(),
                mBuilder.build()
            )
        }
    }
}
class MyBroadcastReceiver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, intent?.getStringExtra("action_msg"), Toast.LENGTH_SHORT).show()
    }
}