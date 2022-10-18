package com.chroneed.chroneedapp.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.PrescriptionMedicineModel
import com.chroneed.chroneedapp.data.medical.TrainResponseDto
import com.chroneed.chroneedapp.databinding.FragmentSettingsBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.main.LoginActivity
import com.chroneed.chroneedapp.ui.main.MainActivity
import com.chroneed.chroneedapp.ui.prescription.PrescriptionListFragment
import com.chroneed.chroneedapp.ui.user.ChangePasswordFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import com.chroneed.chroneedapp.utilities.SaveByteFile
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.system.exitProcess


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
    }

    private fun initEvents() {
        binding.lstSettingMenu.setOnItemClickListener { parent, view, position, id ->
            when (binding.lstSettingMenu.adapter.getItem(position).toString()) {
                "User Profile" -> {
                    showUserProfile()
                }
                "Sync Commands"->{
                    startTrainCommand()
                }
                "Change Password" -> {
                    changePassword()
                }
                "Exit" -> {
                    exitApp()
                }
                "Sign Out" -> {
                    signOut()
                }
                else -> {
                 //   signOut()
                }
            }
        }
    }

    private fun showUserProfile() {}

    private fun changePassword() {
        val fragment = ChangePasswordFragment()
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.main_fragment_view_layout, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun exitApp() {
        exitProcess(-1)
        this.activity?.let { finishAffinity(it) }
    }

    private fun signOut() {
        MySharedPreferences.saveUserToken("", binding.root.context)
        val intentPage = Intent(binding.root.context, LoginActivity::class.java)
        startActivity(intentPage)
        this.activity?.let { finishAffinity(it) }
    }

    private fun startTrainCommand(){
        var loadingProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        loadingProgress.showDialog()
        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var apiInterface = ApiInterface.create(token).trainModels()
        apiInterface.enqueue(object : Callback<TrainResponseDto> {
            override fun onResponse(call: Call<TrainResponseDto>, response: Response<TrainResponseDto>) {
                loadingProgress.closeDialog()
                if (response.code() == 200) {
                    response.body()?.let {
                        SaveByteFile().getTrainFile(binding.root.context)
                    }
                } else
                    Toast.makeText(context, "problem to train models!", Toast.LENGTH_LONG).show()
            }
            override fun onFailure(call: Call<TrainResponseDto>, t: Throwable?) {
                loadingProgress.closeDialog()
                Toast.makeText(context, "can't connect to server!", Toast.LENGTH_LONG).show()
                return
            }
        })
    }

}