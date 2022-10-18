package com.chroneed.chroneedapp.ui.command

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.AddUserCommandResponse
import com.chroneed.chroneedapp.data.medical.NewCommandModel
import com.chroneed.chroneedapp.data.social.SocialPostResponse
import com.chroneed.chroneedapp.databinding.FragmentCommandNewBinding
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.social.SocialHomeFragment
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandNewFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var _binding: FragmentCommandNewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCommandNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initElements()
        initEvents()
    }

    private fun initEvents() {
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        binding.btnSave.setOnClickListener {
            if(binding.txtCommandName.text.toString().isNullOrEmpty()){
                Toast.makeText(binding.root.context, "please enter command name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(binding.txtCommandLabel.text.toString().isNullOrEmpty()){
                Toast.makeText(binding.root.context, "please enter command label", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveCommand()
        }
        binding.selectCommandType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                commandTypeChanged()
            }
        }
    }

    private fun commandTypeChanged() {
        val selectedItem = binding.selectCommandType.selectedItem.toString()
        if (selectedItem == "Call") {
            binding.txtMessageText.visibility = View.GONE
        } else if (selectedItem == "Message") {
            binding.txtMessageText.visibility = View.VISIBLE
        }
    }

    private fun initElements() {
        commandTypeChanged()
    }

    private fun saveCommand() {
        val selectedItem = binding.selectCommandType.selectedItem.toString()
        var commandModel: NewCommandModel
        when (selectedItem) {
            "Call" -> {
                if(binding.txtCommandPhone.text.toString().isNullOrEmpty()){
                    Toast.makeText(binding.root.context, "please enter phone number", Toast.LENGTH_SHORT).show()
                    return
                }
                val callCommand = NewCommandModel.CallCommand(binding.txtCommandPhone.text.toString())

                commandModel = NewCommandModel(
                    callCommand,
                    1,
                    binding.txtCommandName.text.toString(),
                    binding.txtCommandLabel.text.toString(),
                    null,
                    null,
                    null,
                    null
                )
            }
            "Message" -> {
                if(binding.txtCommandPhone.text.toString().isNullOrEmpty()){
                    Toast.makeText(binding.root.context, "please enter phone number", Toast.LENGTH_SHORT).show()
                    return
                }
                if(binding.txtMessageText.text.toString().isNullOrEmpty()){
                    Toast.makeText(binding.root.context, "please enter message text", Toast.LENGTH_SHORT).show()
                    return
                }
                val messageCommand = NewCommandModel.MessageCommand(binding.txtCommandPhone.text.toString(),
                    binding.txtMessageText.text.toString())

                commandModel = NewCommandModel(
                    null,
                    2,
                    binding.txtCommandName.text.toString(),
                    binding.txtCommandLabel.text.toString(),
                    messageCommand,
                    null,
                    null,
                    null
                )
            }
            else -> {
                commandModel = NewCommandModel(
                    null,
                    0,
                    binding.txtCommandName.text.toString(),
                    binding.txtCommandLabel.text.toString(),
                    null,
                    null,
                    null,
                    null)
            }
        }
        val token = "Bearer " + MySharedPreferences.getUserToken(binding.root.context)
        var myProgress = MyProgressDialog(binding.root.context, R.raw.lottie_bluetooth_turnedoff)
        var apiInterface = ApiInterface.create(token).addUserCommand(commandModel)
        myProgress.showDialog()
        apiInterface.enqueue(object : Callback<AddUserCommandResponse> {
            override fun onResponse(
                call: Call<AddUserCommandResponse>,
                response: Response<AddUserCommandResponse>
            ) {
                myProgress.closeDialog()
                if (response.code() == 200) {
                    if (response.body()!!.isSuccess == true) {
                        myProgress.closeDialog()
                        activity?.supportFragmentManager?.popBackStack()
                    } else {
                        Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast.makeText(context, response.body()!!.message, Toast.LENGTH_LONG)
                        .show()
                }
                myProgress.closeDialog()
            }

            override fun onFailure(call: Call<AddUserCommandResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show();
                myProgress.closeDialog()
            }
        })
    }
}


