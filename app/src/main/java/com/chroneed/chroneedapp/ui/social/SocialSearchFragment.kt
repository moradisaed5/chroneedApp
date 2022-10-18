package com.chroneed.chroneedapp.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.*
import com.chroneed.chroneedapp.databinding.FragmentSocialSearchBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.adapter.SocialPostAdapter
import com.chroneed.chroneedapp.ui.social.adapter.SocialSearchUserAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialSearchFragment : Fragment() {
    private var _binding: FragmentSocialSearchBinding? = null
    private val binding get() = _binding!!

    private var userModels: MutableList<SocialSearchUserModel> = mutableListOf()
    private lateinit var userModelsAdapter: SocialSearchUserAdapter

    private var postModels: MutableList<SocialPostModel> = mutableListOf()
    private lateinit var postModelsAdapter: SocialPostAdapter
    private lateinit var searchText: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            searchText = bundle.getString("searchText", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (searchText.isNotEmpty())
            binding.txtSearch.setText(searchText)
        searchUsers()
        binding.btnSearchByPosts.setOnClickListener() {
            searchPosts()
        }
        binding.btnSearchByUsers.setOnClickListener() {
            searchUsers()
        }
    }

    private fun initPostRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        postModelsAdapter = SocialPostAdapter(postModels, thisActivity)

        binding.socialSearchResultRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialSearchResultRecycler.adapter = postModelsAdapter
        myProgress.closeDialog()
    }

    private fun initPostModels(baseModel: List<SocialPostModel>) {
        postModels.clear()
        postModels.addAll(baseModel)
        initPostRecycle()
    }

    private fun searchPosts() {
        val searchText = binding.txtSearch.text.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getSocialPostList(searchText, 0, 100)
        apiInterface.enqueue(object : Callback<SocialSearchPostResponse> {
            override fun onResponse(call: Call<SocialSearchPostResponse>, response: Response<SocialSearchPostResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { initPostModels(it) }
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialSearchPostResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun initUserRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        userModelsAdapter = SocialSearchUserAdapter(userModels, thisActivity)

        binding.socialSearchResultRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialSearchResultRecycler.adapter = userModelsAdapter
        myProgress.closeDialog()
    }


    private fun initUserModels(model: List<SocialSearchUserModel>) {
        userModels.clear()
        userModels.addAll(model)
        initUserRecycle()
    }

    private fun searchUsers() {
        val searchText = binding.txtSearch.text.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getSearchSocialUserList(searchText, 0, 100)
        apiInterface.enqueue(object : Callback<SocialSearchUserResponse> {
            override fun onResponse(call: Call<SocialSearchUserResponse>, response: Response<SocialSearchUserResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { initUserModels(it) }
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialSearchUserResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
}