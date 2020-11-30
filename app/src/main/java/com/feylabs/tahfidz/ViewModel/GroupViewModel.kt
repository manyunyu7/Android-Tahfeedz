package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.GroupMemberModel
import com.feylabs.tahfidz.Model.GroupModel
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject

class GroupViewModel : ViewModel() {
    var groupList = MutableLiveData<MutableList<GroupModel>>()
    var tempGroupList = mutableListOf<GroupModel>()
    var statusGroupList = MutableLiveData<Boolean>()

    var statusRetrieveGroupMember = MutableLiveData<Int>()
    // 1 : Available
    // 2 : Success but null
    // 3 : Loading
    // 0 : Failed


    var groupMember = MutableLiveData<MutableList<GroupMemberModel>>()
    var tempGroupMember = mutableListOf<GroupMemberModel>()

    var status = false

    fun retrieveGroupList(mentor_id: String) {
        AndroidNetworking.post(URL.GROUPING_MENTOR)
            .addBodyParameter("mentor_id", mentor_id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.i("Fan-Group", mentor_id)
                    Log.i("Fan-Group", response.toString())
                    if (response?.getInt("response_code") == 1 && response.getInt("size") != 0) {
                        val group = response.getJSONArray("group")
                        for (i in 0 until group.length()) {
                            group.apply {
                                val id = getJSONObject(i).getString("group_id")
                                val name = getJSONObject(i).getString("group_name")
                                val category = getJSONObject(i).getString("group_category")
                                tempGroupList.add(
                                    GroupModel(
                                        id, name, category
                                    )
                                )
                            }
                        }
                        statusGroupList.postValue(true)
                        groupList.postValue(tempGroupList)
                    } else {
                        statusGroupList.postValue(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("Fan-Group", anError.toString())
                    statusGroupList.postValue(false)
                }

            })
    }

    fun retrieveGroupMember(mentor_id_: String) {
        var tempStat = 3
        statusRetrieveGroupMember.value = (tempStat)
        AndroidNetworking.post(URL.GROUP_DATA)
            .addBodyParameter("mentor_id", mentor_id_)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-group-member", response.toString())
                    if (response.getInt("response_code") == 1) {
                        tempStat = 1
                        statusRetrieveGroupMember.value = (tempStat)
                        val resG = response.getJSONArray("group_member")
                        if (resG.length() != 0) {
                            for (i in 0 until resG.length()) {
                                val resGMember = resG.getJSONObject(i)
                                resGMember.apply {
                                    val id = getString("student_id")
                                    val name = getString("name")
                                    val nisn = getString("nisn")
                                    val contact = getString("student_contact")
                                    val mentor_id = getString("mentor_id")
                                    val group_id = getString("group_id")
                                    val group_name = getString("group_name")
                                    val mentor_name = getString("mentor_name")
                                    val mentor_photo = getString("mentor_photo")
                                    val student_photo = getString("student_photo")
                                    val student_class = getString("student_class")
                                    val student_gender = getString("student_gender")
                                    val mentor_contact = getString("mentor_contact")

                                    tempGroupMember.add(
                                        GroupMemberModel(
                                            student_id = id,
                                            student_class = student_class,
                                            student_contact = contact,
                                            student_nisn = nisn,
                                            student_photo = student_photo,
                                            mentor_contact = mentor_contact,
                                            mentor_id = mentor_id,
                                            mentor_name = mentor_name,
                                            mentor_photo = mentor_photo,
                                            name = name,
                                            group_name = group_name,
                                            group_id = group_id,
                                            student_gender = student_gender
                                        )
                                    )
                                }
                            }
                        }
                        groupMember.postValue(tempGroupMember)
                    } else {
                        tempStat = 2
                        statusRetrieveGroupMember.value = (tempStat)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("fan-group-member", anError.toString())
                    tempStat = 0
                    statusRetrieveGroupMember.value = (tempStat)
                }
            })

        statusRetrieveGroupMember.value = (tempStat)
    }

}