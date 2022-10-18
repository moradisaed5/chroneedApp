package com.chroneed.chroneedapp.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialPostListResponse
import com.chroneed.chroneedapp.data.social.SocialPostModel
import com.chroneed.chroneedapp.databinding.FragmentSocialHomeBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.adapter.SocialPostAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialHomeFragment : Fragment() {
    private var _binding: FragmentSocialHomeBinding? = null
    private val binding get() = _binding!!

    private var models: MutableList<SocialPostModel> = mutableListOf()
    private lateinit var modelsAdapter: SocialPostAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        models.clear()
        requestPostList()
        binding.txtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                goToSearch()
                true
            } else {
                false
            }
        }
        binding.btnSearch.setOnClickListener() {
            goToSearch()
        }
    }

    private fun goToSearch() {
        val fragment = SocialSearchFragment()
        val arguments = Bundle()
        val searchText = binding.txtSearch.text.toString()
        arguments.putString("searchText", searchText)
        fragment.arguments = arguments
        activity
            ?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.social_layout_fragment, fragment)
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun reloadRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        modelsAdapter = SocialPostAdapter(models, thisActivity)

        binding.socialPostListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialPostListRecycler.adapter = modelsAdapter
        myProgress.closeDialog()
    }

    private fun initModels(inputModels : List<SocialPostModel>) {
        models.addAll(inputModels)
        reloadRecycle()
    }
    private fun  requestPostList(){
        val tokenba ="Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress =MyProgressDialog(binding.root.context,R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getSocialPostList(0, 100)
        apiInterface.enqueue(object : Callback<SocialPostListResponse> {
            override fun onResponse(call: Call<SocialPostListResponse>, response: Response<SocialPostListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { initModels(it) }
                    }
                }
                myProgress.closeDialog()
            }
            override fun onFailure(call: Call<SocialPostListResponse>,t: Throwable?) {
                Toast.makeText(context,t?.message,Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
}


