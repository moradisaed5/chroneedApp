package com.chroneed.chroneedapp.ui.command

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chroneed.chroneedapp.data.medical.TrainResponseDto
import com.chroneed.chroneedapp.databinding.ActivityTrainCommandByWordBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class TrainCommandByWordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTrainCommandByWordBinding
    private var id: String = ""
    private val REQUEST_SPEECH_TRAIN_CODE = 10055
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainCommandByWordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        id = intent.getStringExtra("id").toString()
        //Toast.makeText(this, ss, Toast.LENGTH_SHORT).show()
        if (id.isNullOrEmpty())
            this.finish()
        recognizeModel()
    }

    private fun recognizeModel() {
        binding.lottieListening.visibility = View.GONE
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to Train")
        startActivityForResult(intent, REQUEST_SPEECH_TRAIN_CODE)
    }

    private fun startTrain(model: String) {
        binding.lottieListening.visibility = View.VISIBLE
        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)//token
        var apiInterface = ApiInterface.create(token).updateUserCommandTrainLabel(id, model)
        apiInterface.enqueue(object : Callback<TrainResponseDto> {
            override fun onResponse(call: Call<TrainResponseDto>, response: Response<TrainResponseDto>) {
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    try {
                        closActivity()
                    } catch (e: Exception) {
                    }
                }, 300)
            }
            override fun onFailure(call: Call<TrainResponseDto>, t: Throwable?) {
                Toast.makeText(binding.root.context, t?.message, Toast.LENGTH_LONG).show();
                closActivity()
            }
        })
    }

    private fun closActivity() {
        this.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SPEECH_TRAIN_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                startTrain(Objects.requireNonNull(res)[0])
            }else
                closActivity()
        } else
            closActivity()
    }
}