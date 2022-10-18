package com.chroneed.chroneedapp.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*

import com.chroneed.chroneedapp.ui.setting.RingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class WorkReceiver {

}

class UploadWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    val context = appContext
    override fun doWork(): Result {
        //val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getApplicationContext().getPackageName() + "/" + R.raw.ringtone)
//        var mp = MediaPlayer.create(context, soundUri)
        // Do the work here--in this case, upload the images.
//
        //mp.isLooping=true
        //mp.start()
        val intent = Intent(context, RingActivity::class.java)
        context.startActivity(intent)
        //finishAffinity()
        Toast.makeText(this.context, "Work Do", Toast.LENGTH_SHORT).show()
        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}

class ForegroundWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        setForeground(createForegroundInfo())
        return@withContext runCatching {
            runTaskAlarm(applicationContext)
            Result.success()
        }.getOrElse {
            Result.failure()
        }
    }

    private suspend fun runTaskAlarm(context: Context) {
        //val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getApplicationContext().getPackageName() + "/" + R.raw.ringtone)
        while (true){
            var mp= PlayMedia()
            mp.mPlayer?.start()
//            mp.seekTo(startTime)
//            updatePlayerPostion()
//            mp.start()
            delay(20000)
            mp.mPlayer?.stop()
            delay(10000)
        }
    }

    private fun createForegroundInfo(): ForegroundInfo {

        val id = "1225"
        val channelName = "Chroneed App"
        val title = "Schedule Time"
        val cancel = "Cancel"
        val body = "Alarm in application sets..."

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel(id, channelName)
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setContentText(title)
            .setTicker(title)
            .setContentText(body)
            .setSmallIcon(com.chroneed.chroneedapp.R.drawable.ic_calendar)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()
        return ForegroundInfo(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(id: String, channelName: String) {
        notificationManager.createNotificationChannel(
            NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        )
    }
}