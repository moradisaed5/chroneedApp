package com.chroneed.chroneedapp.ui.medicine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalMedicineListResponse
import com.chroneed.chroneedapp.data.medical.GetMedicineListModelResponse
import com.chroneed.chroneedapp.data.medical.MedicalMedicineModel
import com.chroneed.chroneedapp.data.medical.MedicineModels
import com.chroneed.chroneedapp.databinding.FragmentUserMedicinesListBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.medicine.adapter.MedicineAdapter
import com.chroneed.chroneedapp.ui.medicine.adapter.UserMedicineAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserMedicinesListFragment : Fragment() {
    private var _binding: FragmentUserMedicinesListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserMedicinesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        loadUserMedicineData()
    }

    private fun loadUserMedicineData() {
        binding.txtSearchBox.visibility = View.GONE
        var models: MutableList<MedicalMedicineModel> = mutableListOf()
        lateinit var modelsAdapter: UserMedicineAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getUserMedicineByUser(0, 100)
        apiInterface.enqueue(object : Callback<GetMedicalMedicineListResponse> {
            override fun onResponse(call: Call<GetMedicalMedicineListResponse>, response: Response<GetMedicalMedicineListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    models.clear()//Todo: need to load in first time
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { models.addAll(it) }
                    }
                    modelsAdapter = UserMedicineAdapter(models)
                    binding.medicineListRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.medicineListRecycler.adapter = modelsAdapter
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalMedicineListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun loadAllMedicineData(search: String) {
        binding.txtSearchBox.visibility = View.VISIBLE
        var models: MutableList<MedicineModels> = mutableListOf()
        lateinit var modelsAdapter: MedicineAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).searchMedicineList(search)
        apiInterface.enqueue(object : Callback<GetMedicineListModelResponse> {
            override fun onResponse(call: Call<GetMedicineListModelResponse>, response: Response<GetMedicineListModelResponse>) {
                if (response.body()!!.isSuccess == true) {
                    models.clear()//Todo: need to load in first time
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { models.addAll(it) }
                    }
                    modelsAdapter = MedicineAdapter(models)
                    binding.medicineListRecycler.layoutManager = LinearLayoutManager(activity)
                    binding.medicineListRecycler.adapter = modelsAdapter
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicineListModelResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun initEvents() {
        val fragmentNew = UserMedicineAddFragment()
        binding.btnNewCarePlan.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragmentNew)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnAllMedicine.setOnClickListener {
            binding.txtSearchBox.visibility = View.VISIBLE
        }

        binding.txtSearch.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (binding.txtSearch.text.toString().length > 2)
                    loadAllMedicineData(binding.txtSearch.text.toString())
            }
        })

        binding.btnAllUserMedicine.setOnClickListener {
            loadUserMedicineData()
        }
    }

}