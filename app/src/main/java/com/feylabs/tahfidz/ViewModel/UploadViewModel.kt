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
    var statusUpload = MutableLiveData<Int>()
    var message = MutableLiveData<String>()
    var uploadedListener = MutableLiveData<String>()

    fun uploadFile(
        file: File,
        student_id: String,
        student_name: String,
        group_id: String,
        start: String,
        end: String,
        type: String
    ) {
        statusUpload.postValue(3)
        AndroidNetworking.upload(URL.UPLOAD_SUBMISSION)
            .addMultipartFile("inputSubmission", file)
            .addMultipartParameter("studentID", student_id)
            .addMultipartParameter("studentName", student_name)
            .addMultipartParameter("group", group_id)
            .addMultipartParameter("start", start)
            .addMultipartParameter("end", end)
            .addMultipartParameter("type", type)
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
                        statusUpload.postValue(2)
                    } else if (response.getInt("response_code") == 1) {
                        statusUpload.postValue(1)
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("UploadRespon", anError.toString())
                    statusUpload.postValue(0)
                }
            })
    }


}