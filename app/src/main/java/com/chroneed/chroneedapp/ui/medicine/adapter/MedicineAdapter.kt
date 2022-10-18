package com.chroneed.chroneedapp.ui.medicine.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.MedicineModels

class MedicineAdapter constructor(private val items: MutableList<MedicineModels>) :
    RecyclerView.Adapter<MedicineAdapter.viewHolder>() {
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

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnSetGoal = holder.itemView.findViewById<ImageView>(R.id.card_item_medicine_set_goal)
        btnSetGoal.visibility = View.GONE
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val name = itemView.findViewById<TextView>(R.id.card_item_medicine_name)
        private val administration = itemView.findViewById<TextView>(R.id.card_item_medicine_administration)
        private val administrationLabel = itemView.findViewById<TextView>(R.id.card_item_medicine_name_administration_label)
        private val qty = itemView.findViewById<TextView>(R.id.card_item_medicine_qty)
        private val qtyLabel = itemView.findViewById<TextView>(R.id.card_item_medicine_name_qty_label)

        @SuppressLint("SetTextI18n")
        fun bind(item: MedicineModels) {
            name.text = item.name
            if (item.administration.isNullOrEmpty()) {
                administration.text = item.administration
            } else {
                administration.visibility = View.GONE
                administrationLabel.visibility = View.GONE
            }
            if (item.qty.isNullOrEmpty()) {
                qty.text = item.qty
            }
        }
    }
}