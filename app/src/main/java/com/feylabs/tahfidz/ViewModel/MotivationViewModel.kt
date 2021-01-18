package com.feylabs.tahfidz.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.MotModel
import com.feylabs.tahfidz.Util.API_Endpoint
import org.json.JSONObject

class MotivationViewModel : ViewModel() {
    var status = MutableLiveData<String>()
    var listMot = MutableLiveData<MutableList<MotModel>>()

    fun getMot() {
        var tempListMot = mutableListOf<MotModel>()
        AndroidNetworking.post(API_Endpoint.MOT)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getInt("response_code") == 1) {
                        status.postValue("1")
                        var mot = response.getJSONArray("motivation")
                        for (i in 0 until mot.length()) {
                            tempListMot.add(
                                MotModel(
                                    mot.getJSONObject(i).getString("id"),
                                    mot.getJSONObject(i).getString("img"),
                                    mot.getJSONObject(i).getString("title"),
                                    mot.getJSONObject(i).getString("content")
                                )
                            )
                        }
                        listMot.postValue(tempListMot)
                    } else if (response.getInt("response_code") == 3) {
                        status.postValue("3")
                    }
                }

                override fun onError(anError: ANError) {
                    status.postValue(null)
                }

            })
    }

}