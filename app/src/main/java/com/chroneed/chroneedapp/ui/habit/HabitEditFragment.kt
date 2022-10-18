package com.chroneed.chroneedapp.ui.habit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalHabitResponse
import com.chroneed.chroneedapp.data.medical.MedicalHabitModel
import com.chroneed.chroneedapp.data.medical.UpdateMedicalHabitModel
import com.chroneed.chroneedapp.data.medical.enumDayOfWeek
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentHabitEditBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.ImageFilePath
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import okhttp3.MediaType
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class HabitEditFragment : Fragment() {
    private var _binding: FragmentHabitEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var id: String
    private var mCurrentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHabitEditBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initEvents()
    }

    private fun initEvents() {
        selectStartDate()
        selectEndDate()
        selectTime()
        binding.fragmentPrescriptionEditNewMedicineImageShow.setOnClickListener() {
            selectImageFromGallery()
        }
        binding.fragmentHabitEditBtnSave.setOnClickListener() {
            //imageName = uploadImage(mCurrentPhotoPath!!).wait().toString()
            val data = getFormData()
            if (data?.image!!.isNotEmpty()) {
                uploadImage(mCurrentPhotoPath!!, data)
            } else {
                saveRecord(data)
            }
        }
      binding.fragmentPrescriptionEditBack.setOnClickListener(){
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun fillForm(model: MedicalHabitModel) {
        binding.fragmentHabitEditTitle.setText(model.title)
        binding.fragmentHabitEditDescription.setText(model.description)
        binding.fragmentPrescriptionEditStartDate.setText(model.strStartDate)
        binding.fragmentPrescriptionEditEndDate.setText(model.strEndDate)
        binding.fragmentPrescriptionEditRemindTime.setText(model.strRemindTime)
        //fill week days
        if (model.daysOfWeek!!.contains(enumDayOfWeek.Su.value))
            binding.fragmentHabitEditWeekDaySU.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.Mo.value))
            binding.fragmentHabitEditWeekDayMo.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.Tu.value))
            binding.fragmentHabitEditWeekDayTu.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.We.value))
            binding.fragmentHabitEditWeekDayWe.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.Th.value))
            binding.fragmentHabitEditWeekDayTh.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.Fr.value))
            binding.fragmentHabitEditWeekDayFr.isChecked = true

        if (model.daysOfWeek!!.contains(enumDayOfWeek.Sa.value))
            binding.fragmentHabitEditWeekDaySa.isChecked = true


        //load image
        if (model.image != null) {
            if (model.image!!.isNotEmpty()) {
                this.context?.let {
                    Glide
                        .with(it)
                        .load("http://chroneed.saeid-moradi.ir/files/${model.image}")
                        .override(binding.fragmentPrescriptionEditNewMedicineImageShow.width)
                        .into(binding.fragmentPrescriptionEditNewMedicineImageShow)
                }
            }
        }
    }

    private fun loadData() {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()

        try {
            var apiInterface = ApiInterface.create(token).getMedicalHabit(id = id)
            apiInterface.enqueue(object : Callback<GetMedicalHabitResponse> {
                override fun onResponse(call: Call<GetMedicalHabitResponse>, response: Response<GetMedicalHabitResponse>) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
//                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show()
                        response.body()!!.data?.let { fillForm(it) }

                    }
                }

                override fun onFailure(call: Call<GetMedicalHabitResponse>, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                    myProgress.closeDialog()
                }
            })
        } catch (ex: Exception) {
            myProgress.closeDialog()
            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show();
        }
    }

    private fun getFormData(): UpdateMedicalHabitModel? {
        var title = "";
        var description = "";
        var startDate = "";
        var endDate = "";
        var remindTime = "";
        var imageName = ""
        var daysOfWeek: MutableList<Int> = mutableListOf()
        try {
            title = binding.fragmentHabitEditTitle.text.toString()
        } catch (ex: Exception) {
        }
        try {
            description = binding.fragmentHabitEditDescription.text.toString()
        } catch (ex: Exception) {
        }
        try {
            startDate = binding.fragmentPrescriptionEditStartDate.text.toString()
        } catch (ex: Exception) {
        }
        try {
            endDate = binding.fragmentPrescriptionEditEndDate.text.toString()
        } catch (ex: Exception) {
        }
        try {
            remindTime = binding.fragmentPrescriptionEditRemindTime.text.toString()
        } catch (ex: Exception) {
        }
        if (mCurrentPhotoPath == null)
            mCurrentPhotoPath = ""
        if (mCurrentPhotoPath!!.isNotEmpty()) {
            imageName = mCurrentPhotoPath.toString()
        }
        if (binding.fragmentHabitEditWeekDaySU.isChecked)
            daysOfWeek.add(enumDayOfWeek.Su.value)
        if (binding.fragmentHabitEditWeekDayMo.isChecked)
            daysOfWeek.add(enumDayOfWeek.Mo.value)
        if (binding.fragmentHabitEditWeekDayTu.isChecked)
            daysOfWeek.add(enumDayOfWeek.Tu.value)
        if (binding.fragmentHabitEditWeekDayWe.isChecked)
            daysOfWeek.add(enumDayOfWeek.We.value)
        if (binding.fragmentHabitEditWeekDayTh.isChecked)
            daysOfWeek.add(enumDayOfWeek.Th.value)
        if (binding.fragmentHabitEditWeekDayFr.isChecked)
            daysOfWeek.add(enumDayOfWeek.Fr.value)
        if (binding.fragmentHabitEditWeekDaySa.isChecked)
            daysOfWeek.add(enumDayOfWeek.Sa.value)
        if (title.isNullOrEmpty()) {
            Toast.makeText(this.context, "enter habit title!", Toast.LENGTH_SHORT).show()
            binding.fragmentHabitEditTitle.requestFocus()
            return null
        }
        if (remindTime.isNullOrEmpty()) {
            Toast.makeText(this.context, "enter habit remind time!", Toast.LENGTH_SHORT).show()
            binding.fragmentPrescriptionEditRemindTime.requestFocus()
            return null
        }
        //imageName = uploadImage(mCurrentPhotoPath!!).wait().toString()
        return UpdateMedicalHabitModel(
            daysOfWeek,
            description,
            id,
            endDate,
            imageName,
            remindTime,
            startDate,
            title
        )
    }

    private fun uploadImage(imageName: String, model: UpdateMedicalHabitModel) {
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
            var apiInterface = ApiInterface.create(token).uploadFile(profilePic)
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
                    Toast.makeText(context, "can't connect to server!", Toast.LENGTH_LONG).show();
                    return
                }
            })

        }
    }

    private fun saveRecord(data: UpdateMedicalHabitModel) {

        if (data != null) {
            val token = "Bearer " + MySharedPreferences.getUserToken(context)
            var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
            var apiInterface = ApiInterface.create(token).updateMedicalHabit(data)
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



    private fun selectTime() {
        val cal = Calendar.getInstance()

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.fragmentPrescriptionEditRemindTime.setText(dateFormat.format(cal.time))
        }

        binding.fragmentPrescriptionEditRemindTime.setOnClickListener {
            TimePickerDialog(
                context,
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionEditRemindTime.setOnFocusChangeListener { v, hasFocus ->
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

    private fun selectStartDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)

            binding.fragmentPrescriptionEditStartDate.setText(dateFormat.format(myCalendar.time))
        }

        binding.fragmentPrescriptionEditStartDate.setOnClickListener() {
            DatePickerDialog(
                binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionEditStartDate.setOnFocusChangeListener { v, hasFocus ->
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

    private fun selectEndDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.fragmentPrescriptionEditEndDate.setText(dateFormat.format(myCalendar.time))
        }

        binding.fragmentPrescriptionEditEndDate.setOnClickListener() {
            DatePickerDialog(
                binding.root.context, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
            hideKeyboard()
        }
        binding.fragmentPrescriptionEditEndDate.setOnFocusChangeListener { v, hasFocus ->
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
                binding.fragmentPrescriptionEditNewMedicineImageShow.setImageURI(null)
                binding.fragmentPrescriptionEditNewMedicineImageShow.refreshDrawableState()
                binding.fragmentPrescriptionEditNewMedicineImageShow.setImageURI(Uri.fromFile(imageFile))
                binding.fragmentPrescriptionEditNewMedicineImageShow.refreshDrawableState()
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