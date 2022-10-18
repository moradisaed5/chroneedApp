package com.chroneed.chroneedapp.ui.careplan.adapter

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
import com.chroneed.chroneedapp.data.medical.CarePlanModel
import com.chroneed.chroneedapp.data.medical.DeleteCarePlanResponse
import com.chroneed.chroneedapp.data.medical.DeleteMedicalAlarmResponse
import com.chroneed.chroneedapp.data.medical.MedicalAlarmModel
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.alarm.SetAlarmFragment
import com.chroneed.chroneedapp.ui.careplan.CareplanEditFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CarePlanAdapter constructor(private val items: MutableList<CarePlanModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<CarePlanAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_care_plan, parent, false)
        return viewHolder(inflater)
    }
    private fun deleteItem(holder: viewHolder, position: Int){
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).deleteUserCarePlan(id)
        apiInterface.enqueue(object : Callback<DeleteCarePlanResponse> {
            override fun onResponse(call: Call<DeleteCarePlanResponse>, response: Response<DeleteCarePlanResponse>) {
                myProgress.closeDialog()
                if(response.code()==200){
                    if(response.body()!!.isSuccess){
                        items.remove(items[position])
                        notifyDataSetChanged()
                    }
                }

            }
            override fun onFailure(call: Call<DeleteCarePlanResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnEdit = holder.itemView.findViewById<ImageView>(R.id.card_care_plan_edit)
        btnEdit.setOnClickListener() {
            val fragment = CareplanEditFragment()
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

        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_care_plan_delete)
        btnDelete.setOnClickListener{
            deleteItem(holder,position)
        }
        holder.bind(items[position])
    }
    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName = itemView.findViewById<TextView>(R.id.card_care_plan_doctor_name)
        private val txtDescription = itemView.findViewById<TextView>(R.id.card_care_plan_description)
        private val txtDate = itemView.findViewById<TextView>(R.id.card_care_plan_date_text)

        @SuppressLint("SetTextI18n")
        fun bind(item: CarePlanModel) {

            txtName.text = item.doctorName
            txtDescription.text = item.planDescription
            txtDate.text = item.planDate.split("T")[0]
        }
    }

}