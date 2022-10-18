package com.chroneed.chroneedapp.ui.social

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialCommentModel
import com.chroneed.chroneedapp.data.social.SocialPostModel
import com.chroneed.chroneedapp.databinding.FragmentSocialCommentsBinding
import com.chroneed.chroneedapp.databinding.FragmentSocialHomeBinding
import com.chroneed.chroneedapp.ui.social.adapter.SocialCommentsAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.google.android.material.textfield.TextInputEditText

class SocialCommentsFragment : Fragment() {
    private var _binding: FragmentSocialCommentsBinding? = null
    private val binding get() = _binding!!

    private var models: MutableList<SocialCommentModel> = mutableListOf()
    private lateinit var modelsAdapter: SocialCommentsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initModels()
        initRecycle()
    }

    private fun initRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        modelsAdapter = SocialCommentsAdapter(models, thisActivity)

        binding.socialPostListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialPostListRecycler.adapter = modelsAdapter
        myProgress.closeDialog()
    }

    private fun initModels() {
//        models.add(
//           SocialCommentModel("123213",
//           "Saeid Moradi",
//           "29038402934",
//           "this is my comments for now...")
//        )
//        models.add(
//            SocialCommentModel("123213",
//                "Saeid Moradi",
//                "29038402934",
//                "this is my comments for now...")
//        )
//        models.add(
//            SocialCommentModel("123213",
//                "Saeid Moradi",
//                "29038402934",
//                "this is my comments for now...")
//        )
//        models.add(
//            SocialCommentModel("123213",
//                "Saeid Moradi",
//                "29038402934",
//                "this is my comments for now...")
//        )
//        models.add(
//            SocialCommentModel("123213",
//                "Saeid Moradi",
//                "29038402934",
//                "this is my comments for now...")
//        )
//        models.add(
//            SocialCommentModel("123213",
//                "Saeid Moradi",
//                "29038402934",
//                "this is my comments for now...")
//        )
    }



}