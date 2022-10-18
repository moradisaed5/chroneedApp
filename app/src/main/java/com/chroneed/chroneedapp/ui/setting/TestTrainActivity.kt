package com.chroneed.chroneedapp.ui.setting

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chroneed.chroneedapp.databinding.ActivityTestTrainBinding
import java.util.*


class TestTrainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTestTrainBinding
    private var logList: MutableList<String> = mutableListOf()
    // on below line we are creating a constant value
    private val REQUEST_CODE_SPEECH_INPUT = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestTrainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setLog("onCreate ...")
        binding.btnRecord.setOnClickListener {
            startRecord()
        }
        binding.btnPlay.setOnClickListener {

        }
    }

    private fun startRecord() {
// on below line we are calling speech recognizer intent.
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // on below line we are passing language model
        // and model free form in our intent
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        // on below line we are passing our
        // language as a default language.
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        // on below line we are specifying a prompt
        // message as speak to text on below line.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        // on below line we are specifying a try catch block.
        // in this block we are calling a start activity
        // for result method and passing our result code.
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            // on below line we are displaying error message in toast
            setLog(e.message.toString())
        }
    }
    // on below line we are calling on activity result method.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                setLog(Objects.requireNonNull(res)[0])
            }
        }
    }
    private fun clearLogList() {
        logList = mutableListOf()
        updateLogList()
    }
    private fun setLog(text: String) {
        logList.add(text)
        updateLogList()
    }
    private fun updateLogList() {
        binding.listLog.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, logList)
    }
}