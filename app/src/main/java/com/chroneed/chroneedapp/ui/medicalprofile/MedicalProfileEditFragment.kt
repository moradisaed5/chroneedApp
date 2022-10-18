package com.chroneed.chroneedapp.ui.medicalprofile

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.*
import com.chroneed.chroneedapp.databinding.FragmentMedicalProfileEditBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class MedicalProfileEditFragment : Fragment() {
    private var _binding: FragmentMedicalProfileEditBinding? = null
    private val binding get() = _binding!!

    private lateinit var id: String
    var typeModel: MutableList<MedicalRecordType> = mutableListOf()
    var stageModel: MutableList<MedicalRecordStage> = mutableListOf()
    private lateinit var formModel: MedicalRecordModel

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
        _binding = FragmentMedicalProfileEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        initView()
    }


    private fun initEvent() {
        binding.txtStartDate.setOnClickListener {
            getSelectStartDate()
        }
        binding.txtStartDate.setOnFocusChangeListener { v, hasFocus ->
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
        binding.btnSave.setOnClickListener {
            if (formIsValid())
                saveRecord()
        }
    }

    private fun saveRecord() {
        if (!formIsValid())
            return
        var recordStage = binding.selectMedicalRecordStage.selectedItem.toString()
        var recordType = binding.selectMedicalRecordType.selectedItem.toString()
        var typeId = ""
        var stageId = ""
        typeModel.forEach {
            if (it.name == recordType)
                typeId = it.id.toString()
        }
        stageModel.forEach {
            if (it.name == recordStage)
                stageId = it.id.toString()
        }

        val subject = binding.txtSubject.text.toString()
        val startDate = binding.txtStartDate.text.toString()
        val endDate = binding.txtEndDate.text.toString()
        val isTreatmentIsOver = binding.chkIsTreatmentIsOver.isChecked
        val description = binding.txtDescription.text.toString()
        val images: MutableList<String> = mutableListOf()

        var model = UpdateMedicalRecordRequest(id,subject, startDate, endDate, isTreatmentIsOver, description, images, typeId, stageId)

        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).updateMedicalRecord(model)
        apiInterface.enqueue(object : Callback<GetMedicalRecordResponse> {
            override fun onResponse(call: Call<GetMedicalRecordResponse>, response: Response<GetMedicalRecordResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data != null) {
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalRecordResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })

    }

    private fun formIsValid(): Boolean {
        try {
            var recordStage = binding.selectMedicalRecordStage.selectedItem.toString()
            var recordType = binding.selectMedicalRecordType.selectedItem.toString()
            var typeId = ""
            var stageId = ""
            typeModel.forEach {
                if (it.name == recordType)
                    typeId = it.id.toString()
            }
            stageModel.forEach {
                if (it.name == recordStage)
                    stageId = it.id.toString()
            }

            val subject = binding.txtSubject.text.toString()
            val startDate = binding.txtStartDate.text.toString()
            val endDate = binding.txtEndDate.text.toString()
            val isTreatmentIsOver = binding.chkIsTreatmentIsOver.isChecked
            val description = binding.txtDescription.text.toString()
            if (subject.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "please enter subject ...", Toast.LENGTH_SHORT).show()
                return false
            }

            if (typeId.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "please select medical type ...", Toast.LENGTH_SHORT).show()
                return false
            }

            if (stageId.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "please select medical stage ...", Toast.LENGTH_SHORT).show()
                return false
            }

            if (startDate.isNullOrEmpty()) {
                Toast.makeText(binding.root.context, "please select start date ...", Toast.LENGTH_SHORT).show()
                return false
            }

            return true

        } catch (e: Exception) {
            Toast.makeText(binding.root.context, e.message, Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun initView() {
        loadData()


    }

    private fun loadData() {

        var model: MutableList<String> = mutableListOf()
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicalRecord(id)
        apiInterface.enqueue(object : Callback<GetMedicalRecordResponse> {
            override fun onResponse(call: Call<GetMedicalRecordResponse>, response: Response<GetMedicalRecordResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data != null) {
                        formModel = response.body()!!.data!!
                        binding.txtSubject.setText(formModel.subject.toString())
                        binding.txtStartDate.setText(formModel.startDate.toString())
                        binding.txtEndDate.setText(formModel.endDate.toString())
                        if (formModel.isTreatmentIsOver == true)
                            binding.chkIsTreatmentIsOver.isChecked = true
                        binding.txtDescription.setText(formModel.description.toString())

                        loadMedicalStage()
                        loadMedicalType()
                    } else {
                        Toast.makeText(binding.root.context, "record not found!", Toast.LENGTH_SHORT).show()
                        activity?.supportFragmentManager?.popBackStack()
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalRecordResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun loadMedicalType() {
        var model: MutableList<String> = mutableListOf()
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicalRecordTypeList(0, 100)
        apiInterface.enqueue(object : Callback<GetMedicalRecordTypeListResponse> {
            override fun onResponse(call: Call<GetMedicalRecordTypeListResponse>, response: Response<GetMedicalRecordTypeListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        var selectedIndex = 0
                        var selectedName = ""

                        typeModel = response.body()!!.data!!.toMutableList()
                        response.body()!!.data!!.forEach {
                            model.add(it.name!!)
                            if (it.id == formModel.medicalRecordTypeId)
                                selectedName = it.name.toString()
                        }
                        selectedIndex = model.indexOf(selectedName)
                        binding.selectMedicalRecordType.adapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_list_item_1, model)
                        if (selectedIndex != 0)
                            binding.selectMedicalRecordType.setSelection(selectedIndex)
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalRecordTypeListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun loadMedicalStage() {
        var model: MutableList<String> = mutableListOf()
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicalRecordStageList(0, 100)
        apiInterface.enqueue(object : Callback<GetMedicalRecordStageListResponse> {
            override fun onResponse(call: Call<GetMedicalRecordStageListResponse>, response: Response<GetMedicalRecordStageListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        var selectedIndex = 0
                        var selectedName = ""
                        stageModel = response.body()!!.data!!.toMutableList()
                        response.body()!!.data!!.forEach {
                            model.add(it.name!!)
                            if (it.id == formModel.medicalRecordStageId)
                                selectedName = it.name.toString()
                        }
                        selectedIndex = model.indexOf(selectedName)
                        binding.selectMedicalRecordStage.adapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_list_item_1, model)
                        if (selectedIndex != 0)
                            binding.selectMedicalRecordStage.setSelection(selectedIndex)
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalRecordStageListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun getSelectStartDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.txtStartDate.setText(dateFormat.format(myCalendar.time))
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

    private fun hideKeyboard() {
        Handler().postDelayed({
            val inputMethodManager =
                this.activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        }, 1)
    }
}