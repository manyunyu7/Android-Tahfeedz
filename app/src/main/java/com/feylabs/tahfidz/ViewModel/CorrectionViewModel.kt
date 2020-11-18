package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject

class CorrectionViewModel : ViewModel() {
    var status = MutableLiveData<Boolean>()
    var message = MutableLiveData<MutableMap<String,String>>()

    fun updateSubmission(id:String,score:String,text:String){

        AndroidNetworking.post(URL.UPDATE_CORRECTION)
            .addBodyParameter("score",score)
            .addBodyParameter("correction",text)
            .addBodyParameter("submission_id",text)
            .build()
            .getAsJSONObject(object :JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    Log.i("fan-correct",response.toString())
                    if(response?.getInt("response_code")==1){
                        status.postValue(true)
                    }else{
                        status.postValue(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("fan-correct",anError.toString())
                    status.postValue(false)
                }

            })
    }
}