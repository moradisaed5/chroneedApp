package com.chroneed.chroneedapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.user.LoginDto
import com.chroneed.chroneedapp.data.user.LoginResponse
import com.chroneed.chroneedapp.data.user.RegisterDto
import com.chroneed.chroneedapp.data.user.RegisterResponse
import com.chroneed.chroneedapp.databinding.ActivityRegisterBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initEvents()
        initElements()
    }

    private fun initElements() {
    }

    private fun initEvents() {
        binding.btnRegister.setOnClickListener(){
            if(binding.txtFirstName.text.isNullOrEmpty())
            {
                Toast.makeText(this, "please enter your first name ...", Toast.LENGTH_LONG).show()
                binding.txtFirstName.requestFocus()
                return@setOnClickListener
            }
            if(binding.txtLastName.text.isNullOrEmpty())
            {
                Toast.makeText(this, "please enter your last name ...", Toast.LENGTH_LONG).show()
                binding.txtLastName.requestFocus()
                return@setOnClickListener
            }
            if(binding.txtPhoneNumber.text.isNullOrEmpty())
            {
                Toast.makeText(this, "please enter your phone number ...", Toast.LENGTH_LONG).show()
                binding.txtPhoneNumber.requestFocus()
                return@setOnClickListener
            }
            if(binding.txtPassword.text.isNullOrEmpty())
            {
                Toast.makeText(this, "please enter your password ...", Toast.LENGTH_LONG).show()
                binding.txtPassword.requestFocus()
                return@setOnClickListener
            }
            registerUser()
        }
        binding.txtLogin.setOnClickListener(){
            val intentPage = Intent(this, LoginActivity::class.java)
            startActivity(intentPage)
            finishAffinity()
        }
    }
    private fun  registerUser(){
        var model= RegisterDto(
            binding.txtFirstName.text.toString(),
            binding.txtLastName.text.toString(),
            binding.txtPassword.text.toString(),
            binding.txtPhoneNumber.text.toString())
        var myProgress = MyProgressDialog(this, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()

        var apiInterface = ApiInterface.create("").register(model)
        apiInterface.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {

                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        MySharedPreferences.saveUserId(response.body()!!.data?.id, this@RegisterActivity)
                        MySharedPreferences.saveUserFirstName(response.body()!!.data?.firstName, this@RegisterActivity)
                        MySharedPreferences.saveUserLastName(response.body()!!.data?.lastName, this@RegisterActivity)
                        MySharedPreferences.saveUserToken(response.body()!!.data?.jwtToken, this@RegisterActivity)
                        MySharedPreferences.saveUserPic(response.body()!!.data?.profileImageName, this@RegisterActivity)
                        MySharedPreferences.saveUserPhone(response.body()!!.data?.phoneNumber, this@RegisterActivity)
                        MySharedPreferences.setIsLogin(true, this@RegisterActivity)
                        val intentPage = Intent(this@RegisterActivity, MainActivity::class.java)
                        startActivity(intentPage)
                        finishAffinity()
                    } else
                        Toast.makeText(this@RegisterActivity, response.body()!!.message, Toast.LENGTH_LONG).show();
                } else Toast.makeText(this@RegisterActivity, "کاربری با اطلاعات وارد شده پیدا نشد",Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable?) {
                myProgress.closeDialog()
                Toast.makeText(this@RegisterActivity, "عدم ارتباط با سرور", Toast.LENGTH_LONG).show();
            }
        })
    }

}