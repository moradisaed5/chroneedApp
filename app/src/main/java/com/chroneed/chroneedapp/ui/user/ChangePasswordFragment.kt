package com.chroneed.chroneedapp.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalHabitResponse
import com.chroneed.chroneedapp.data.user.ChangePasswordDto
import com.chroneed.chroneedapp.data.user.UserResponse
import com.chroneed.chroneedapp.databinding.FragmentChangePasswordBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordFragment : Fragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        binding.btnSave.setOnClickListener() {
            saveChangePassword()
        }
        binding.btnBack.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun saveChangePassword() {

        if(binding.txtOldPassword.text.isNullOrEmpty()){
            Toast.makeText(binding.root.context, "please enter your old password!", Toast.LENGTH_SHORT).show()
            binding.txtOldPassword.requestFocus()
            return
        }
        if(binding.txtNewPassword.text.isNullOrEmpty()){
            Toast.makeText(binding.root.context, "please enter your new password!", Toast.LENGTH_SHORT).show()
            binding.txtNewPassword.requestFocus()
            return
        }
        var modelDto=ChangePasswordDto(binding.txtOldPassword.text.toString(),binding.txtNewPassword.text.toString())
        requestChangePassword(modelDto)

    }
    private fun requestChangePassword(model: ChangePasswordDto){
        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).changeUserPassword(model)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>,response: Response<UserResponse>) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        MySharedPreferences.saveUserToken(response.body()!!.data?.jwtToken, binding.root.context)
                        activity?.supportFragmentManager?.popBackStack()
                        Toast.makeText(binding.root.context, "password changed!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}