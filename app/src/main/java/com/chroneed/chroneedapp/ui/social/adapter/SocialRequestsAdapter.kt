package com.chroneed.chroneedapp.ui.social.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialRequestModel
import com.chroneed.chroneedapp.data.social.SocialRequestResponse
import com.chroneed.chroneedapp.data.social.SocialSearchUserResponse
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialRequestsAdapter constructor(private val items: MutableList<SocialRequestModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<SocialRequestsAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_social_request, parent, false)
        return viewHolder(inflater)
    }


    private fun acceptItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        val id=items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).acceptRequestUser(id)
        apiInterface.enqueue(object : Callback<SocialRequestResponse> {
            override fun onResponse(call: Call<SocialRequestResponse>, response: Response<SocialRequestResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!=null) {
                        items.remove(items[position])
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialRequestResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun rejectItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        val id=items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).rejectRequestUser(id)
        apiInterface.enqueue(object : Callback<SocialRequestResponse> {
            override fun onResponse(call: Call<SocialRequestResponse>, response: Response<SocialRequestResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!=null) {
                        items.remove(items[position])
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialRequestResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnAccept = holder.itemView.findViewById<ImageView>(R.id.card_item_social_request_accept)
        val btnReject = holder.itemView.findViewById<ImageView>(R.id.card_item_social_request_reject)
        val imgAvatar = holder.itemView.findViewById<ImageView>(R.id.request_image)
        val profileImage = items[position].profilaImageName
        if (profileImage != null) {
            if (profileImage!!.isNotEmpty()) {
                Glide.with(context).load("http://chroneed.saeid-moradi.ir/files/$profileImage")
                    .placeholder(R.drawable.ic_user)
                    .into(imgAvatar)
            }
        }

        btnAccept.setOnClickListener() {
            acceptItem(holder, position)
        }
        btnReject.setOnClickListener() {
            rejectItem(holder, position)
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtDescription = itemView.findViewById<TextView>(R.id.card_item_social_request_description)
        private val txtFullName = itemView.findViewById<TextView>(R.id.card_item_social_request_full_name)
        private val imgAvatar = itemView.findViewById<ImageView>(R.id.request_image)

        @SuppressLint("SetTextI18n")
        fun bind(item: SocialRequestModel) {
            txtDescription.text=item.requestDate
            txtFullName.text=item.fullName
        }
    }

}