package com.feylabs.tahfidz.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.AdminModel
import com.feylabs.tahfidz.Util.API_Endpoint
import org.json.JSONObject

class AdminViewModel : ViewModel() {

    var statusGetAdmin = MutableLiveData<Int>()
    var dataAdmin = MutableLiveData<MutableList<AdminModel>>()


    fun getAdmin() {
        var tempListAdmin = mutableListOf<AdminModel>()
        AndroidNetworking.post(API_Endpoint.GET_ADMIN)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getInt("response_code") == 1) {
                        statusGetAdmin.postValue(1)
                        var mot = response.getJSONArray("admin")
                        for (i in 0 until mot.length()) {
                            val id = mot.getJSONObject(i).getString("id")
                            val admin_name = mot.getJSONObject(i).getString("admin_name")
                            val admin_contact = mot.getJSONObject(i).getString("admin_contact")
                            val admin_desc = mot.getJSONObject(i).getString("admin_desc")
                            val admin_email = mot.getJSONObject(i).getString("admin_email")
                            tempListAdmin.add(
                                AdminModel(
                                    id = id,
                                    name = admin_name,
                                    contact = admin_contact,
                                    desc = admin_desc,
                                    email = admin_email
                                )
                            )
                        }
                        dataAdmin.postValue(tempListAdmin)
                    } else if (response.getInt("response_code") == 3) {
                        statusGetAdmin.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
                    statusGetAdmin.postValue(0)
                }

            })
    }

}