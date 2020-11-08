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

class SubmissionViewModel : ViewModel(){
    var status = MutableLiveData<Boolean>()
    val dataSubmission = MutableLiveData<MutableList<SubmissionModel>>()

    fun retrieveSubmissionStudent(id:String){
        val dataSubmissionAPI = mutableListOf<SubmissionModel>()
        AndroidNetworking.post(URL.SUBMISSION)
            .addBodyParameter("student_id",id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener{
                override fun onResponse(response: JSONObject) {
                    Log.i("responSub",response.toString())
                    if (response.getInt("response_code")==1){
                        val submission = response.getJSONArray("submission")
                        for (i in 0 until submission.length()){
                            val sub_id = submission.getJSONObject(i).getString("id_submission")
                            val sub_student_id = submission.getJSONObject(i).getString("id_submission")
                            val sub_status = submission.getJSONObject(i).getString("id_submission")
                            val sub_date = submission.getJSONObject(i).getString("id_submission")
                            val sub_student_name = submission.getJSONObject(i).getString("id_submission")
                            val sub_start = submission.getJSONObject(i).getString("id_submission")
                            val sub_end = submission.getJSONObject(i).getString("id_submission")
                            val sub_audio = submission.getJSONObject(i).getString("id_submission")

                            dataSubmissionAPI.add(
                                SubmissionModel(
                                sub_id,sub_student_id,sub_date,sub_student_name,sub_status,
                                sub_start,sub_end,sub_audio
                            ))

                        }
                        status.postValue(true)
                        dataSubmission.postValue(dataSubmissionAPI)
                    }else{
                        status.postValue(false)
                    }
                }

                override fun onError(anError: ANError?) {
                    Log.i("responSub",anError.toString())
                    status.postValue(false)
                }

            })
    }
}