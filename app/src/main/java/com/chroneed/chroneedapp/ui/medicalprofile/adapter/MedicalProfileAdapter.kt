package com.chroneed.chroneedapp.ui.medicalprofile.adapter

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
import com.chroneed.chroneedapp.data.medical.DeleteCarePlanResponse
import com.chroneed.chroneedapp.data.medical.DeleteMedicalRecordResponse
import com.chroneed.chroneedapp.data.medical.MedicalRecordModel
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.medicalprofile.MedicalProfileEditFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicalProfileAdapter constructor(private val items: MutableList<MedicalRecordModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<MedicalProfileAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_medical_record, parent, false)
        return viewHolder(inflater)
    }

    private fun deleteItem(holder: viewHolder, position: Int) {
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).deleteMedicalRecord(id)
        apiInterface.enqueue(object : Callback<DeleteMedicalRecordResponse> {
            override fun onResponse(call: Call<DeleteMedicalRecordResponse>, response: Response<DeleteMedicalRecordResponse>) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess) {
                        items.remove(items[position])
                        notifyDataSetChanged()
                    }
                }

            }

            override fun onFailure(call: Call<DeleteMedicalRecordResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnEdit = holder.itemView.findViewById<ImageView>(R.id.medical_record_edit)
        btnEdit.setOnClickListener() {
            val fragment = MedicalProfileEditFragment()
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

        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.medical_record_delete)
        btnDelete.setOnClickListener {
            deleteItem(holder, position)
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.medical_record_title)
        val startDate = itemView.findViewById<TextView>(R.id.medical_record_start)
        val endDate = itemView.findViewById<TextView>(R.id.medical_record_end)
        val description = itemView.findViewById<TextView>(R.id.medical_record_desc)

        @SuppressLint("SetTextI18n")
        fun bind(item: MedicalRecordModel) {
            title.setText(item.subject)
            if (!item.startDate.isNullOrEmpty())
                startDate.setText("Start : " + item.startDate.split('T')[0])
            else
                startDate.visibility = View.GONE
            if (!item.endDate.isNullOrEmpty())
                endDate.setText("End : " + item.endDate!!.split('T')[0])
            else
                endDate.visibility = View.GONE
            description.setText("Type:" + item.strMedicalRecordType + "\nStage :" + item.strMedicalRecordStage + "\n" + item.description)
        }
    }

}