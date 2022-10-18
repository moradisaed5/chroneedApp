package com.chroneed.chroneedapp.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.user.GlobalResponseDto
import com.chroneed.chroneedapp.data.user.SetUserAccessTypeForUserDto
import com.chroneed.chroneedapp.data.user.UserResponse
import com.chroneed.chroneedapp.databinding.FragmentProfileBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
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
        binding.btnSaveNurse.setOnClickListener {
            val password = binding.txtNursePassword.text.toString()
            savePassword(password, true)
        }
        binding.btnSavePeople.setOnClickListener {
            val password = binding.txtFamilyPassword.text.toString()
            savePassword(password, false)
        }
    }

    private fun savePassword(password: String, isNurse: Boolean) {
        var accessType = 1
        if (isNurse)
            accessType = 2
        val model= SetUserAccessTypeForUserDto(accessType,password)
        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).setUserAccessTypeForUser(model)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<GlobalResponseDto> {
            override fun onResponse(call: Call<GlobalResponseDto>, response: Response<GlobalResponseDto>) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        Toast.makeText(binding.root.context, "password changed!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show()
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GlobalResponseDto>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}