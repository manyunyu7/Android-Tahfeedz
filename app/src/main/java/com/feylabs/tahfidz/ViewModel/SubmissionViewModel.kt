package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.Util.URL
import org.json.JSONObject


class SubmissionViewModel : ViewModel() {
    var status = MutableLiveData<Int>()
    var statusDelete = MutableLiveData<Boolean>()
    val dataSubmission = MutableLiveData<MutableList<SubmissionModel>>()

    fun retrieveSubmissionStudent(id: String, type: String, isIndividually: Boolean = false,individualID : String) {
        var param = ""
        var ids = ""
        ids=id
        if (type=="student") {
            param = "student_id"
        }
        if (type=="mentor"){
            param = "group_id"
        }
        if (isIndividually){
            param="student_id"
            ids = individualID
        }
        Log.i("responseSub",id+param)

        val dataSubmissionAPI = mutableListOf<SubmissionModel>()
        status.postValue(3)
        AndroidNetworking.post(URL.SUBMISSION)
            .addBodyParameter(param, ids)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("responSub", response.toString())
                    if (response.getInt("response_code") == 1) {
                        val submission = response.getJSONArray("submission")
                        for (i in 0 until submission.length()) {
                            val sub_id = submission.getJSONObject(i).getString("id_submission")
                            val sub_student_id = submission.getJSONObject(i).getString("id_student")
                            val sub_status = submission.getJSONObject(i).getString("status")
                            val sub_date = submission.getJSONObject(i).getString("date")
                            val sub_student_name =
                                submission.getJSONObject(i).getString("student_name")
                            val sub_start = submission.getJSONObject(i).getString("start")
                            val sub_end = submission.getJSONObject(i).getString("end")
                            val sub_audio = submission.getJSONObject(i).getString("audio")
                            val sub_score = submission.getJSONObject(i).getString("score")
                            val sub_correction = submission.getJSONObject(i).getString("correction")

                            dataSubmissionAPI.add(
                                SubmissionModel(
                                    sub_id, sub_student_id, sub_date, sub_student_name, sub_status,
                                    sub_start, sub_end, sub_audio,sub_score,sub_correction
                                )
                            )

                        }
                        status.postValue(1)
                        dataSubmission.postValue(dataSubmissionAPI)
                    } else {
                        status.postValue(2)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("responSub", anError.toString())
                    status.postValue(0)
                }
            })
    }

    fun deleteSubmission(
        submissionID: String
    ) {
        Log.i("id_submission", submissionID)
        AndroidNetworking.post(URL.DELETE_SUBMISSION)
            .addBodyParameter("submission_id", submissionID)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("FAN-delSub",response.toString())
                    if (response.getInt("response_code") == 1) {
                        statusDelete.postValue(true)
                    } else {
                        statusDelete.postValue(false)
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("FAN-delSub",anError.toString())
                    statusDelete.postValue(false)
                }
            })
    }

}