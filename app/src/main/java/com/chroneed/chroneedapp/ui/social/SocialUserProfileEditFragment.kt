package com.chroneed.chroneedapp.ui.social

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.*
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentSocialUserProfileEditBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class SocialUserProfileEditFragment : Fragment() {
    private var _binding: FragmentSocialUserProfileEditBinding? = null
    private val binding get() = _binding!!
    private var mCurrentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialUserProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.profileImage.setOnClickListener() {
            selectImageFromGallery()
        }
        binding.btnBack.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnSaveProfile.setOnClickListener() {
            uploadImageAndSaveRecord(mCurrentPhotoPath)
        }
        requestUserProfile()

    }

    private fun saveProfile(imageName: String) {
        val baseModel = NewSocialProfileModel(
            binding.switchPrivate.isChecked,
            binding.txtBio.text.toString(),
            imageName,
            binding.txtFirstName.text.toString(),
            binding.txtLastName.text.toString())

        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).setSocialUserProfile(baseModel)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<SocialProfileResponse> {
            override fun onResponse(call: Call<SocialProfileResponse>,response: Response<SocialProfileResponse>) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        val fragment = SocialUserProfileFragment()
                        activity
                            ?.supportFragmentManager
                            ?.beginTransaction()
                            ?.replace(R.id.social_layout_fragment, fragment)
                            ?.addToBackStack(null)
                            ?.commit()
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

            override fun onFailure(call: Call<SocialProfileResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun uploadImageAndSaveRecord(imageName: String?) {
        var uploadedFileName = ""
        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var loadingProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        //loadingProgress.showDialog()
        var profilePic: MultipartBody.Part? = null
        if (imageName != null) {
            uploadedFileName = ""
            val file = File(imageName)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            profilePic = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var apiInterface = ApiInterface.create("").uploadFile(profilePic)
            apiInterface.enqueue(object : Callback<UploadResponseDto> {
                override fun onResponse(call: Call<UploadResponseDto>,response: Response<UploadResponseDto>) {
                    if (response.code() == 200) {
                        if (response.body()!!.isSuccess) {
                            if (response.body()!!.data != null) {
                                if (response.body()!!.data?.count() == 1) {
                                    try {
                                        loadingProgress.closeDialog()
                                        uploadedFileName = response.body()!!.data?.first()?.fileName.toString()
                                        saveProfile(uploadedFileName)
                                    } catch (ex: Exception) {
                                    }
                                }
                            }
                        } else
                            Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                                .show();
                    } else
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                            .show();

                    if (uploadedFileName!!.isNotEmpty()) {
                    } else {
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                            .show()
                    }
                    loadingProgress.closeDialog()
                }

                override fun onFailure(call: Call<UploadResponseDto>, t: Throwable?) {
                    loadingProgress.closeDialog()
                    Toast.makeText(context, t!!.message, Toast.LENGTH_LONG).show();
                    return
                }
            })

        } else
            saveProfile("")
    }

    private fun initProfile(inputModel: SocialProfileModel) {
        val image = binding.profileImage
        val imageName = inputModel.imageProfileName
        if (imageName != null) {
            if (imageName!!.isNotEmpty()) {
                Glide.with(binding.root.context).load("http://chroneed.saeid-moradi.ir/files/$imageName")
                    .placeholder(R.drawable.ic_user)
                    .into(image)
            }
        }
        if (inputModel != null) {
            binding.txtBio.setText(inputModel.bio)
            binding.txtFirstName.setText(inputModel.userFirstName)
            binding.txtLastName.setText(inputModel.userLastName)
            binding.switchPrivate.isChecked = inputModel.isPrivate!!
        }
    }

    private fun requestUserProfile() {
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getUserProfile()
        apiInterface.enqueue(object : Callback<SocialProfileResponse> {
            override fun onResponse(call: Call<SocialProfileResponse>, response: Response<SocialProfileResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data != null) {
                        response.body()!!.data?.let { initProfile(it) }
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialProfileResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(binding.root.context) // optional usage

            val imageFile = File(uriFilePath)
            if (imageFile.isFile) {
                mCurrentPhotoPath = uriFilePath
                binding.profileImage.setImageURI(null)
                binding.profileImage.refreshDrawableState()
                binding.profileImage.setImageURI(Uri.fromFile(imageFile))
                binding.profileImage.refreshDrawableState()
            }
        }
    }

    private fun selectImageFromGallery() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setActivityTitle("habit image")
            }
        )
    }

}