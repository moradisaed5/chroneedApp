package com.chroneed.chroneedapp.ui.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.SocialPostListResponse
import com.chroneed.chroneedapp.data.social.SocialPostModel
import com.chroneed.chroneedapp.data.social.SocialProfileModel
import com.chroneed.chroneedapp.data.social.SocialProfileResponse
import com.chroneed.chroneedapp.databinding.FragmentSocialViewProfileBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.adapter.SocialPostAdapter
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SocialViewProfileFragment : Fragment() {

    private var _binding: FragmentSocialViewProfileBinding? = null
    private val binding get() = _binding!!

    private var models: MutableList<SocialPostModel> = mutableListOf()
    private lateinit var modelsAdapter: SocialPostAdapter

    private lateinit var id: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = this.arguments
        if (bundle != null) {
            id = bundle.getString("id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSocialViewProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestUserProfile()
        requestPostList()
        binding.btnFollow.setOnClickListener(){
            sendRequestFollow()
        }
    }

    private fun initRecycle() {
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        val thisActivity = activity?.let { it }!!
        modelsAdapter = SocialPostAdapter(models, thisActivity)

        binding.socialPostListRecycler.layoutManager = LinearLayoutManager(activity)
        binding.socialPostListRecycler.adapter = modelsAdapter
        myProgress.closeDialog()
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
    private fun initProfile(inputModel: SocialProfileModel){
        if(inputModel!=null){
            binding.fragmentSocialProfilePostCount.setText("${inputModel.postCount}\nPosts")
            binding.fragmentSocialProfileFollowingCount.setText("${inputModel.fllowingCount}\nFollowings")
            binding.fragmentSocialProfileFollowerCount.setText("${inputModel.fllowerCount}\nFollowers")
            binding.txtBio.setText(inputModel.bio)
            binding.fragmentSocialProfileUserFullName.setText(inputModel.userFullName)
            binding.txtIsPrivate.visibility=View.GONE
            binding.btnFollow.icon=null

            when(inputModel.followStatus){
                0->{//unknown
                    binding.btnFollow.setText("Follow")
                }
                1->{//followed
                    binding.btnFollow.setText("Following")
                    binding.btnFollow.setIconResource(R.drawable.ic_accept)
                }
                2->{//public
                    binding.btnFollow.setText("Follow")
                }
                3->{//private
                    binding.btnFollow.setText("Follow")
                    binding.txtIsPrivate.visibility=View.VISIBLE
                }
                4->{//own profile
                    binding.btnFollow.setText("own")
                }
                5->{//requested
                    binding.btnFollow.setText("Requested!")
                    binding.txtIsPrivate.visibility=View.VISIBLE
                }
            }
            val image = binding.profileImage
            val imageName = inputModel.imageProfileName
            if (imageName != null) {
                if (imageName!!.isNotEmpty()) {
                    Glide.with(binding.root.context).load("http://chroneed.saeid-moradi.ir/files/$imageName")
                        .placeholder(R.drawable.ic_user)
                        .into(image)
                }
            }
        }
    }

    private fun  requestPostList(){
        val tokenba ="Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress =MyProgressDialog(binding.root.context,R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).visitSocialPostList(id,0, 100)
        apiInterface.enqueue(object : Callback<SocialPostListResponse> {
            override fun onResponse(call: Call<SocialPostListResponse>, response: Response<SocialPostListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { initModels(it) }
                    }
                }
                myProgress.closeDialog()
            }
            override fun onFailure(call: Call<SocialPostListResponse>, t: Throwable?) {
                Toast.makeText(context,t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
    private fun requestUserProfile(){
        val tokenba ="Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress =MyProgressDialog(binding.root.context,R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).visitUserProfile(id)
        apiInterface.enqueue(object : Callback<SocialProfileResponse> {
            override fun onResponse(call: Call<SocialProfileResponse>, response: Response<SocialProfileResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!=null) {
                        response.body()!!.data?.let { initProfile(it) }
                    }
                }
                myProgress.closeDialog()
            }
            override fun onFailure(call: Call<SocialProfileResponse>, t: Throwable?) {
                Toast.makeText(context,t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun  sendRequestFollow(){
        val tokenba ="Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress =MyProgressDialog(binding.root.context,R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).sendRequestUser(id)
        apiInterface.enqueue(object : Callback<SocialProfileResponse> {
            override fun onResponse(call: Call<SocialProfileResponse>, response: Response<SocialProfileResponse>) {
                if (response.body()!!.isSuccess == true) {
                    requestUserProfile()
                    requestPostList()
                }
                myProgress.closeDialog()
            }
            override fun onFailure(call: Call<SocialProfileResponse>, t: Throwable?) {
                Toast.makeText(context,t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
}