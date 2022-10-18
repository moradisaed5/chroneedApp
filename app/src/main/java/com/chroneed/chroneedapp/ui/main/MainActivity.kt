package com.chroneed.chroneedapp.ui.main

import android.Manifest
//import android.R
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetAlarmNotifyListResponse
import com.chroneed.chroneedapp.data.medical.TrainResponseDto
import com.chroneed.chroneedapp.databinding.ActivityMainBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.setting.RecordActivity
import com.chroneed.chroneedapp.ui.setting.SettingsFragment
import com.chroneed.chroneedapp.ui.social.SocialNetworkActivity
import com.chroneed.chroneedapp.ui.user.ProfileFragment
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.ForegroundWorker
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        firstInitMain()
        setHomeFabClick()
        setNavigationItemSelect()
        getAlarms()
        //runAlarmTask()
    }

    private fun setUserType() {
        var usertype = MySharedPreferences.getUserType(this)
        val btnSocial: View = binding.mainMenuNavigationView.findViewById(R.id.bottom_menu_social)
        val btnHome: View = binding.mainMenuNavigationView.findViewById(R.id.buttommenu_home)
        val btnProfile: View = binding.mainMenuNavigationView.findViewById(R.id.buttommenu_perofile)
        val btnSetting: View = binding.mainMenuNavigationView.findViewById(R.id.buttommenu_settings)
        if((usertype=="0")){
            btnProfile.visibility=View.VISIBLE
            btnSocial.visibility=View.VISIBLE
            btnSetting.visibility=View.VISIBLE
            btnHome.visibility=View.VISIBLE
        }else if (usertype == "1") {
            btnProfile.visibility=View.VISIBLE
            btnSetting.visibility=View.VISIBLE
            btnHome.visibility=View.VISIBLE
            btnSocial.visibility = View.INVISIBLE
            btnSocial.isEnabled=false
        } else if (usertype == "2") {
            btnProfile.visibility=View.VISIBLE
            btnSetting.visibility=View.VISIBLE
            btnHome.visibility=View.VISIBLE
            btnSocial.visibility = View.INVISIBLE
            btnSocial.isEnabled=false
        }
    }

    private fun runAlarmTask() {
        WorkManager.getInstance(this)
            .beginUniqueWork(
                "ForegroundWorker", ExistingWorkPolicy.APPEND_OR_REPLACE,
                OneTimeWorkRequest.from(ForegroundWorker::class.java)
            ).enqueue().state
            .observe(this) { state ->
                Log.d(ContentValues.TAG, "ForegroundWorker: $state")
            }
    }

    private fun getAlarms() {
        val token = "Bearer " + MySharedPreferences.getUserToken(this)//token
        try {
            var apiInterface = ApiInterface.create(token).getAlarmNotifyList()
            apiInterface.enqueue(object : Callback<GetAlarmNotifyListResponse> {
                override fun onResponse(
                    call: Call<GetAlarmNotifyListResponse>,
                    response: Response<GetAlarmNotifyListResponse>
                ) {
                    if (response.body()!!.isSuccess == true) {
                        if (response.body()!!.data.count() > 0) {
                            // AlarmUtil().setAutoAlarms(response.body()!!.data,this@MainActivity)
                            //AlarmUtil().setAutoAlarm("my title","my desc",1, this@MainActivity)
                        }
                    }
                }

                override fun onFailure(call: Call<GetAlarmNotifyListResponse>, t: Throwable?) {
                }
            })
        } catch (ex: Exception) {
        }
    }

    private fun recognizeMessage(word: String) {
        val token = "Bearer " + MySharedPreferences.getUserToken(this)//token
        try {
            var apiInterface = ApiInterface.create(token).getUserCommandRecognition(word)
            apiInterface.enqueue(object : Callback<TrainResponseDto> {
                override fun onResponse(
                    call: Call<TrainResponseDto>,
                    response: Response<TrainResponseDto>
                ) {
                    if (response.body()!!.isSuccess == true) {
                        if (response.body()!!.data != null) {
                            var type = response.body()!!.data!!.type
                            var label = ""
                            when (type) {
                                0 -> label = "JustLabel"
                                1 -> {
                                    if (response.body()!!.data!!.callCommand != null) {
                                        response.body()!!.data!!.callCommand!!.phoneNumber?.let { startCall(it) }
                                    }
                                }
                                2 -> {
                                    if (response.body()!!.data!!.messageCommand != null) {
                                        val num = response.body()!!.data!!.messageCommand!!.phoneNumber!!
                                        val message = response.body()!!.data!!.messageCommand!!.textMessage!!
                                        startSMS(num, message)
                                    }
                                }
                                3 -> {
                                    label = "Medicine"
                                }
                                4 -> {
                                    label = "Habit"
                                }
                                5 -> {
                                    label = "Goal"
                                }
                                6 -> {
                                    label = "Nothing"
                                }
                                else -> {
                                    label = "Nothing"
                                }
                            }
                            //Toast.makeText(baseContext, label, Toast.LENGTH_SHORT).show()

                        }
                    }
                }

                override fun onFailure(call: Call<TrainResponseDto>, t: Throwable?) {
                }
            })
        } catch (ex: Exception) {
        }
    }


    private val REQUEST_CODE_SPEECH_INPUT = 10054

    private fun setHomeFabClick() {
        binding.mainMenuFabListening.setOnClickListener() {
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
            binding.lottieListening.visibility = View.VISIBLE
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                try {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
                } catch (e: Exception) {
                    // on below line we are displaying error message in toast
                }
            }, 300)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
