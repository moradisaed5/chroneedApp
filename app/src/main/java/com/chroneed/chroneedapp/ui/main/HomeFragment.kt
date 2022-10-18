package com.chroneed.chroneedapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.databinding.FragmentHomeBinding
import com.chroneed.chroneedapp.ui.alarm.ShowAlarmsFragment
import com.chroneed.chroneedapp.ui.careplan.CareplanListFragment
import com.chroneed.chroneedapp.ui.command.CommandListFragment
import com.chroneed.chroneedapp.ui.goal.UserShowGoalsFragment
import com.chroneed.chroneedapp.ui.habit.HabitListFragment
import com.chroneed.chroneedapp.ui.medicalprofile.MedicalProfileListFragment
import com.chroneed.chroneedapp.ui.medicine.UserMedicinesListFragment
import com.chroneed.chroneedapp.ui.prescription.PrescriptionListFragment
import com.chroneed.chroneedapp.utilities.AlarmUtil
import com.chroneed.chroneedapp.utilities.MySharedPreferences


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvents()
//        AlarmUtil().setAutoAlarm("my title","my desc",1, this@MainActivity)
//        this.activity?.let { AlarmUtil().setAutoAlarm("my title","my desc",1, it) } //Todo : it is for temp and must be delete
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initEvents() {
        val prescriptionListFragment = PrescriptionListFragment()
//        val medicalListFragment = MedicalListFragment()
        val habitListFragment = HabitListFragment()
        val commandListFragment = CommandListFragment()
//        val practitionerListFragment = PractitionerListFragment()
        val medicalAlarmListFragment = ShowAlarmsFragment()
        val carePlanFragment = CareplanListFragment()
        val userMedicinesListFragment = UserMedicinesListFragment()
        val userShowGoalsFragment = UserShowGoalsFragment()
        val medicalProfileList = MedicalProfileListFragment()

        binding.fragmentHomeTopmenuPrescriptions.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, prescriptionListFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHomeTopmenuHabits.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, habitListFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHomeTopmenuCommand.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, commandListFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHomeTopmenuCarePlan.setOnClickListener{
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, carePlanFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHomeTopmenuUserMedicine.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, userMedicinesListFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.fragmentHomeTopmenuUserGoal.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, userShowGoalsFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        binding.fragmentHomeTopmenuMedicalAlarms.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, medicalAlarmListFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        binding.fragmentHomeTopmenuMedicalInformation.setOnClickListener() {
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, medicalProfileList)
                ?.addToBackStack(null)
                ?.commit()
        }
    }


}