package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject
import java.io.File

class DeleteSubmissionViewModel : ViewModel() {
    var status = MutableLiveData<Boolean>()
    var message = MutableLiveData<String>()


}