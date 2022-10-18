package com.chroneed.chroneedapp.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetMedicalAlarmListResponse
import com.chroneed.chroneedapp.data.medical.MedicalAlarmModel
import com.chroneed.chroneedapp.databinding.FragmentShowAlarmsBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.alarm.adapter.AlarmListAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowAlarmsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private var _binding: FragmentShowAlarmsBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentShowAlarmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
        loadData()
    }
    private fun initEvents() {
        val fragmentNew = SetAlarmFragment()
        binding.btnNewAlarm.setOnClickListener {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragmentNew)
                ?.addToBackStack(null)
                ?.commit()
        }
    }
    private fun loadData() {
        var models: MutableList<MedicalAlarmModel> = mutableListOf()
        lateinit var modelsAdapter: AlarmListAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getMedicationAlarmByUser(0, 100)
        apiInterface.enqueue(object : Callback<GetMedicalAlarmListResponse> {
            override fun onResponse(call: Call<GetMedicalAlarmListResponse>,response: Response<GetMedicalAlarmListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        models.clear()//Todo: need to load in first time
                        response.body()!!.data?.let { models.addAll(it) }
                        modelsAdapter = activity?.let { AlarmListAdapter(models, it) }!!

                        binding.alarmListRecycler.layoutManager = LinearLayoutManager(activity)
                        binding.alarmListRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetMedicalAlarmListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }


}