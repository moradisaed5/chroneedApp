package com.chroneed.chroneedapp.utilities


import android.app.Activity
import android.content.ContentResolver
import android.media.MediaPlayer
import android.media.MediaPlayer.OnSeekCompleteListener
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.chroneed.chroneedapp.R


class PlayMedia : Activity(), OnSeekCompleteListener {
    var mHandler: Handler? = null
    var mPlayer: MediaPlayer? = null
    var mStartTime = 6889
    var mEndTime = 7254
    val mStopAction = Runnable { mPlayer!!.stop() }
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + applicationContext.getApplicationContext().getPackageName() + "/" + R.raw.ringtone)
        val tv = TextView(this)
        tv.text = "Playing..."
        setContentView(tv)
        mHandler = Handler()
        mPlayer = MediaPlayer.create(this, soundUri)
        mPlayer!!.setOnSeekCompleteListener(this)
        mPlayer!!.seekTo(mStartTime)
    }

    public override fun onDestroy() {
        super.onDestroy()
        mPlayer!!.release()
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        mPlayer!!.start()
        mHandler?.postDelayed(mStopAction, (mEndTime - mStartTime).toLong())
    }
}