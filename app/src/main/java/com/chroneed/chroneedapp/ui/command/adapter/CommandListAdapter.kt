package com.chroneed.chroneedapp.ui.command.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.DeleteCommandResponse
import com.chroneed.chroneedapp.data.medical.GetCommandModel
import com.chroneed.chroneedapp.data.medical.TrainResponseDto
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.ui.command.TrainCommandByWordActivity
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CommandListAdapter constructor(private val items: MutableList<GetCommandModel>, val activity: FragmentActivity) :
    RecyclerView.Adapter<CommandListAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_command_simple, parent, false)
        return viewHolder(inflater)
    }


    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnTrain = holder.itemView.findViewById<ImageView>(R.id.card_item_command_train)
        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_item_command_train_delete)
        val btnClear = holder.itemView.findViewById<ImageView>(R.id.card_item_command_train_clear)


        btnTrain.setOnClickListener() {
            val intent = Intent(context, TrainCommandByWordActivity::class.java)
            intent.putExtra("id",items[position].id)
            activity.startActivity(intent)
        }
        btnDelete.setOnClickListener{
            deleteItem(holder,position)
        }
        btnClear.setOnClickListener{
            clearTrainItem(holder,position)
        }
        holder.bind(items[position])
    }

    private fun clearTrainItem(holder: viewHolder, position: Int) {
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).updateUserCommand_ClearTrains(id)
        apiInterface.enqueue(object : Callback<TrainResponseDto> {
            override fun onResponse(call: Call<TrainResponseDto>, response: Response<TrainResponseDto>) {
                myProgress.closeDialog()
                if(response.code()==200){
                    if(response.body()!!.isSuccess){
                        notifyDataSetChanged()
                    }
                }
            }
            override fun onFailure(call: Call<TrainResponseDto>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    private fun deleteItem(holder: viewHolder, position: Int) {
        val id = items[position].id.toString()
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).deleteUserCommand(id)
        apiInterface.enqueue(object : Callback<DeleteCommandResponse> {
            override fun onResponse(call: Call<DeleteCommandResponse>, response: Response<DeleteCommandResponse>) {
                myProgress.closeDialog()
                if(response.code()==200){
                    if(response.body()!!.isSuccess){
                        items.remove(items[position])
                        notifyDataSetChanged()
                    }
                }

            }
            override fun onFailure(call: Call<DeleteCommandResponse>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val txtName = itemView.findViewById<TextView>(R.id.card_command_item_name)
        private val txtLabel = itemView.findViewById<TextView>(R.id.card_command_item_label)
        private val txtType = itemView.findViewById<TextView>(R.id.card_command_item_type)

        @SuppressLint("SetTextI18n")
        fun bind(item: GetCommandModel) {
            txtName.text = item.commandName
            txtLabel.text = item.label
            when (item.commanType) {
                0 -> {//Justlabel
                    txtType.text="Dictionary"
                }
                1 -> {//Call
                    txtType.text="Call"
                }
                2->{//Message
                    txtType.text="Message"
                }
                3->{//Medicine
                    txtType.text="Medicine"
                }
                4->{//Habit
                    txtType.text="Habit"
                }
                5->{//Goal
                    txtType.text="Goal"
                }
            }
        }
    }

}