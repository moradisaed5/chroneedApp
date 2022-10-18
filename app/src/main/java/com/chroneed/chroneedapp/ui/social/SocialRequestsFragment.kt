package com.chroneed.chroneedapp.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialRequestListResponse
import com.chroneed.chroneedapp.data.social.SocialRequestModel
import com.chroneed.chroneedapp.databinding.FragmentSocialRequestsBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.adapter.SocialRequestsAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialRequestsFragment : Fragment() {

    private var _binding: FragmentSocialRequestsBinding? = null
    private val binding get() = _binding!!

    private var models: MutableList<SocialRequestModel> = mutableListOf()
    private lateinit var modelsAdapter: SocialRequestsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getRequests()
    }

    private fun initRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        modelsAdapter = SocialRequestsAdapter(models, thisActivity)

        binding.socialRequestListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialRequestListRecycler.adapter = modelsAdapter
        myProgress.closeDialog()
    }

    private fun initModels(inputModel:List<SocialRequestModel>) {
        models.addAll(inputModel)
        initRecycle()
    }
    private fun getRequests(){
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getMyRequestList(0, 100)
        apiInterface.enqueue(object : Callback<SocialRequestListResponse> {
            override fun onResponse(call: Call<SocialRequestListResponse>, response: Response<SocialRequestListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { initModels(it) }
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialRequestListResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
}