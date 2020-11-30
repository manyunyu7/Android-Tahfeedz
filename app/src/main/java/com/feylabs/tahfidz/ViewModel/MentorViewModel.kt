package com.feylabs.tahfidz.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject


class MentorViewModel : ViewModel() {
    var status = MutableLiveData<MutableMap<String, String>>()
    var tempStatus = mutableMapOf<String, String>()

    //1 = Success
    //0 = FAN ERROR
    //3 = Loading
    //2 = HTPP ERROR
    var statusGetUpdated = MutableLiveData<Int>()
    var statusUpdateBasic = MutableLiveData<Int>()

    var mentorData = MutableLiveData<MutableMap<String, String>>()
    var mentorGroupData = MutableLiveData<MutableMap<String, String>>()

    var tempMentorData = mutableMapOf<String, String>()
    var tempGetUpdated = mutableMapOf<String, String>()
    var tempGroupData = mutableMapOf<String, String>()

    fun loginMentor(username: String, password: String) {
        AndroidNetworking.post(URL.LOGIN_MENTOR)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.e("FAN INFO", response.toString())
                    if (response.getInt("http_code") == 200) {
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
                        tempStatus.put("code", "404")
                        tempStatus.put("status", response.getString("message"))

                        status.postValue(tempStatus)
                        mentorData.postValue(tempMentorData)
                    }
                }

                override fun onError(anError: ANError) {
                    tempStatus.put("code", "404")
                    tempStatus.put("status", anError.errorDetail.toString())

                    status.postValue(tempStatus)
                    mentorData.postValue(tempMentorData)
                }

            })
    }

    fun getUpdatedData(id: String, context: Context) {
        var tempStatusGetUpdated = 3
        statusGetUpdated.value = 3
        AndroidNetworking.post(URL.MENTOR_DATA)
            .addBodyParameter("id", id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-info-update", response.toString())
                    Log.e("FAN INFO", response.toString())
                    if (response.getInt("http_code") == 200) {
                        tempStatusGetUpdated = 1
                        statusGetUpdated.value = 1
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

                            for ((key, value) in tempGetUpdated) {
                                Log.i("pref mentor $key", value)
                                Preference(context).save(key, value)
                            }
                        }
                    } else {
                        tempStatusGetUpdated = 2
                        statusGetUpdated.value = 2
                    }
                }

                override fun onError(anError: ANError) {
                    statusGetUpdated.value = 0
                    tempStatusGetUpdated = 0
                    Log.i("fan-info-update", anError.toString())
                }
            })
        statusGetUpdated.value = tempStatusGetUpdated
    }

    fun updateData(id: String, name: String, email: String, contact: String) {
        statusUpdateBasic.postValue(3)
        AndroidNetworking.post(URL.MENTOR_UPDATE)
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