package com.chroneed.chroneedapp.ui.medicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.MedicalMedicineModel
import com.chroneed.chroneedapp.data.medical.SetGoalRequest
import com.chroneed.chroneedapp.data.medical.SetGoalResponse
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.habit.HabitListFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserMedicineAdapter constructor(private val items: MutableList<MedicalMedicineModel>) :
    RecyclerView.Adapter<UserMedicineAdapter.viewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {

        context = parent.context
        val inflater = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item_medicine, parent, false)
        return viewHolder(inflater)
    }

    private fun setGoal(holder: viewHolder, @SuppressLint("RecyclerView") position: Int){
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var model= SetGoalRequest(id,0)
        try {
            var apiInterface = ApiInterface.create(tokenba).setGoal(model)
            apiInterface.enqueue(object : Callback<SetGoalResponse> {
                override fun onResponse(call: Call<SetGoalResponse>, response: Response<SetGoalResponse>) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG).show();
                    }
                }

                override fun onFailure(call: Call<SetGoalResponse>, t: Throwable?) {
                    Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                    myProgress.closeDialog()
                }
            })
        } catch (ex: Exception) {
            myProgress.closeDialog()
            Toast.makeText(context, ex.message, Toast.LENGTH_LONG).show();
        }
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnSetGoal = holder.itemView.findViewById<ImageView>(R.id.card_item_medicine_set_goal)
        btnSetGoal.setOnClickListener {
            setGoal(holder, position)
        }
        holder.bind(items[position])


    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.card_item_medicine_name)
        private val administration = itemView.findViewById<TextView>(R.id.card_item_medicine_administration)
        private val administrationLabel = itemView.findViewById<TextView>(R.id.card_item_medicine_name_administration_label)
        private val qty = itemView.findViewById<TextView>(R.id.card_item_medicine_qty)
        private val qtyLabel = itemView.findViewById<TextView>(R.id.card_item_medicine_name_qty_label)
        private val btnSetGoal = itemView.findViewById<ImageView>(R.id.card_item_medicine_set_goal)

        @SuppressLint("SetTextI18n")
        fun bind(item: MedicalMedicineModel) {
            name.text = item.name
            if (item.administration.isNullOrEmpty()) {
                administration.text = item.administration
            } else {
                administration.visibility= View.GONE
                administrationLabel.visibility= View.GONE
            }
            if (item.qty.isNullOrEmpty()) {
                qty.text = item.qty
            }
        }
    }
}