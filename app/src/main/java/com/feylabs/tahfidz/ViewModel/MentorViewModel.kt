package com.feylabs.tahfidz.ViewModel

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
    var status = MutableLiveData<MutableMap<String,String>>()
    var tempStatus = mutableMapOf<String,String>()

    var mentorData = MutableLiveData<MutableMap<String, String>>()
    var mentorGroupData = MutableLiveData<MutableMap<String, String>>()

    var tempMentorData = mutableMapOf<String, String>()
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
                            tempMentorData["login_type"] = "mentor"
                            tempMentorData["mentor_id"] = id
                            tempMentorData["mentor_name"] = name
                            tempMentorData["mentor_email"] = email
                            tempMentorData["mentor_contact"] = contact
                            tempMentorData["mentor_gender"] = gender
                            tempMentorData["created_at"] = created_at
                            tempMentorData["updated_at"] = updated_at
                        }
                        tempStatus.put("code","200")
                        tempStatus.put("status","success")

                        status.postValue(tempStatus)
                        mentorData.postValue(tempMentorData)

                    } else {
                        tempStatus.put("code","404")
                        tempStatus.put("status",response.getString("message"))

                        status.postValue(tempStatus)
                        mentorData.postValue(tempMentorData)
                    }
                }

                override fun onError(anError: ANError) {
                    tempStatus.put("code","404")
                    tempStatus.put("status",anError.errorDetail.toString())

                    status.postValue(tempStatus)
                    mentorData.postValue(tempMentorData)
                }

            })
    }


    fun getMentorData()  : MutableMap<String,String> {
        return tempMentorData
    }

}