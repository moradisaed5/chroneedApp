package com.chroneed.chroneedapp.ui.command.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.chroneed.chroneedapp.R
import com.chroneed.chroneedapp.data.medical.DeleteVoiceResponseDto
import com.chroneed.chroneedapp.data.medical.GetVoiceResponseDto
import com.chroneed.chroneedapp.network.ApiInterface
import com.chroneed.chroneedapp.utilities.MyProgressDialog
import com.chroneed.chroneedapp.utilities.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommandRecordAdapter constructor(private val items: MutableList<String>, val activity: FragmentActivity) :
    RecyclerView.Adapter<CommandRecordAdapter.viewHolder>() {
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.card_item_command_voice, parent, false)
        return viewHolder(inflater)
    }

    private fun deleteVoice(holder: viewHolder, position: Int){

        val filename = items[position].split('/').last()
        val labelname =items[position].split('/')[items[position].split('/').count()-2]
        val tokenba = "Bearer " + MySharedPreferences.getUserToken(context)
        var myProgress = MyProgressDialog(context, R.raw.lottie_bluetooth_turnedoff)
        myProgress.showDialog()
        var apiInterface = ApiInterface.create(tokenba).deleteUserLabelVoices(labelname,filename)
        apiInterface.enqueue(object : Callback<DeleteVoiceResponseDto> {
            override fun onResponse(call: Call<DeleteVoiceResponseDto>, response: Response<DeleteVoiceResponseDto>) {
                myProgress.closeDialog()
                if(response.code()==200){
                    if(response.body()!!.isSuccess){
                        items.remove(items[position])
                        notifyDataSetChanged()
                    }
                }

            }
            override fun onFailure(call: Call<DeleteVoiceResponseDto>, t: Throwable?) {
                Toast.makeText(context, t?.message, Toast.LENGTH_LONG).show()
                myProgress.closeDialog()
            }
        })
    }
    private fun playVoice(holder: viewHolder, position: Int){
        val btnPlay = holder.itemView.findViewById<ImageView>(R.id.card_item_command_voice_btn_play)
        val lottiePlay = holder.itemView.findViewById<LottieAnimationView>(R.id.card_item_command_voice_lottie_play_record)

        var mediaRecorder: MediaRecorder? = null
        var uriString="http://chroneedapi.com/identity.api/"+items[position].toString()
        var soundUri = Uri.parse(uriString)
        var mPlayer = MediaPlayer.create(context, soundUri)
        mPlayer.start()
        val handler = Handler(Looper.getMainLooper())
        lottiePlay.visibility=View.VISIBLE
        btnPlay.visibility=View.GONE
        lottiePlay.playAnimation()
        handler.postDelayed({
            btnPlay.visibility=View.VISIBLE
            lottiePlay.visibility=View.GONE
        }, 3000)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val btnPlay = holder.itemView.findViewById<ImageView>(R.id.card_item_command_voice_btn_play)
        val btnDelete = holder.itemView.findViewById<ImageView>(R.id.card_item_command_voice_btn_delete)
        val lottiePlay = holder.itemView.findViewById<LottieAnimationView>(R.id.card_item_command_voice_lottie_play_record)
        btnPlay.setOnClickListener{
            playVoice(holder,position)
        }
        btnDelete.setOnClickListener{
            deleteVoice(holder,position)
        }
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(item: String) {
            val txtName = itemView.findViewById<TextView>(R.id.card_item_command_voice_file_name)
            txtName.text=item
        }
    }

}