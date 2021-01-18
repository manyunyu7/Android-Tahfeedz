package com.feylabs.tahfidz.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Util.API_Endpoint
import org.json.JSONObject


class MentorViewModel : ViewModel() {
    var loginStatus = MutableLiveData<Int>()
    var status = MutableLiveData<MutableMap<String, String>>()
    var tempStatus = mutableMapOf<String, String>()

    //1 = Success
    //0 = FAN ERROR
    //3 = Loading
    //2 = HTPP ERROR
    var statusGetUpdated = MutableLiveData<Int>()
    var statusUpdateBasic = MutableLiveData<Int>()

    var statusChangePassword = MutableLiveData<Int>()

    var mentorData = MutableLiveData<MutableMap<String, String>>()
    var mentorDataAfterUpdate = MutableLiveData<MutableMap<String, String>>()

    var tempMentorData = mutableMapOf<String, String>()
    var tempGetUpdated = mutableMapOf<String, String>()
    var tempGroupData = mutableMapOf<String, String>()

    fun loginMentor(username: String, password: String) {
        loginStatus.postValue(3)
        AndroidNetworking.post(API_Endpoint.LOGIN_MENTOR)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("FAN INFO", response.toString())
                    if (response.getInt("http_code") == 200) {
                        loginStatus.postValue(1)
                        val mentor = response.getJSONObject("mentor")
                        mentor.apply {
                            val id = getString("id")
                            val name = getString("name")
                            val email = getString("email")
                            val contact = getString("contact")
                            val gender = getString("gender")
                            val created_at = getString("created_at")
                            val updated_at = getString("updated_at")
                            val mentor_photo = getString("mentor_photo")
                            tempMentorData["login_type"] = "mentor"
                            tempMentorData["mentor_id"] = id
                            tempMentorData["mentor_name"] = name
                            tempMentorData["mentor_email"] = email
                            tempMentorData["mentor_contact"] = contact
                            tempMentorData["mentor_gender"] = gender
                            tempMentorData["created_at"] = created_at
                            tempMentorData["updated_at"] = updated_at
                            tempMentorData["mentor_photo"] = mentor_photo
                        }
                        tempStatus.put("code", "200")
                        tempStatus.put("status", "success")

                        status.postValue(tempStatus)
                        mentorData.postValue(tempMentorData)

                    } else {
                        loginStatus.postValue(2)
                        tempStatus.put("code", "404")
                        tempStatus.put("status", response.getString("message"))

                        status.postValue(tempStatus)
                        mentorData.postValue(tempMentorData)
                    }
                }

                override fun onError(anError: ANError) {
                    loginStatus.postValue(0)
                    tempStatus.put("code", "404")
                    tempStatus.put("status", anError.errorDetail.toString())

                    status.postValue(tempStatus)
                    mentorData.postValue(tempMentorData)
                }

            })
    }

    fun getUpdatedData(id: String, context: Context) {
        var tempStatusGetUpdated = 3
        statusGetUpdated.postValue(3)
        var tempUpdatedData = mutableMapOf<String,String>()
        AndroidNetworking.post(API_Endpoint.MENTOR_DATA)
            .addBodyParameter("id", id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-info-update", response.toString())
                    Log.e("FAN INFO", response.toString())
                    if (response.getInt("http_code") == 200) {
                        tempStatusGetUpdated = 1
                        statusGetUpdated.postValue(1)
                        val mentor = response.getJSONObject("mentor")
                        mentor.apply {
                            val id = getString("id")
                            val name = getString("name")
                            val email = getString("email")
                            val contact = getString("contact")
                            val gender = getString("gender")
                            val created_at = getString("created_at")
                            val updated_at = getString("updated_at")
                            tempGetUpdated["login_type"] = "mentor"
                            tempGetUpdated["mentor_id"] = id
                            tempGetUpdated["mentor_name"] = name
                            tempGetUpdated["mentor_email"] = email
                            tempGetUpdated["mentor_contact"] = contact
                            tempGetUpdated["mentor_gender"] = gender
                            tempGetUpdated["created_at"] = created_at
                            tempGetUpdated["updated_at"] = updated_at

                            mentorDataAfterUpdate.postValue(tempGetUpdated)
                        }
                    } else {
                        tempStatusGetUpdated = 2
                        statusGetUpdated.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
                    tempStatusGetUpdated = 0
                    statusGetUpdated.postValue(0)
                    Log.i("fan-info-update", anError.toString())
                }
            })
        statusGetUpdated.value = tempStatusGetUpdated
    }

    fun changePassword(id: String, old: String, new: String) {
        AndroidNetworking.post(API_Endpoint.MENTOR_UPDATE_AUTH)
            .addBodyParameter("mentor_id", id)
            .addBodyParameter("old_password", old)
            .addBodyParameter("new_password", new)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("FAN-mentor-pass", response.toString())
                    if (response.getInt("response_code") == 1) {
                        statusChangePassword.postValue(1)
                    } else {
                        statusChangePassword.postValue(2)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("Error-Fan-update", anError.toString())
                    statusChangePassword.postValue(0)
                }
            })
    }

    fun updateData(id: String, name: String, email: String, contact: String) {
        statusUpdateBasic.postValue(3)
        AndroidNetworking.post(API_Endpoint.MENTOR_UPDATE)
            .addBodyParameter("mentor_id", id)
            .addBodyParameter("name", name)
            .addBodyParameter("email", email)
            .addBodyParameter("contact", contact)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-resp-update-mentor",response.toString())
                    if (response.getInt("response_code") == 1) {
                        statusUpdateBasic.postValue(1)
                    } else {
                        statusUpdateBasic.postValue(2)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("fan-resp-update-mentor",anError.toString())
                    statusUpdateBasic.postValue(0)
                }

            })
    }


    fun getMentorData(): MutableMap<String, String> {
        return tempMentorData
    }

    fun getMentorUpdated(): MutableMap<String, String> {
        return tempGetUpdated
    }

}