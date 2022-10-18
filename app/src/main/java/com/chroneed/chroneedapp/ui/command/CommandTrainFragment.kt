package com.chroneed.chroneedapp.ui.command

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.EncoderProfiles
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetVoiceResponseDto
import com.chroneed.chroneedapp.data.medical.SendVoiceResponseDto
import com.chroneed.chroneedapp.databinding.FragmentCommandTrainBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.command.adapter.CommandRecordAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import com.github.squti.androidwaverecorder.WaveRecorder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CommandTrainFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            label = bundle.getString("label", "")
        }
    }

    private lateinit var label: String
    private var output: String? = null
    //private var mediaRecorder: MediaRecorder? = null
    private var waveRecorder : WaveRecorder? = null

    private var state: Boolean = false


    //private var shouldContinue = true
    private var _binding: FragmentCommandTrainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommandTrainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initElements()
        initEvents()
        loadRecords()
        initRecordVoice()
    }

    private fun initElements() {

    }

    private fun initEvents() {
        binding.btnUpload.setOnClickListener {
            uploadFile()
        }
    }

    private fun resetRecording() {
        loadRecords()
        initRecordVoice()
    }

    private fun createFile() {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val storageDir: File? = this.activity?.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
      var file=  File.createTempFile(
            "Voice_${timeStamp}_", /* prefix */
            ".wav", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            output = absolutePath
        }

    }

    private fun initRecordVoice() {
        binding.btnPlay.visibility = View.INVISIBLE
        binding.btnUpload.visibility = View.INVISIBLE
        getRecordPermission()
        binding.btnRecord.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    binding.root.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val permissions = arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                this.activity?.let { it1 -> ActivityCompat.requestPermissions(it1, permissions, 0) }
            } else {
                createFile()
                startRecording()
            }
        }
        binding.btnPlay.setOnClickListener() {
            var soundUri = Uri.parse(output)
            var mPlayer = MediaPlayer.create(binding.root.context, soundUri)
            mPlayer.start()
            binding.btnPlay.visibility = View.GONE
            binding.btnRecord.visibility = View.GONE
            binding.btnUpload.visibility = View.GONE
            binding.lottiePlay.visibility = View.VISIBLE
            binding.lottiePlay.playAnimation()
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.btnPlay.visibility = View.VISIBLE
                binding.btnRecord.visibility = View.VISIBLE
                binding.btnUpload.visibility = View.VISIBLE
                binding.lottiePlay.visibility = View.GONE
            }, 1200)
        }
    }

    @SuppressLint("WrongConstant")
    private fun startRecording() {
        try {
            if (!state) {

                waveRecorder= WaveRecorder(output.toString())
                waveRecorder!!.waveConfig.sampleRate = 16000
                waveRecorder!!.waveConfig.channels = AudioFormat.CHANNEL_IN_MONO
                waveRecorder!!.waveConfig.audioEncoding = AudioFormat.ENCODING_PCM_16BIT
                waveRecorder!!.startRecording()


//                mediaRecorder = MediaRecorder()
//                mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
//                mediaRecorder?.setOutputFormat(AudioFormat.ENCODING_PCM_16BIT)
//                mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                mediaRecorder?.setOutputFile(output)
//                mediaRecorder?.setAudioSamplingRate(16000)
//                mediaRecorder?.prepare()
//                mediaRecorder?.start()

                binding.btnRecord.playAnimation()
                binding.btnPlay.visibility = View.GONE
                binding.btnUpload.visibility = View.GONE
                binding.lottiePlay.visibility = View.GONE
                state = true
                //
                Toast.makeText(binding.root.context, "record started!", Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
//                    mediaRecorder?.stop()
//                    mediaRecorder?.release()

                    waveRecorder!!.stopRecording()
                    state = false
                    binding.btnRecord.visibility = View.VISIBLE
                    binding.btnPlay.visibility = View.VISIBLE
                    binding.btnUpload.visibility = View.VISIBLE
                    binding.btnRecord.cancelAnimation()
                }, 1200)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    private fun getRecordPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                binding.root.context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions =
                arrayOf(android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            this.activity?.let { ActivityCompat.requestPermissions(it, permissions, 0) }
        } else
            return true
        return false
    }

    private fun uploadFile() {
        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var voiceFile: MultipartBody.Part? = null
        if (output != null) {
            val file = File(output)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            voiceFile = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var apiInterface = ApiInterface.create(token).sendVoices(label, voiceFile)
            //----------------------------------
            binding.lottieUpload.visibility = View.VISIBLE
            binding.btnPlay.visibility = View.GONE
            binding.btnRecord.visibility = View.GONE
            binding.btnUpload.visibility = View.GONE
            binding.lottieUpload.playAnimation()
            //----------------------------------

            apiInterface.enqueue(object : Callback<SendVoiceResponseDto> {
                override fun onResponse(call: Call<SendVoiceResponseDto>, response: Response<SendVoiceResponseDto>) {
                    if (response.code() == 200) {
                        binding.lottieUpload.cancelAnimation()
                        binding.lottieUpload.visibility = View.GONE
                        binding.btnPlay.visibility = View.VISIBLE
                        binding.btnRecord.visibility = View.VISIBLE
                        binding.btnUpload.visibility = View.VISIBLE
                        Toast.makeText(context, "voice sent!", Toast.LENGTH_LONG).show();
                        resetRecording()
                    } else {
                        binding.lottieUpload.cancelAnimation()
                        binding.lottieUpload.visibility = View.GONE
                        binding.btnPlay.visibility = View.VISIBLE
                        binding.btnRecord.visibility = View.VISIBLE
                        binding.btnUpload.visibility = View.VISIBLE
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG).show();
                    }

                }

                override fun onFailure(call: Call<SendVoiceResponseDto>, t: Throwable?) {
                    binding.lottieUpload.cancelAnimation()
                    binding.lottieUpload.visibility = View.GONE
                    binding.btnPlay.visibility = View.VISIBLE
                    binding.btnRecord.visibility = View.VISIBLE
                    binding.btnUpload.visibility = View.VISIBLE

                    Toast.makeText(context, "can't connect to server!", Toast.LENGTH_LONG).show();
                    return
                }
            })

        }
    }
    private fun loadRecords() {
        var models: MutableList<String> = mutableListOf()
        lateinit var modelsAdapter: CommandRecordAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getVoices(label)
        apiInterface.enqueue(object : Callback<GetVoiceResponseDto> {
            override fun onResponse(
                call: Call<GetVoiceResponseDto>,
                response: Response<GetVoiceResponseDto>
            ) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        models.clear()//Todo: need to load in first time
                        response.body()!!.data?.let { models.addAll(it) }
                        modelsAdapter = activity?.let { CommandRecordAdapter(models, it) }!!
                        binding.voiceListRecycler.layoutManager = LinearLayoutManager(activity)
                        binding.voiceListRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetVoiceResponseDto>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}