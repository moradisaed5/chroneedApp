package com.chroneed.chroneedapp.ui.alarm

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalAlarmResponse
import com.chroneed.chroneedapp.data.medical.MedicalAlarmModel
import com.chroneed.chroneedapp.data.user.UploadResponseDto
import com.chroneed.chroneedapp.databinding.FragmentSetAlarmBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SetAlarmFragment : Fragment() {

    private var id: String = ""
    private var mCurrentPhotoPath = ""
    private var requestCode = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "")
        }
    }

    private var _binding: FragmentSetAlarmBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSetAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        initView()
        loadData()
    }


    private fun initEvents() {
        binding.btnImgSelect.setOnClickListener {
            requestCode = CodeManager.RETURN_IMAGE_Alarm_MEDICINE
            cropImage.launch(
                options {
                    setGuidelines(CropImageView.Guidelines.ON)
                    setActivityTitle("select your alarm image")
                }
            )
        }
        binding.btnClearImage.setOnClickListener {
            mCurrentPhotoPath = ""
            binding.imgView.setImageURI(null)
            binding.imgView.refreshDrawableState()

            binding.imgView.visibility = View.GONE
            binding.btnClearImage.visibility = View.GONE
            binding.btnImgSelect.visibility = View.VISIBLE
        }

        binding.btnEndTimeNever.setOnClickListener {
            binding.endDateLayout.visibility = View.GONE
        }
        binding.btnEndTimeOn.setOnClickListener {
            binding.endDateLayout.visibility = View.VISIBLE
        }
        binding.selectAlarmEvery.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                setViewUseEvery(binding.txtAlarmEveryDurationSelect.text.toString())
            }
        }
        binding.txtAlarmEveryDurationSelect.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                setViewUseEvery(binding.txtAlarmEveryDurationSelect.text.toString())
            }
        })
        binding.btnSetAlarm.setOnClickListener {
            if (!validateForm())
                return@setOnClickListener

            if (!mCurrentPhotoPath.isNullOrEmpty())
                uploadImage(getMedicalAlarm())
            else
                if (id.isNullOrEmpty())
                    setAlarm(getMedicalAlarm())
                else
                    updateAlarm(getMedicalAlarm())
        }
        binding.txtSelectStartDate.setOnClickListener {
            getSelectStartDate()
        }
        binding.txtSelectStartDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                getSelectStartDate()
            }
        }

        binding.txtEndDate.setOnClickListener {
            getSelectEndDate()
        }
        binding.txtEndDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                getSelectEndDate()
            }
        }
        binding.txtSelectStartTime.setOnClickListener {
            getSelectStartTime()
        }
        binding.txtSelectStartTime.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                getSelectStartTime()
            }
        }
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun getSelectStartTime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            val myFormat = "HH:mm"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.txtSelectStartTime.setText(dateFormat.format(cal.time))
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
        hideKeyboard()
    }


    private fun getSelectStartDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.txtSelectStartDate.setText(dateFormat.format(myCalendar.time))
        }
        this.context?.let { it ->
            DatePickerDialog(
                it, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        hideKeyboard()
    }
    private fun getSelectEndDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.txtEndDate.setText(dateFormat.format(myCalendar.time))
        }
        this.context?.let { it ->
            DatePickerDialog(
                it, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(
                    Calendar.MONTH
                ),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        hideKeyboard()
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // use the returned uri
            val uriContent = result.uriContent
            val uriFilePath = result.getUriFilePath(binding.root.context) // optional usage

            val imageFile = File(uriFilePath)
            if (imageFile.isFile) {
                mCurrentPhotoPath = uriFilePath!!

                if (requestCode == CodeManager.RETURN_IMAGE_Alarm_MEDICINE) {


                    val image = binding.imgView
                    Glide.with(binding.root.context).load(Uri.fromFile(imageFile))
                        .placeholder(R.drawable.ic_gallery)
                        .into(image)

                    binding.imgView.visibility = View.VISIBLE
                    binding.btnClearImage.visibility = View.VISIBLE
                    binding.btnImgSelect.visibility = View.GONE
                }

            }
        }
    }

    private fun initView(model: MedicalAlarmModel) {
        binding.txtTitle.setText(model.title)
        binding.txtDescription.setText(model.description)
        //load image
        if (!model.image.isNullOrEmpty()) {
            binding.btnClearImage.visibility = View.VISIBLE
            binding.imgView.visibility = View.VISIBLE
            binding.btnImgSelect.visibility = View.GONE

            val image = binding.imgView
            Glide.with(binding.root.context).load("http://chroneedapi.com/identity.api/files/${model.image}")
                .placeholder(R.drawable.ic_gallery)
                .into(image)
        } else {
            binding.imgView.visibility = View.GONE
        }
        val endType = getEndType(model.endType!!)
        when (endType) {
            "never" -> setViewEndTypeNever()
            "on" -> setViewEndTypeOn(model.endDate.toString())
            else -> setViewEndTypeOn(model.endDate.toString())
        }
        var startDate = ""
        var startTime = ""
        try {
            startDate = model.startDate.toString().toLowerCase().split("t")[0]
            startTime = model.startDate.toString().toLowerCase().split("t")[1].split(".")[0]
            startTime = startTime.split(":")[0] + ":" + startTime.split(":")[1]
        } catch (e: Exception) {
        }
        binding.txtSelectStartDate.setText(startDate)
        binding.txtSelectStartTime.setText(startTime)
        setViewUseEvery(model.repeatEvery.toString())
        var every = model.repeatEveryType!!.toInt() - 1
        if (every == -1)
            every = 0
        binding.selectAlarmEvery.setSelection(every)
        setViewWeek(model.dayOfWeeks!!)
        binding.txtAlarmEveryDurationSelect.setText(model.repeatEvery.toString())
    }

    private fun initView() {
        binding.btnClearImage.visibility = View.GONE
        binding.imgView.visibility = View.GONE
        binding.btnImgSelect.visibility = View.VISIBLE

        binding.btnEndTimeOn.isChecked = true

    }

    private fun setViewUseEvery(repeatEvery: String) {

        val selectedItem = binding.selectAlarmEvery.selectedItem.toString()
        binding.txtAlarmEveryDurationLayout.setHint("Use every $repeatEvery $selectedItem")
    }

    private fun setViewEndTypeOn(value: String) {
        var endDate = ""
        try {
            endDate = value.toLowerCase().split("t")[0]
        } catch (e: Exception) {
        }
        binding.btnEndTimeOn.isChecked = true
        binding.txtEndDate.setText(endDate)
    }

    private fun setViewEndTypeNever() {
        binding.endDateLayout.visibility = View.GONE
        binding.btnEndTimeNever.isChecked = true
        binding.txtEndDate.setText("")
    }

    private fun setViewWeek(model: List<Int>) {
        if (model.contains(0)) {//Su
            binding.btnWeekdaySu.isChecked = true
        }
        if (model.contains(1)) {//Mo
            binding.btnWeekdayMo.isChecked = true
        }
        if (model.contains(2)) {//Tu
            binding.btnWeekdayTu.isChecked = true
        }
        if (model.contains(3)) {//We
            binding.btnWeekdayWe.isChecked = true
        }
        if (model.contains(4)) {//Th
            binding.btnWeekdayTh.isChecked = true
        }
        if (model.contains(5)) {//Fr
            binding.btnWeekdayFr.isChecked = true
        }
        if (model.contains(6)) {//Sa
            binding.btnWeekdaySa.isChecked = true
        }
    }

    private fun validateForm(): Boolean {
        if (binding.txtTitle.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "title is required ...!", Toast.LENGTH_LONG).show()
            binding.txtTitle.requestFocus()
            return false
        }
        if (binding.txtSelectStartDate.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "start date is required ...!", Toast.LENGTH_LONG).show()
            binding.txtSelectStartDate.requestFocus()
            return false
        }
        if (binding.txtSelectStartTime.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "start time is required ...!", Toast.LENGTH_LONG).show()
            binding.txtSelectStartTime.requestFocus()
            return false
        }
        if (binding.txtAlarmEveryDurationSelect.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "every duration time is required ...!", Toast.LENGTH_LONG).show()
            binding.txtAlarmEveryDurationSelect.requestFocus()
            return false
        }
        if (binding.btnEndTimeOn.isChecked)
            if (binding.txtEndDate.text.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "end date is required ...!", Toast.LENGTH_LONG).show()
                binding.txtEndDate.requestFocus()
                return false
            }
        return true
    }


    private fun uploadImage(model: MedicalAlarmModel) {
        var loadingProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        loadingProgress.showDialog()
        var token = "Bearer " + MySharedPreferences.getUserToken(context)
        var profilePic: MultipartBody.Part? = null
        if (mCurrentPhotoPath != null) {
            var uploadedFileName = ""
            val file = File(mCurrentPhotoPath)
            val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            profilePic = MultipartBody.Part.createFormData("file", file.name, requestFile)
            var apiInterface = ApiInterface.create(token).uploadFile(profilePic)
            apiInterface.enqueue(object : Callback<UploadResponseDto> {
                override fun onResponse(call: Call<UploadResponseDto>, response: Response<UploadResponseDto>) {
                    if (response.code() == 200) {
                        if (response.body()!!.isSuccess) {
                            if (response.body()!!.data != null) {
                                uploadedFileName = response.body()!!.data?.first()?.fileName.toString()
                                model.image = uploadedFileName
                                if (id.isNullOrEmpty())
                                    setAlarm(model)
                                else
                                    updateAlarm(model)
                            }
                        } else
                            Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                                .show();
                    } else
                        Toast.makeText(context, "problem to upload file!", Toast.LENGTH_LONG)
                            .show();

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

    private fun updateAlarm(model: MedicalAlarmModel) {
        model.id = id
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).updateMedicationAlarm(model)
        apiInterface.enqueue(object : Callback<GetMedicalAlarmResponse> {
            override fun onResponse(call: Call<GetMedicalAlarmResponse>, response: Response<GetMedicalAlarmResponse>) {
                if (response.body()!!.isSuccess == true) {
                    activity?.supportFragmentManager?.popBackStack()
                }
                Toast.makeText(binding.root.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalAlarmResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun setAlarm(model: MedicalAlarmModel) {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).addMedicationAlarm(model)
        apiInterface.enqueue(object : Callback<GetMedicalAlarmResponse> {
            override fun onResponse(call: Call<GetMedicalAlarmResponse>, response: Response<GetMedicalAlarmResponse>) {
                if (response.body()!!.isSuccess == true) {
                    activity?.supportFragmentManager?.popBackStack()
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalAlarmResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun getMedicalAlarm(): MedicalAlarmModel {
        val listOfWeek: MutableList<Int> = mutableListOf()
        val title = binding.txtTitle.text.toString()
        val description = binding.txtDescription.text.toString()
        var alarmEvery = 0//hour=1,day=2,week=3,month=4,year=5
        alarmEvery = when (binding.selectAlarmEvery.selectedItem.toString()) {
            "hour" -> 1
            "day" -> 2
            "week" -> 3
            "month" -> 4
            "year" -> 5
            else -> 1
        }
        val startAlarmTime = binding.txtSelectStartTime.text.toString()
        val startAlarmDate = binding.txtSelectStartDate.text.toString() + "T" + startAlarmTime
        var useEveryTime = 0
        try {
            useEveryTime = binding.txtAlarmEveryDurationSelect.text.toString().toInt()
        } catch (e: Exception) {
        }


        if (binding.btnWeekdaySu.isChecked)
            listOfWeek.add(0)
        if (binding.btnWeekdayMo.isChecked)
            listOfWeek.add(1)
        if (binding.btnWeekdayTu.isChecked)
            listOfWeek.add(2)
        if (binding.btnWeekdayWe.isChecked)
            listOfWeek.add(3)
        if (binding.btnWeekdayTh.isChecked)
            listOfWeek.add(4)
        if (binding.btnWeekdayFr.isChecked)
            listOfWeek.add(5)
        if (binding.btnWeekdaySa.isChecked)
            listOfWeek.add(6)
        var endType = 1;
        var endDate = ""
        if (binding.btnEndTimeOn.isChecked) {
            endType = 2
            endDate = binding.txtEndDate.text.toString() + "T" + startAlarmTime
        }
        return MedicalAlarmModel(listOfWeek.toList(), description, endDate, endType, null, mCurrentPhotoPath, null, useEveryTime, alarmEvery, startAlarmDate, title)
    }


    private fun loadData() {
        if (id.isNullOrEmpty())
            return

        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicationAlarm(id)
        apiInterface.enqueue(object : Callback<GetMedicalAlarmResponse> {
            override fun onResponse(call: Call<GetMedicalAlarmResponse>, response: Response<GetMedicalAlarmResponse>) {
                if (response.body()!!.isSuccess == true) {
                    initView(response.body()!!.data)
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalAlarmResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun getRepeatEveryType(value: Int): String {
        return when (value) {
            0 -> "unknown"
            1 -> "hour"
            2 -> "day"
            3 -> "week"
            4 -> "month"
            5 -> "year"
            else -> "unknown"
        }
    }

    private fun getEndType(value: Int): String {
        return when (value) {
            0 -> "unknown"
            1 -> "never"
            2 -> "on"
            3 -> "after"
            else -> "unknown"
        }
    }

    private fun hideKeyboard() {
        Handler().postDelayed({
            val inputMethodManager =
                this.activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }, 1)
    }
}