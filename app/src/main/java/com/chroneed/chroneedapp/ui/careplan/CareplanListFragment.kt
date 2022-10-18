package com.chroneed.chroneedapp.ui.careplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.*
import com.chroneed.chroneedapp.databinding.FragmentCareplanListBinding
import com.chroneed.chroneedapp.databinding.FragmentCommandListBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.alarm.SetAlarmFragment
import com.chroneed.chroneedapp.ui.alarm.adapter.AlarmListAdapter
import com.chroneed.chroneedapp.ui.careplan.adapter.CarePlanAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CareplanListFragment : Fragment() {
    private var _binding: FragmentCareplanListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCareplanListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        loadData()
    }

    private fun loadData() {
        var models: MutableList<CarePlanModel> = mutableListOf()
        lateinit var modelsAdapter: CarePlanAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getUserCarePlanByUser(0, 100)
        apiInterface.enqueue(object : Callback<GetCarePlanListResponse> {
            override fun onResponse(call: Call<GetCarePlanListResponse>, response: Response<GetCarePlanListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        models.clear()//Todo: need to load in first time
                        response.body()!!.data?.let { models.addAll(it) }
                        modelsAdapter = activity?.let { CarePlanAdapter(models, it) }!!

                        binding.carePlanListRecycler.layoutManager = LinearLayoutManager(activity)
                        binding.carePlanListRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetCarePlanListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun initEvents() {
        val fragmentNew = CareplanNewFragment()
        binding.btnNewCarePlan.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragmentNew)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.btnBack.setOnClickListener{
            activity?.supportFragmentManager?.popBackStack()
        }
    }
}