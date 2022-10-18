package com.chroneed.chroneedapp.ui.social.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.*
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.prescription.PrescriptionEditFragment
import com.chroneed.chroneedapp.ui.social.SocialCommentsFragment
import com.chroneed.chroneedapp.ui.social.SocialPostFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SocialPostAdapter constructor(private val items: MutableList<SocialPostModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<SocialPostAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_social_post, parent, false)
        return viewHolder(inflater)
    }

    private var models: MutableList<SocialCommentModel> = mutableListOf()
    private lateinit var modelsAdapter: SocialCommentsAdapter

    private fun loadComments(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        models.clear()
        val id = items[position].id
        val commentsRecycler = holder.itemView.findViewById<RecyclerView>(R.id.social_post_comments_list_recycler)
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getSocialPostCommentList(id, 0, 100)
        apiInterface.enqueue(object : Callback<SocialCommentListResponse> {
            override fun onResponse(call: Call<SocialCommentListResponse>, response: Response<SocialCommentListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { models.addAll(it) }
                        modelsAdapter = SocialCommentsAdapter(models, activity)
                        commentsRecycler.layoutManager = LinearLayoutManager(activity)
                        commentsRecycler.adapter = modelsAdapter
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialCommentListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun toggleShowComment(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        items[position].readyToComment = items[position].readyToComment != true
        if (models == null)
            loadComments(holder, position)
        if (models.count() == 0)
            loadComments(holder, position)
        notifyDataSetChanged()
    }

    private fun likeItem(holder: viewHolder, @SuppressLint("RecyclerView") position: Int) {
        val id = items[position].id
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var apiInterface = ApiInterface.create(tokenba).toggleLikeSocialPost(id)
        apiInterface.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (items[position].isLiked == true) {
                    items[position].isLiked = false
                    items[position].likeCount = items[position].likeCount?.minus(1)
                } else {
                    items[position].isLiked = true
                    items[position].likeCount = items[position].likeCount?.plus(1)
                }
                notifyDataSetChanged()
            }
            override fun onFailure(call: Call<Void>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
            }
        })



    }

    private fun showPost(holder: viewHolder, @SuppressLint("RecyclerView") position: Int){
        val fragment = SocialPostFragment()
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

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val image = holder.itemView.findViewById<ImageView>(R.id.card_item_social_image)
        val imageName = items[position].imageName
        if (imageName != null) {
            if (imageName!!.isNotEmpty()) {
                Glide.with(context).load("http://chroneedapi.com/identity.api/files/$imageName")
                    .into(image)
            }
        }
        val imageLiked = holder.itemView.findViewById<ImageView>(R.id.card_item_social_like_filled)
        val imageNotLiked = holder.itemView.findViewById<ImageView>(R.id.card_item_social_like)
        val imageShowComment = holder.itemView.findViewById<ImageView>(R.id.card_item_social_comment)
        val commentCount = holder.itemView.findViewById<TextView>(R.id.card_item_social_comment_count)
        imageShowComment.setOnClickListener() {
            showPost(holder, position)
        }
        imageLiked.setOnClickListener() {
            likeItem(holder, position)
        }
        imageNotLiked.setOnClickListener() {
            likeItem(holder, position)
        }
        commentCount.setOnClickListener() {
            showPost(holder, position)
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.card_item_social_title)


        private val imageLiked = itemView.findViewById<ImageView>(R.id.card_item_social_like_filled)
        private val imageNotLiked = itemView.findViewById<ImageView>(R.id.card_item_social_like)
        private val imageShowComment = itemView.findViewById<ImageView>(R.id.card_item_social_comment)
        private val description = itemView.findViewById<TextView>(R.id.card_item_social_description)
        private val likeCount = itemView.findViewById<TextView>(R.id.card_item_social_like_count)
        private val commentCount = itemView.findViewById<TextView>(R.id.card_item_social_comment_count)
        //card_item_social_comment_count

        @SuppressLint("SetTextI18n")
        fun bind(item: SocialPostModel) {

            title.text = item.title
            description.text = item.description
            likeCount.text = "liked by ${item.likeCount} people."
            commentCount.text = "${item.commentCount.toString()} commented"
            if (item.isLiked == true) {
                imageNotLiked.visibility = View.GONE
                imageLiked.visibility = View.VISIBLE
            } else {
                imageLiked.visibility = View.GONE
                imageNotLiked.visibility = View.VISIBLE
            }

        }
    }

}