//                Toast.makeText(this, Objects.requireNonNull(res)[0], Toast.LENGTH_LONG).show()
                val word = Objects.requireNonNull(res)[0]
                recognizeMessage(word)
                //startCall()
                //startSMS()

            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                binding.lottieListening.visibility = View.GONE
            }, 1000)
        }
        if (requestCode == CodeManager.REQUEST_SPEECH_GLOBAL_CODE) {
            val res: ArrayList<String> =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            getWord(Objects.requireNonNull(res)[0])
        }
    }

    val permissionRequest = 101
    private fun startSMS(phoneNumber: String, message: String) {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            myMessage(phoneNumber, message)
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.SEND_SMS),
                permissionRequest
            )
        }
    }

    private fun myMessage(phoneNumber: String, message: String) {
        if (phoneNumber == "" || message == "") {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            if (TextUtils.isDigitsOnly(phoneNumber)) {
                val smsManager: SmsManager = SmsManager.getDefault()
                smsManager.sendTextMessage("+91 " + phoneNumber, null, message, null, null)
                Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }


    private fun startCall(callNumber: String) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.CALL_PHONE),
            REQUEST_PHONE_CALL
        ) else {
            makeCall(callNumber)
        }
    }

    var REQUEST_PHONE_CALL = 3
    private fun makeCall(callNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse("tel:$callNumber"))
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            return
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PHONE_CALL) {
            makeCall("")
        }
    }

    private fun getWord(model: String) {
        Toast.makeText(this, model, Toast.LENGTH_LONG).show()
        binding.lottieListening.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.lottieListening.visibility = View.GONE
        }, 2000)
    }

    private fun setNavigationItemSelect() {
        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()

        binding.mainMenuNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.buttommenu_perofile -> makeCurrentNavView(profileFragment)
                R.id.bottom_menu_social -> {
                    var usertype = MySharedPreferences.getUserType(this)
                    if(usertype!="0")
                    {
                        Toast.makeText(this, "you can't access to social media ...", Toast.LENGTH_LONG).show()

                    }else{
                        val intent = Intent(this, SocialNetworkActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }

                }
                R.id.buttommenu_home -> makeCurrentNavView(homeFragment)
                R.id.buttommenu_settings -> makeCurrentNavView(settingsFragment)
                else -> makeCurrentNavView(homeFragment)
            }
            true
        }
    }

    private fun firstInitMain() {
        val homeFragment = HomeFragment()
        binding.mainMenuNavigationView.background = null
        makeCurrentNavView(homeFragment)

        binding.btnRecordActivity.setOnClickListener() {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun makeCurrentNavView(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.main_fragment_view_layout, fragment)
            commit()
        }
    }
}