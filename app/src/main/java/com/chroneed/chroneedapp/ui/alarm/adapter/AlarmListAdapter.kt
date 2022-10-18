package com.chroneed.chroneedapp.ui.alarm.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.DeleteMedicalAlarmResponse
import com.chroneed.chroneedapp.data.medical.DeleteVoiceResponseDto
import com.chroneed.chroneedapp.data.medical.MedicalAlarmModel
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.alarm.SetAlarmFragment
import com.chroneed.chroneedapp.ui.command.adapter.CommandRecordAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmListAdapter constructor(private val items: MutableList<MedicalAlarmModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<AlarmListAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_medical_alarm, parent, false)
        return viewHolder(inflater)
    }
    private fun deleteAlarm(holder: viewHolder, position: Int){
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).deleteMedicationAlarm(id)
        apiInterface.enqueue(object : Callback<DeleteMedicalAlarmResponse> {
            override fun onResponse(call: Call<DeleteMedicalAlarmResponse>, response: Response<DeleteMedicalAlarmResponse>) {
                myProgress.closeDialog()
                if(response.code()==200){
                    if(response.body()!!.isSuccess){
                        items.remove(items[position])
                        notifyDataSetChanged()
                    }
                }

            }
            override fun onFailure(call: Call<DeleteMedicalAlarmResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnEdit = holder.itemView.findViewById<ImageView>(R.id.card_item_alarm_edit)
        btnEdit.setOnClickListener() {
            val fragment = SetAlarmFragment()
            val arguments = Bundle()
            arguments.putString("id", items[position].id)
            fragment.arguments = arguments
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.main_fragment_view_layout, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_item_alarm_delete)
        btnDelete.setOnClickListener{
            deleteAlarm(holder,position)
        }
        holder.bind(items[position])
    }
    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName = itemView.findViewById<TextView>(R.id.card_item_alarm_title)
        private val txtLabel = itemView.findViewById<TextView>(R.id.card_item_alarm_desc)
        private val txtWeeks = itemView.findViewById<TextView>(R.id.card_item_alarm_days_names)

        @SuppressLint("SetTextI18n")
        fun bind(item: MedicalAlarmModel) {
            var weeks = ""
            if (item.dayOfWeeks!!.contains(0))
                if (weeks.isNullOrEmpty())
                    weeks += "Su"
                else
                    weeks += ",Su"
            if (item.dayOfWeeks!!.contains(1))
                if (weeks.isNullOrEmpty())
                    weeks += "Mo"
                else
                    weeks += ",Mo"
            if (item.dayOfWeeks!!.contains(2))
                if (weeks.isNullOrEmpty())
                    weeks += "Tu"
                else
                    weeks += ",Tu"
            if (item.dayOfWeeks!!.contains(3))
                if (weeks.isNullOrEmpty())
                    weeks += "We"
                else
                    weeks += ",We"
            if (item.dayOfWeeks!!.contains(4))
                if (weeks.isNullOrEmpty())
                    weeks += "Th"
                else
                    weeks += ",Th"
            if (item.dayOfWeeks!!.contains(5))
                if (weeks.isNullOrEmpty())
                    weeks += "Fr"
                else
                    weeks += ",Fr"
            if (item.dayOfWeeks!!.contains(6))
                if (weeks.isNullOrEmpty())
                    weeks += "Sa"
                else
                    weeks += ",Sa"
            txtName.text = item.title
            txtLabel.text = item.description
            txtWeeks.text =weeks
        }
    }

}