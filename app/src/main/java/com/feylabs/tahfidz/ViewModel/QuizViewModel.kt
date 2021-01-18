package com.feylabs.tahfidz.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.feylabs.tahfidz.Model.QuizModel
import com.feylabs.tahfidz.Util.API_Endpoint
import org.json.JSONObject

class QuizViewModel : ViewModel() {

    val quizInsertStatus = MutableLiveData<Int>()

    val quizGetStatus = MutableLiveData<Int>()
    val listQuiz = MutableLiveData<MutableList<QuizModel>>()

    fun getQuiz(id : String) {
        var tempList = mutableListOf<QuizModel>()
        AndroidNetworking.post(API_Endpoint.GET_QUIZ)
            .addBodyParameter("group_id",id)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-quiz",response.toString())
                    if (response.getInt("response_code") == 1) {
                        val raz = response.getJSONArray("quiz")
                        for (i in 0 until raz.length()) {
                            val kyyy = raz.getJSONObject(i)
                            val id_quiz = kyyy.getString("id_quiz")
                            val group_id = kyyy.getString("group_id")
                            val title = kyyy.getString("title")
                            val desc = kyyy.getString("desc")
                            val gform_link = kyyy.getString("gform_link")
                            val created_at = kyyy.getString("created_at")
                            val updated_at = kyyy.getString("updated_at")
                            tempList.add(
                                QuizModel(
                                id_quiz = id_quiz,
                                    group_id = group_id,
                                    title = title,
                                    desc = desc,
                                    gform_link = gform_link,
                                    created_at = created_at,
                                    updated_at = updated_at
                            ))
                        }
                        quizGetStatus.postValue(1)
                        listQuiz.postValue(tempList)
                    } else if (response.getInt("response_code") == 3) {
                        quizGetStatus.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
                    quizGetStatus.postValue(0)
                    Log.i("fan-quiz",anError.toString())
                }

            })
    }

    fun insertQuiz(id : String,title:String,gform_link:String,desc:String) {
        var tempList = mutableListOf<QuizModel>()
        AndroidNetworking.post(API_Endpoint.CREATE_QUIZ)
            .addBodyParameter("group_id",id)
            .addBodyParameter("title",title)
            .addBodyParameter("gform_link",gform_link)
            .addBodyParameter("desc",desc)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.i("fan-quiz-create",response.toString())
                    if (response.getInt("response_code") == 1) {
                        quizInsertStatus.postValue(1)
                        listQuiz.postValue(tempList)
                    } else if (response.getInt("response_code") == 3) {
                        quizInsertStatus.postValue(2)
                    }
                }

                override fun onError(anError: ANError) {
                    quizInsertStatus.postValue(0)
                    Log.i("fan-quiz-create",anError.toString())
                }

            })
    }


}