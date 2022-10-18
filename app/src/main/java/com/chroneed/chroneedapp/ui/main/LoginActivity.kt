package com.chroneed.chroneedapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.user.LoginDto
import com.chroneed.chroneedapp.data.user.LoginResponse
import com.chroneed.chroneedapp.databinding.ActivityLoginBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initEvents()
        initElements()
    }

    private fun initElements() {
        val token = MySharedPreferences.getUserToken(this)
        if (token.isNotEmpty()) {
            val intentPage = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intentPage)
            finishAffinity()
        }
    }

    private fun initEvents() {
        binding.loginBtnLogin.setOnClickListener() {
            loginClick()
        }
        binding.loginTxtRegister.setOnClickListener() {
            val intentPage = Intent(this, RegisterActivity::class.java)
            startActivity(intentPage)
            finishAffinity()
        }
    }

    private fun loginClick() {
        val email = binding.loginEmailText.text.toString()
        val password = binding.loginPasswordText.text.toString()
        if (email.isEmpty()) {//if email is empty => error
            binding.loginEmailText.error = "email is empty !"
            return
        } else {
            binding.loginEmailText.error = ""
        }
        if (password.isEmpty()) {//if email is empty => error
            binding.loginPasswordText.error = "password is empty !"
            return
        } else {
            binding.loginPasswordText.error = ""
        }
        if (email.isNotEmpty() && password.isNotEmpty()) {
            requestLogin()
        }
    }

    private fun requestLogin() {
        val email = binding.loginEmailText.text.toString()
        val password = binding.loginPasswordText.text.toString()
            var myProgress = MyProgressDialog(this, R.raw.lottie_bluetooth_turnedoff)
            myProgress.showDialog()

        var apiInterface = ApiInterface.create("").login(LoginDto(email, password.trim()))
        apiInterface.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        MySharedPreferences.saveUserId(response.body()!!.data?.id, this@LoginActivity)
                        MySharedPreferences.saveUserFirstName(response.body()!!.data?.firstName, this@LoginActivity)
                        MySharedPreferences.saveUserLastName(response.body()!!.data?.lastName, this@LoginActivity)
                        MySharedPreferences.saveUserToken(response.body()!!.data?.jwtToken, this@LoginActivity)
                        MySharedPreferences.saveUserPic(response.body()!!.data?.profileImageName, this@LoginActivity)
                        MySharedPreferences.saveUserType(response.body()!!.data?.userType.toString(), this@LoginActivity)
                        MySharedPreferences.saveUserPhone(response.body()!!.data?.phoneNumber, this@LoginActivity)
                        MySharedPreferences.setIsLogin(true, this@LoginActivity)
                        val intentPage = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intentPage)
                        finishAffinity()
                    } else
                        Toast.makeText(this@LoginActivity, response.body()!!.message, Toast.LENGTH_LONG).show();
                } else Toast.makeText(this@LoginActivity, "کاربری با اطلاعات وارد شده پیدا نشد", Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable?) {
                myProgress.closeDialog()
                Toast.makeText(this@LoginActivity, "عدم ارتباط با سرور", Toast.LENGTH_LONG).show();
            }
        })
    }
}