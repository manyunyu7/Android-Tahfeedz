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


class MentorLoginViewModel(): ViewModel() {
    var status = MutableLiveData<Boolean>()
    var mentorData = MutableLiveData<MutableMap<String,String>>()
    var mentorGroupData = MutableLiveData<MutableMap<String,String>>()

    var tempStudentData = mutableMapOf<String,String>()
    var tempGroupData = mutableMapOf<String,String>()

    fun loginStudent(username : String , password : String){
        AndroidNetworking.post(URL.LOGIN_STUDENT)
            .addBodyParameter("username",username)
            .addBodyParameter("password",password)
            .build()
            .getAsJSONObject(object :JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
//                    TODO("Not yet implemented"
                    Log.e("FAN INFO",response.toString())
                    if(response.getInt("http_code") == 200){

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

                        if (kelompok!=null || kelompok != "null"){
                            val group = response.getJSONObject("group_data")
                            val groupID = group.getString("id")
                            val groupName = group.getString("group_name")
                            val mentor_name = group.getString("mentor_name")
                            val mentor_id = group.getString("mentor_id")
                            val mentor_contact = group.getString("mentor_contact")

                            tempGroupData["id_group"] = groupID
                            tempGroupData["group_name"] = groupName
                            tempGroupData["mentor_id"] = mentor_id
                            tempGroupData["mentor_name"] = mentor_name
                            tempGroupData["mentor_contact"] = mentor_contact
                        }
//                        studentData.postValue(tempStudentData)
//                        groupData.postValue(tempGroupData)
                        status.postValue(true)


                    }else{
                     status.postValue(false)
                    }
                }

                override fun onError(anError: ANError) {
//                    TODO("Not yet implemented")
                    status.postValue(false)
                    Log.e("Error Login FAN",anError.toString())
                }

            })
    }

//    fun getGroupData() : LiveData<MutableMap<String,String>> {
//        return groupData
//    }
//
//    fun getStudentData()  : LiveData<MutableMap<String,String>> {
//        return studentData
//    }

}