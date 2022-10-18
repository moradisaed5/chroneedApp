package com.chroneed.chroneedapp.ui.social

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.NewSocialPostModel
import com.chroneed.chroneedapp.data.social.SocialPostResponse
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentSocialNewPostBinding
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


class SocialNewPostFragment : Fragment() {

    private var _binding: FragmentSocialNewPostBinding? = null
    private val binding get() = _binding!!
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialNewPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnBack.setOnClickListener() {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnSave.setOnClickListener() {
            if (binding.txtTitle.text.isNullOrEmpty()) {
                binding.txtTitle.requestFocus()
                Toast.makeText(binding.root.context, "title can not be empty", Toast.LENGTH_LONG).show()
            } else if (binding.txtDescription.text.isNullOrEmpty()) {
                binding.txtDescription.requestFocus()
                Toast.makeText(binding.root.context, "description can not be empty", Toast.LENGTH_LONG).show()
            } else
                uploadImageAndSaveRecord(mCurrentPhotoPath);
        }
        binding.imgPost.setOnClickListener() {
            selectImageFromGallery()
        }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(binding.root.context) // optional usage

            val imageFile = File(uriFilePath)
            if (imageFile.isFile) {
                mCurrentPhotoPath = uriFilePath
                binding.imgPost.setImageURI(null)
                binding.imgPost.refreshDrawableState()
                binding.imgPost.setImageURI(Uri.fromFile(imageFile))
                binding.imgPost.refreshDrawableState()
            }
        }
    }

    private fun goToHome(){
        val fragment = SocialHomeFragment()
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.social_layout_fragment, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun selectImageFromGallery() {
        cropImage.launch(
            options {
                setGuidelines(CropImageView.Guidelines.ON)
                setActivityTitle("habit image")
            }
        )
    }

    private fun uploadImageAndSaveRecord(imageName: String?) {
        var uploadedFileName = ""
        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var loadingProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        loadingProgress.showDialog()
        var profilePic: MultipartBody.Part? = null
        if (imageName != null) {
            uploadedFileName = ""
            val file = File(imageName)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            profilePic = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var apiInterface = ApiInterface.create("").uploadFile(profilePic)
            apiInterface.enqueue(object : Callback<UploadResponseDto> {
                override fun onResponse(
                    call: Call<UploadResponseDto>,
                    response: Response<UploadResponseDto>
                ) {
                    if (response.code() == 200) {
                        if (response.body()!!.isSuccess) {
                            if (response.body()!!.data != null) {
                                if (response.body()!!.data?.count() == 1) {
                                    try {
                                        uploadedFileName = response.body()!!.data?.first()?.fileName.toString()
                                        sendSocialPost(uploadedFileName)
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
            sendSocialPost("")
    }

    private fun sendSocialPost(imageName: String) {
        val postModel = NewSocialPostModel(binding.txtTitle.text.toString(), binding.txtDescription.text.toString(), imageName)
        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).addSocialPost(postModel)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<SocialPostResponse> {
            override fun onResponse(
                call: Call<SocialPostResponse>,
                response: Response<SocialPostResponse>
            ) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        val fragment = SocialHomeFragment()
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

            override fun onFailure(call: Call<SocialPostResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}