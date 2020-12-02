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


class StudentViewModel : ViewModel() {

    //1 = Success
    //0 = FAN ERROR
    //3 = Loading
    //2 = HTPP ERROR

    var statusLogin = MutableLiveData<Int>()
    var statusGetUpdated = MutableLiveData<Int>()
    var statusUpdateBasic = MutableLiveData<Int>()
    var statusChangePassword = MutableLiveData<Int>()
    var studentData = MutableLiveData<MutableMap<String, String>>()
    var groupData = MutableLiveData<MutableMap<String, String>>()

//    var tempStatusChangePassword = 99

    var tempStudentData = mutableMapOf<String, String>()
    var tempUpdatedData = mutableMapOf<String, String>()
    var tempGroupData = mutableMapOf<String, String>()
    var tempUpdatedGroupData = mutableMapOf<String, String>()

    fun loginStudent(username: String, password: String) {
        statusLogin.postValue(3)
        AndroidNetworking.post(URL.LOGIN_STUDENT)
            .addBodyParameter("username", username)
            .addBodyParameter("password", password)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
//                    TODO("Not yet implemented"
                    Log.e("FAN INFO", response.toString())
                    if (response.getInt("http_code") == 200) {

                        val student = response.getJSONObject("student")
                        val id = student.getString("id")
                        val name = student.getString("name")
                        val nisn = student.getString("nisn")
                        val email = student.getString("email")
                        val contact = student.getString("contact")
                        val gender = student.getString("gender")
                        val created_at = student.getString("created_at")
                        val update_at = student.getString("updated_at")
                        val url_profile = student.getString("url_profile")

                        val kelompok = student.getString("kelompok")


                        tempStudentData["login_type"] = "student"
                        tempStudentData["student_id"] = id
                        tempStudentData["student_name"] = name
                        tempStudentData["student_nisn"] = nisn
                        tempStudentData["student_email"] = email
                        tempStudentData["student_group"] = kelompok
                        tempStudentData["student_contact"] = contact
                        tempStudentData["student_gender"] = gender
                        tempStudentData["created_at"] = created_at
                        tempStudentData["updated_at"] = update_at
                        tempStudentData["student_photo"] = url_profile

                        if (kelompok.toString() != "null") {
                            val group = response.getJSONObject("group_data")
                            val groupID = group.getString("id")
                            val groupName = group.getString("group_name")
                            val mentor_name = group.getString("mentor_name")
                            val mentor_id = group.getString("mentor_id")
                            val mentor_contact = group.getString("mentor_contact")
                            val groupAnnouncement = group.getString("announcement")

                            tempGroupData["id_group"] = groupID
                            tempGroupData["group_name"] = groupName
                            tempGroupData["group_mentor_id"] = mentor_id
                            tempGroupData["group_mentor_name"] = mentor_name
                            tempGroupData["group_mentor_contact"] = mentor_contact
                            tempGroupData["group_temp"] = groupID
                            tempUpdatedGroupData["group_announcement"] = groupAnnouncement


                        }
                        statusLogin.postValue(1)
                        studentData.postValue(tempStudentData)
                        groupData.postValue(tempGroupData)
                    } else {
                        statusLogin.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
//                    TODO("Not yet implemented")
                    statusLogin.postValue(0)
                    Log.e("Error Login FAN", anError.toString())
                    Log.e("Error Login FAN", anError.toString())
                    Log.e("Error Login FAN", anError.errorDetail)
                }

            })
    }

    fun getStudentData(id: String) {
        statusGetUpdated.postValue(3)
        AndroidNetworking.post(URL.STUDENT_DATA)
            .addBodyParameter("id", id)
            .addHeaders("id", id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
//                    TODO("Not yet implemented"
                    Log.i("FAN-getStudentData", response.toString())
                    Log.i("FAN-getStudentData", id)
                    if (response.getInt("http_code") == 200) {

                        val student = response.getJSONObject("student")
                        val id = student.getString("id")
                        val name = student.getString("name")
                        val nisn = student.getString("nisn")
                        val email = student.getString("email")
                        val contact = student.getString("contact")
                        val gender = student.getString("gender")
                        val created_at = student.getString("created_at")
                        val update_at = student.getString("updated_at")
                        val kelompok = student.getString("kelompok")
                        val url_profile = student.getString("url_profile")

                        tempUpdatedData["login_type"] = "student"
                        tempUpdatedData["student_id"] = id
                        tempUpdatedData["student_name"] = name
                        tempUpdatedData["student_nisn"] = nisn
                        tempUpdatedData["student_email"] = email
                        tempUpdatedData["student_group"] = kelompok
                        tempUpdatedData["student_contact"] = contact
                        tempUpdatedData["student_gender"] = gender
                        tempUpdatedData["created_at"] = created_at
                        tempUpdatedData["updated_at"] = update_at
                        tempUpdatedData["student_photo"] = url_profile

                        if (kelompok.toString() != "null") {
                            val group = response.getJSONObject("group_data")
                            val groupID = group.getString("id")
                            val groupName = group.getString("group_name")
                            val groupAnnouncement = group.getString("announcement")
                            val mentor_name = group.getString("mentor_name")
                            val mentor_id = group.getString("mentor_id")
                            val mentor_contact = group.getString("mentor_contact")

                            tempUpdatedGroupData["id_group"] = groupID
                            tempUpdatedGroupData["group_name"] = groupName
                            tempUpdatedGroupData["group_mentor_id"] = mentor_id
                            tempUpdatedGroupData["group_mentor_name"] = mentor_name
                            tempUpdatedGroupData["group_announcement"] = groupAnnouncement
                            tempUpdatedGroupData["group_mentor_contact"] = mentor_contact
                        } else {
                            tempUpdatedGroupData["id_group"] = "null"
                            tempUpdatedGroupData["group_name"] = "null"
                            tempUpdatedGroupData["group_mentor_id"] = "null"
                            tempUpdatedGroupData["group_mentor_name"] = "null"
                            tempUpdatedGroupData["group_mentor_contact"] = "null"
                        }
                        studentData.postValue(tempStudentData)
                        groupData.postValue(tempUpdatedGroupData)
                        statusGetUpdated.postValue(1)
                    } else {
                        statusGetUpdated.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
                    statusGetUpdated.postValue(0)
                    Log.e("Error-StudentGet", anError.toString())
                }
            })
    }

    fun updateData(id: String, name: String, email: String, contact: String) {
        statusUpdateBasic.postValue(3)
        AndroidNetworking.post(URL.STUDENT_UPDATE_BASIC)
            .addBodyParameter("student_id", id)
            .addBodyParameter("name", name)
            .addBodyParameter("email", email)
            .addBodyParameter("contact", contact)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getInt("response_code") == 1) {
                        statusUpdateBasic.postValue(1)
                    } else {
                        statusUpdateBasic.postValue(2)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("Error-Fan-update", anError.toString())
                    statusUpdateBasic.postValue(0)
                }

            })
    }

    fun changePassword(id: String, old: String, new: String) {
        var temp = 99
        AndroidNetworking.post(URL.STUDENT_UPDATE_AUTH)
            .addBodyParameter("student_id", id)
            .addBodyParameter("old_password", old)
            .addBodyParameter("new_password", new)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("FAN-stud-pass", response.toString())
                    if (response.getInt("response_code") == 1) {
                        temp = 1
                        statusChangePassword.value = 1
                    } else {
                        statusChangePassword.postValue(2)
                        statusChangePassword.value = 2
                        temp = 2
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("Error-Fan-update", anError.toString())
                    temp = 0
                    statusChangePassword.value = 0
                }
            })
    }


    fun getGroupData(): LiveData<MutableMap<String, String>> {
        return groupData
    }

    fun getStudentData(): LiveData<MutableMap<String, String>> {
        return studentData
    }

}