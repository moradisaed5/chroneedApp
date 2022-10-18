package com.chroneed.chroneedapp.ui.setting

import android.content.ContentResolver
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.databinding.ActivityRingBinding

class RingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initEvents()
    }

    private fun initEvents() {
        binding.btnSnoze.setOnClickListener() {
            val soundUri =
                Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + binding.root.context.getApplicationContext().getPackageName() + "/" + R.raw.ringtone)
            val mp = MediaPlayer.create(binding.root.context, soundUri)
            if (mp.isPlaying)
                Toast.makeText(binding.root.context, "is playing ...", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(binding.root.context, "is not playing ...", Toast.LENGTH_SHORT).show()
        }
    }
}