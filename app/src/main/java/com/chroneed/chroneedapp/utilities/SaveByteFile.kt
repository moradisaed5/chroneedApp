package com.chroneed.chroneedapp.utilities

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.chroneed.chroneedapp.network.ApiInterface
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*

class SaveByteFile {

    private var fileName="conv_actions_frozen.tflite"
     fun getTrainFile(context: Context) {
        val token = "Bearer " + MySharedPreferences.getUserToken(context)//token
        try {
            var apiInterface = ApiInterface.create(token).downloadTrainFile()
            apiInterface.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    var temp=response.body()!!.bytes()
                    saveTrainFile(context,temp)
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable?) {
                }
            })
        } catch (ex: Exception) {
        }
    }
   private fun saveTrainFile(context: Context, byteFile: ByteArray) {
        // Create an image file name
        deleteOnExistFile(context)
        var filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path + fileName
        var file = File(filePath)
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(byteFile)
        fos.close()
        Toast.makeText(context, "train file created ...", Toast.LENGTH_LONG).show()
    }

    private fun deleteOnExistFile(context: Context) {
        var filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.path + fileName
        var file = File(filePath)
        file.deleteOnExit()
    }
}