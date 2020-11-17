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

    var groupMember = MutableLiveData<MutableList<GroupMemberModel>>()
    var tempGroupMember = mutableListOf<GroupMemberModel>()

    var status = false

     fun retrieveGroupList(mentor_id: String) {
        AndroidNetworking.post(URL.GROUPING_MENTOR)
            .addBodyParameter("mentor_id", mentor_id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.i("Fan-Group",mentor_id)
                    Log.i("Fan-Group",response.toString())
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
                    }else{
                        statusGroupList.postValue(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("Fan-Group",anError.toString())
                    statusGroupList.postValue(false)
                }

            })
    }

    private fun retrieveGroupMember() {
        AndroidNetworking.post(URL.GROUP_DATA)
            .addBodyParameter("mentor_id")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (response.getInt("response_code") == 1) {
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
                                    val mentor_contact = getString("mentor_contact")

                                    tempGroupMember.add(
                                        GroupMemberModel(
                                            id,
                                            name,
                                            contact,
                                            mentor_id,
                                            group_id,
                                            group_name,
                                            mentor_name,
                                            mentor_contact
                                        )
                                    )
                                }
                            }
                            groupMember.postValue(tempGroupMember)
                        }
                    } else {
                        status = false
                    }
                }

                override fun onError(anError: ANError?) {
                    status = false
                }

            })
    }

}