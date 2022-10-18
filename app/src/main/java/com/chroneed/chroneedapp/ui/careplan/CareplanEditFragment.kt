package com.chroneed.chroneedapp.ui.careplan

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.CarePlanModel
import com.chroneed.chroneedapp.data.medical.GetCarePlanResponse
import com.chroneed.chroneedapp.data.medical.UpdateCarePlanRequest
import com.chroneed.chroneedapp.databinding.FragmentCareplanEditBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class CareplanEditFragment : Fragment() {
    private var _binding: FragmentCareplanEditBinding? = null
    private val binding get() = _binding!!
    private var id: String = ""

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
        _binding = FragmentCareplanEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        initEvent()
    }

    private fun initEvent() {
        binding.txtPlanDate.setOnClickListener {
            getSelectStartDate()
        }
        binding.txtPlanDate.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                getSelectStartDate()
            }
        }
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnSave.setOnClickListener {
            if (!validateForm())
                return@setOnClickListener
            saveForm()
        }
    }

    private fun saveForm() {
        val doctorName = binding.txtDoctorName.text.toString()
        val description = binding.txtDescription.text.toString()
        val planDate = binding.txtPlanDate.text.toString() + "T00:00:00"
        val model = UpdateCarePlanRequest(id, doctorName, planDate, description)

        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).updateUserCarePlan(model)
        apiInterface.enqueue(object : Callback<GetCarePlanResponse> {
            override fun onResponse(call: Call<GetCarePlanResponse>, response: Response<GetCarePlanResponse>) {
                if (response.body()!!.isSuccess == true) {
                    activity?.supportFragmentManager?.popBackStack()
                }
                Toast.makeText(binding.root.context, response.body()!!.message, Toast.LENGTH_SHORT).show()
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetCarePlanResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun initView(model: CarePlanModel) {
        binding.txtDoctorName.setText(model.doctorName)
        binding.txtPlanDate.setText(model.planDate.split("T")[0])
        binding.txtDescription.setText(model.planDescription)
    }

    private fun loadData() {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getUserCarePlan(id)
        apiInterface.enqueue(object : Callback<GetCarePlanResponse> {
            override fun onResponse(call: Call<GetCarePlanResponse>, response: Response<GetCarePlanResponse>) {
                if (response.body()!!.isSuccess == true) {
                    initView(response.body()!!.data!!)
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetCarePlanResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun validateForm(): Boolean {
        if (binding.txtDoctorName.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "doctor name is required!", Toast.LENGTH_SHORT).show()
            return false
        }
        if (binding.txtPlanDate.text.isNullOrEmpty()) {
            Toast.makeText(binding.root.context, "plan date is required!", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun getSelectStartDate() {
        val myCalendar = Calendar.getInstance()
        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "yyyy-MM-dd"
            val dateFormat = SimpleDateFormat(myFormat, Locale.US)
            binding.txtPlanDate.setText(dateFormat.format(myCalendar.time))
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