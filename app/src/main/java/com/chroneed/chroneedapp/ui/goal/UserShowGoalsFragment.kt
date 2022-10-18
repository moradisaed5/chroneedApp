package com.chroneed.chroneedapp.ui.goal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.GetGoalStaticsResponse
import com.chroneed.chroneedapp.data.medical.GoalStaticsModel
import com.chroneed.chroneedapp.data.medical.NewCommandModel
import com.chroneed.chroneedapp.databinding.FragmentUserShowGoalsBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.command.adapter.CommandListAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserShowGoalsFragment : Fragment() {
    private var _binding: FragmentUserShowGoalsBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserShowGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvent()
        loadData()
    }

    private fun loadData() {
        var models: MutableList<NewCommandModel> = mutableListOf()
        lateinit var modelsAdapter: CommandListAdapter
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(token).getUserMedicalGoalStatics()
        apiInterface.enqueue(object : Callback<GetGoalStaticsResponse> {
            override fun onResponse(
                call: Call<GetGoalStaticsResponse>,
                response: Response<GetGoalStaticsResponse>
            ) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        initView(response.body()!!.data!!)
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<GetGoalStaticsResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }

    private fun initView(model: List<GoalStaticsModel>) {
        var firstItem = model[0]
        var secondItem = model[1]
        //Medicine=0,Habit=1,Goal=2
        if (firstItem.goalType == 0)
            setMedicineGoalStatics(firstItem)
        if (firstItem.goalType == 1)
            setHabitGoalStatics(firstItem)

        if (secondItem.goalType == 0)
            setMedicineGoalStatics(secondItem)
        if (secondItem.goalType == 1)
            setHabitGoalStatics(secondItem)
    }

    private fun setHabitGoalStatics(model: GoalStaticsModel) {
        binding.txtHabitToday.text = model.todayCount.toString()
        binding.txtHabitWeek.text = model.weekCount.toString()
        binding.txtHabitMonth.text = model.monthCount.toString()
        binding.txtHabitYear.text = model.yearCount.toString()
        binding.txtHabitAll.text = model.allCount.toString()
    }

    private fun setMedicineGoalStatics(model: GoalStaticsModel) {
        binding.txtMedicineToday.text = model.todayCount.toString()
        binding.txtMedicineWeek.text = model.weekCount.toString()
        binding.txtMedicineMonth.text = model.monthCount.toString()
        binding.txtMedicineYear.text = model.yearCount.toString()
        binding.txtMedicineAll.text = model.allCount.toString()
    }

    private fun initEvent() {

    }
}