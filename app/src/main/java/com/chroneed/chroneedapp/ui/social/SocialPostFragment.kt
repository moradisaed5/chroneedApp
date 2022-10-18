package com.chroneed.chroneedapp.ui.social

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.social.*
import com.chroneed.chroneedapp.databinding.FragmentSocialPostBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.adapter.SocialCommentsAdapter
import com.chroneed.chroneedapp.ui.social.adapter.SocialPostAdapter
import com.chroneed.chroneedapp.utilities.CodeManager
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SocialPostFragment : Fragment() {
    private var _binding: FragmentSocialPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: String
    private val REQUEST_CODE_SPEECH_INPUT = 10064
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
        _binding = FragmentSocialPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lottieListening.visibility=View.GONE
        loadPost()
        loadComments()
        binding.postRecordVoice.setOnClickListener {
            recordStart()
        }
        binding.postCommentSend.setOnClickListener{
            sendComment()
        }
    }


    private fun loadComments() {
        var models: MutableList<SocialCommentModel> = mutableListOf()
        lateinit var modelsAdapter: SocialCommentsAdapter
        models.clear()
        val commentsRecycler = binding.socialPostCommentsListRecycler
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).getSocialPostCommentList(id, 0, 100)
        apiInterface.enqueue(object : Callback<SocialCommentListResponse> {
            override fun onResponse(call: Call<SocialCommentListResponse>, response: Response<SocialCommentListResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data!!.count() > 0) {
                        response.body()!!.data?.let { models.addAll(it) }
                        binding.socialCommentCount.text = "${models.count()} commented"
                        modelsAdapter = SocialCommentsAdapter(models, activity!!)
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

    private fun loadPost() {
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).visitSocialPostById(id)
        apiInterface.enqueue(object : Callback<SocialPostResponse> {
            override fun onResponse(call: Call<SocialPostResponse>, response: Response<SocialPostResponse>) {
                if (response.body()!!.isSuccess == true) {
                    if (response.body()!!.data != null) {
                        val _model = response.body()!!.data!!
                        binding.socialTitle.setText(_model.title)
                        binding.socialDescription.setText(_model.description)
                        if (_model.isLiked == true) {
                            binding.socialLike.visibility = View.GONE
                            binding.socialLikeFilled.visibility = View.VISIBLE
                        } else {
                            binding.socialLike.visibility = View.GONE
                            binding.socialLikeFilled.visibility = View.VISIBLE
                        }
                        //binding.socialCommentCount.text = "${_model.commentCount} commented"
                        binding.socialLikeCount.setText("liked by ${_model.likeCount} people.")
                        val image = binding.socialImage
                        if (_model.imageName != null) {
                            if (_model.imageName!!.isNotEmpty()) {
                                Glide.with(binding.root.context).load("http://chroneedapi.com/identity.api/files/${_model.imageName}")
                                    .into(image)
                            }
                        }
                    }
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialPostResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun sendComment() {
        //val commentModel = NewSocialCommentModel(id, commentText.text.toString())
        val commentsRecycler = binding.socialPostCommentsListRecycler
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).addSocialPostComment(NewSocialCommentModel(id,binding.socialCommentText.text.toString()))
        apiInterface.enqueue(object : Callback<SocialCommentResponse> {
            override fun onResponse(call: Call<SocialCommentResponse>, response: Response<SocialCommentResponse>) {
                if (response.body()!!.isSuccess == true) {
                    binding.socialCommentText.setText("")
                    loadComments()
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<SocialCommentResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun recordStart() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

        // on below line we are passing language model
        // and model free form in our intent
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )

        // on below line we are passing our
        // language as a default language.
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )

        // on below line we are specifying a prompt
        // message as speak to text on below line.
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text")

        // on below line we are specifying a try catch block.
        // in this block we are calling a start activity
        // for result method and passing our result code.
        //binding.lottieListening.visibility = View.VISIBLE
        startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
//                Toast.makeText(this, Objects.requireNonNull(res)[0], Toast.LENGTH_LONG).show()
                try {
                    binding.lottieListening.visibility=View.VISIBLE
                    val word = Objects.requireNonNull(res)[0]
                    binding.socialCommentText.setText(word)
                } catch (e: Exception) {
                    binding.lottieListening.visibility=View.GONE
                }
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    binding.lottieListening.visibility=View.GONE
                }, 1500)
                var prevText = binding.socialCommentText.text.toString()

                //recognizeMessage(word)
                //startCall()
                //startSMS()

            }
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                //binding.lottieListening.visibility = View.GONE
            }, 1000)
        }
        if (requestCode == CodeManager.REQUEST_SPEECH_GLOBAL_CODE) {
            val res: ArrayList<String> =
                data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
            // getWord(Objects.requireNonNull(res)[0])
        }
    }
}