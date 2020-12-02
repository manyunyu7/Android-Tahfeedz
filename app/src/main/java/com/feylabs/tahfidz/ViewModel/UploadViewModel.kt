package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject
import java.io.File

class UploadViewModel : ViewModel() {
    var status = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()
    var uploadedListener = MutableLiveData<String>()

    fun uploadFile(
        file: File,
        student_id: String,
        student_name: String,
        group_id: String,
        start: String,
        end: String
    ) {
        Log.i("dataUpload", file.toString())
        Log.i("dataUpload", student_id)
        Log.i("dataUpload", student_name)
        Log.i("dataUpload", group_id)
        Log.i("dataUpload", start)
        Log.i("dataUpload", end)
        AndroidNetworking.upload(URL.UPLOAD_SUBMISSION)
            .addMultipartFile("inputSubmission", file)
            .addMultipartParameter("studentID", student_id)
            .addMultipartParameter("studentName", student_name)
            .addMultipartParameter("group", group_id)
            .addMultipartParameter("start", start)
            .addMultipartParameter("end", end)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->
                // do anything with progress
                uploadedListener.postValue(
                    "${bytesUploaded / 1024} KB of ${totalBytes / 1024} KB"
                )
            }
            .getAsJSONObject(object : JSONObjectRequestListener {

                override fun onResponse(response: JSONObject) {
                    Log.i("UploadRespon", response.toString())
                    if (response.getInt("response_code") == 0) {
                        status.postValue(false)
                        message.postValue(response.getString("message"))
                    } else if (response.getInt("response_code") == 1) {
                        status.postValue(true)
                        message.postValue(response.getString("message"))
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("UploadRespon", anError.toString())
                    status.postValue(false)
                    message.postValue("FAN Error")
                }
            })
    }


}