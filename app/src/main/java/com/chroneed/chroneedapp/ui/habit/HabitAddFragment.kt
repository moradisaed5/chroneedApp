package com.chroneed.chroneedapp.ui.habit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalHabitResponse
import com.chroneed.chroneedapp.data.medical.NewMedicalHabitModel
import com.chroneed.chroneedapp.data.medical.enumDayOfWeek
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentHabitAddBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.ImageFilePath
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class HabitAddFragment : Fragment() {
    private var _binding: FragmentHabitAddBinding? = null
    private val binding get() = _binding!!
    private var mCurrentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
//        val a=Uri.parse("file:///storage/emulated/0/DCIM/Camera/IMG_20220731_221109.jpg")
//        binding.fragmentHabitAddNewMedicineImageShow.setImageURI(a)
    }

    private fun initEvents() {
        selectStartDate()
        selectEndDate()
        selectTime()
        binding.fragmentHabitAddNewMedicineImageShow.setOnClickListener() {
            selectImageFromGallery()
        }
        binding.fragmentHabitAddBtnSave.setOnClickListener() {
            //imageName = uploadImage(mCurrentPhotoPath!!).wait().toString()
            val data = getFormData()
            if (data?.image!!.isNotEmpty()) {
                uploadImage(mCurrentPhotoPath!!, data)
            } else {
                saveRecord(data)
            }
        }
    }

    private fun saveRecord(data: NewMedicalHabitModel) {
        if (data != null) {
            val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
            var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
            var apiInterface = ApiInterface.create(token).addMedicalHabit(data)
            myProgress.showDialog()
            apiInterface.enqueue(object : Callback<GetMedicalHabitResponse> {
                override fun onResponse(
                    call: Call<GetMedicalHabitResponse>,
                    response: Response<GetMedicalHabitResponse>
                ) {
                    myProgress.closeDialog()
                    if (response.code() == 200) {
                        if (response.body()!!.isSuccess == true) {
                            myProgress.closeDialog()
                            activity?.supportFragmentManager?.popBackStack()
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

                override fun onFailure(call: Call<GetMedicalHabitResponse>, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                    myProgress.closeDialog()
                }
            })
        }
    }

    private fun getFormData(): NewMedicalHabitModel? {
        var title = "";
        var description = "";
        var startDate = "";
        var endDate = "";
        var remindTime = "";
        var imageName = ""
        var daysOfWeek: MutableList<Int> = mutableListOf()
        try {
            title = binding.fragmentHabitAddTitle.text.toString()
        } catch (ex: Exception) {
        }
        try {
            description = binding.fragmentHabitAddDescription.text.toString()
        } catch (ex: Exception) {
        }
        try {
            startDate = binding.fragmentPrescriptionAddStartDate.text.toString()
        } catch (ex: Exception) {
        }
        try {
            endDate = binding.fragmentPrescriptionAddEndDate.text.toString()
        } catch (ex: Exception) {
        }
        try {

            remindTime = binding.fragmentPrescriptionAddRemindTime.text.toString()
        } catch (ex: Exception) {
        }
        if (mCurrentPhotoPath!!.isNotEmpty()) {
            imageName = mCurrentPhotoPath.toString()
        }

        if (binding.fragmentHabitAddWeekDaySU.isChecked)
            daysOfWeek.add(enumDayOfWeek.Su.value)
        if (binding.fragmentHabitAddWeekDayMo.isChecked)
            daysOfWeek.add(enumDayOfWeek.Mo.value)
        if (binding.fragmentHabitAddWeekDayTu.isChecked)
            daysOfWeek.add(enumDayOfWeek.Tu.value)
        if (binding.fragmentHabitAddWeekDayWe.isChecked)
            daysOfWeek.add(enumDayOfWeek.We.value)
        if (binding.fragmentHabitAddWeekDayTh.isChecked)
            daysOfWeek.add(enumDayOfWeek.Th.value)
        if (binding.fragmentHabitAddWeekDayFr.isChecked)
            daysOfWeek.add(enumDayOfWeek.Fr.value)
        if (binding.fragmentHabitAddWeekDaySa.isChecked)
            daysOfWeek.add(enumDayOfWeek.Sa.value)
        if (title.isNullOrEmpty()) {
            Toast.makeText(this.context, "enter habit title!", Toast.LENGTH_SHORT).show()
            binding.fragmentHabitAddTitle.requestFocus()
            return null
        }
        if (remindTime.isNullOrEmpty()) {
            Toast.makeText(this.context, "enter habit remind time!", Toast.LENGTH_SHORT).show()
            binding.fragmentPrescriptionAddRemindTime.requestFocus()
            return null
        }
        //imageName = uploadImage(mCurrentPhotoPath!!).wait().toString()
        return NewMedicalHabitModel(
            daysOfWeek,
            description,
            endDate,
            imageName,
            remindTime,
            startDate,
            title
        )
    }

    private fun uploadImage(imageName: String, model: NewMedicalHabitModel) {
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
                                        model.image = uploadedFileName
                                        model?.let { it -> saveRecord(it) }
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

        }
    }



    private fun selectTime() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.fragmentPrescriptionAddRemindTime.setText(dateFormat.format(cal.time))
        }
        binding.fragmentPrescriptionAddRemindTime.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionAddRemindTime.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                TimePickerDialog(
                    context,
                    timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    true
                ).show()
                hideKeyboard()
            }
        }
    }

    private fun selectEndDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.fragmentPrescriptionAddEndDate.setText(dateFormat.format(myCalendar.time))
        }
        binding.fragmentPrescriptionAddEndDate.setOnClickListener() {
            DatePickerDialog(
                binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionAddEndDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                        Calendar.MONTH
                    ),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
                hideKeyboard()
            }
        }
    }

    private fun selectStartDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.fragmentPrescriptionAddStartDate.setText(dateFormat.format(myCalendar.time))
        }

        binding.fragmentPrescriptionAddStartDate.setOnClickListener() {
            DatePickerDialog(
                binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionAddStartDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                DatePickerDialog(
                    binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                        Calendar.MONTH
                    ),
                    myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show()
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard() {
        Handler().postDelayed({
            val inputMethodManager =
                this.activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }, 1)
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(binding.root.context) // optional usage

            val imageFile = File(uriFilePath)
            if (imageFile.isFile) {
                mCurrentPhotoPath = uriFilePath
                binding.fragmentHabitAddNewMedicineImageShow.setImageURI(null)
                binding.fragmentHabitAddNewMedicineImageShow.refreshDrawableState()
                binding.fragmentHabitAddNewMedicineImageShow.setImageURI(Uri.fromFile(imageFile))
                binding.fragmentHabitAddNewMedicineImageShow.refreshDrawableState()
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