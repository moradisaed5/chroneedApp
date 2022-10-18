package com.chroneed.chroneedapp.ui.medicalprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.CarePlanModel
import com.chroneed.chroneedapp.data.medical.GetCarePlanListResponse
import com.chroneed.chroneedapp.data.medical.GetMedicalRecordListResponse
import com.chroneed.chroneedapp.data.medical.MedicalRecordModel
import com.chroneed.chroneedapp.databinding.FragmentMedicalProfileListBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.careplan.CareplanNewFragment
import com.chroneed.chroneedapp.ui.careplan.adapter.CarePlanAdapter
import com.chroneed.chroneedapp.ui.medicalprofile.adapter.MedicalProfileAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MedicalProfileListFragment : Fragment() {
    private var _binding: FragmentMedicalProfileListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMedicalProfileListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
        loadData()
    }

    private fun initEvent() {
        binding.btnBack.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnNewMedicalRecord.setOnClickListener{
            val fragmentNew = MedicalProfileNewFragment()
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragmentNew)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    private fun loadData() {
        var models: MutableList<MedicalRecordModel> = mutableListOf()
        lateinit var modelsAdapter: MedicalProfileAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicalRecordByUser(0, 100)
        apiInterface.enqueue(object : Callback<GetMedicalRecordListResponse> {
            override fun onResponse(call: Call<GetMedicalRecordListResponse>, response: Response<GetMedicalRecordListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        models.clear()//Todo: need to load in first time
                        response.body()!!.data?.let { models.addAll(it) }
                        modelsAdapter = activity?.let { MedicalProfileAdapter(models, it) }!!

                        binding.medicalRecordListRecycler.layoutManager = LinearLayoutManager(activity)
                        binding.medicalRecordListRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }
            override fun onFailure(call: Call<GetMedicalRecordListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}