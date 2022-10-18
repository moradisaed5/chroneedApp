package com.chroneed.chroneedapp.ui.social.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialCommentModel

class SocialCommentsAdapter constructor(private val items: MutableList<SocialCommentModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<SocialCommentsAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_social_comment, parent, false)
        return viewHolder(inflater)
    }



    private fun likeItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {

    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val userImage = holder.itemView.findViewById<ImageView>(R.id.card_item_social_comment_image)

        val imageName = items[position].profileImage
        if (imageName != null) {
            if (imageName!!.isNotEmpty()) {
                Glide.with(context).load("http://chroneedapi.com/identity.api/files/$imageName")
                    .placeholder(R.drawable.ic_user)
                    .into(userImage)
            }
        }

        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userFullName = itemView.findViewById<TextView>(R.id.card_item_social_comment_full_name)
        private val userComment = itemView.findViewById<TextView>(R.id.card_item_social_comment_comment)



        @SuppressLint("SetTextI18n")
        fun bind(item: SocialCommentModel) {
            userFullName.text=item.fullName
            userComment.text=item.comment
        }
    }

}