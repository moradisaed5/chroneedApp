package com.chroneed.chroneedapp.ui.social.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialSearchUserModel
import com.chroneed.chroneedapp.ui.prescription.PrescriptionEditFragment
import com.chroneed.chroneedapp.ui.social.SocialViewProfileFragment


class SocialSearchUserAdapter constructor(private val items: MutableList<SocialSearchUserModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<SocialSearchUserAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_social_user, parent, false)
        return viewHolder(inflater)
    }


    private fun acceptItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

    }

    private fun rejectItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val imgAvatar = holder.itemView.findViewById<ImageView>(R.id.user_image)
        val item = holder.itemView.findViewById<ConstraintLayout>(R.id.card_item_social_user_item)


        val profileImage = items[position].profileImageName
        if (profileImage != null) {
            if (profileImage!!.isNotEmpty()) {
                Glide.with(context).load("http://chroneed.saeid-moradi.ir/files/$profileImage")
                    .placeholder(R.drawable.ic_user)
                    .into(imgAvatar)
            }
        }

        item.setOnClickListener(){
            val fragment = SocialViewProfileFragment()
            val arguments = Bundle()
            val id = items[position].id
            arguments.putString("id", id)
            fragment.arguments = arguments
            activity
                ?.supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.social_layout_fragment, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }
        val imageName=items[position].profileImageName
        if (imageName != null) {
            if (imageName!!.isNotEmpty()) {
                Glide.with(context).load("http://chroneed.saeid-moradi.ir/files/$imageName")
                    .placeholder(R.drawable.ic_user)
                    .into(imgAvatar)
            }
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtBio = itemView.findViewById<TextView>(R.id.card_item_social_user_bio)
        private val txtFullName = itemView.findViewById<TextView>(R.id.card_item_social_user_full_name)


        @SuppressLint("SetTextI18n")
        fun bind(item: SocialSearchUserModel) {
            txtBio.text=item.biography
            txtFullName.text="${item.firstName}  ${item.lastName}"

        }
    }
}