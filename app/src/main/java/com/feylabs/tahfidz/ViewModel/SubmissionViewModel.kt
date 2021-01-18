package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.Util.API_Endpoint
import org.json.JSONObject


class SubmissionViewModel : ViewModel() {
    var status = MutableLiveData<Int>()
    var statusDelete = MutableLiveData<Boolean>()
    val dataSubmission = MutableLiveData<MutableList<SubmissionModel>>()

    fun retrieveSubmissionStudent(
        id: String,
        type: String,
        isIndividually: Boolean = false,
        individualID: String
    ) {
        var param = ""
        var ids = ""
        ids = id
        if (type == "student") {
            param = "student_id"
        }
        if (type == "mentor") {
            param = "group_id"
        }
        if (isIndividually) {
            param = "student_id"
            ids = individualID
        }
        Log.i("responseSub", id + param)

        val dataSubmissionAPI = mutableListOf<SubmissionModel>()
        status.postValue(3)
        AndroidNetworking.post(API_Endpoint.SUBMISSION)
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
                            val sub_status_code = submission.getJSONObject(i).getString("status")
                            val sub_status_text = submission.getJSONObject(i).getString("status_text")
                            val sub_date = submission.getJSONObject(i).getString("date")
                            val sub_student_name =
                                submission.getJSONObject(i).getString("student_name")
                            val sub_start = submission.getJSONObject(i).getString("start")
                            val sub_end = submission.getJSONObject(i).getString("end")
                            val sub_audio = submission.getJSONObject(i).getString("audio")
                            val sub_correction = submission.getJSONObject(i).getString("correction")

                            val sub_score = submission.getJSONObject(i).getString("score")
                            val sub_score_ahkam =
                                submission.getJSONObject(i).getString("score_ahkam")
                            val sub_score_itqan =
                                submission.getJSONObject(i).getString("score_itqan")
                            val sub_score_makhroj =
                                submission.getJSONObject(i).getString("score_makhroj")
                            val sub_score_tajwid =
                                submission.getJSONObject(i).getString("score_tajwid")
                            val sub_score_final =
                                submission.getJSONObject(i).getString("score_tajwid")

                            dataSubmissionAPI.add(
                                SubmissionModel(
                                    id = sub_id,
                                    id_student = sub_student_id,
                                    date = sub_date,
                                    studentName = sub_student_name,
                                    status_code = sub_status_code,
                                    status_text = sub_status_text,
                                    start = sub_start,
                                    end = sub_end,
                                    audio = sub_audio,
                                    correction = sub_correction,
                                    score_ahkam = sub_score_ahkam,
                                    score_itqan = sub_score_itqan,
                                    score_makhroj = sub_score_makhroj,
                                    score_tajwid = sub_score_tajwid,
                                    score_final = sub_score_final
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
        AndroidNetworking.post(API_Endpoint.DELETE_SUBMISSION)
            .addBodyParameter("submission_id", submissionID)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("FAN-delSub", response.toString())
                    if (response.getInt("response_code") == 1) {
                        statusDelete.postValue(true)
                    } else {
                        statusDelete.postValue(false)
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("FAN-delSub", anError.toString())
                    statusDelete.postValue(false)
                }
            })
    }

}