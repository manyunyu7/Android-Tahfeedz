package com.feylabs.tahfidz.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.ViewModel.StudentLoginViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_login_student.*
import kotlinx.android.synthetic.main.layout_login_ustadz.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLayoutBinding()

        var loginViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentLoginViewModel::class.java);

        btnLoginStudent.setOnClickListener {
            val usr = etUsernameStudent.text.toString()
            val pass = etPasswordStudent.text.toString()
            if (usr.isEmpty() || pass.isEmpty()) {
                "Mohon Isi Username dan Password Terlebih Dahulu".showToast()
            }
        }
        btnLoginStudent.setOnClickListener {
            val usr = etUsernameStudent.text.toString()
            val pass = etPasswordStudent.text.toString()
            if (usr.isEmpty() || pass.isEmpty()) {
                "Mohon Isi Username dan Password Terlebih Dahulu".showToast()
            } else {
                loginViewModel.loginStudent(usr, pass)
                anim_loading.visibility = View.VISIBLE
            }
        }

        loginViewModel.status.observe(this, Observer {
            if (it) {
                anim_loading.visibility = View.GONE
                var studentData = loginViewModel.getStudentData()
                var groupData = loginViewModel.getGroupData()
                var studentMap = mutableMapOf<String, String>()
                var groupMap = mutableMapOf<String, String>()


                //Saving value of studentData from LiveData to studentMap
                studentData.value?.toMap(studentMap)
                groupData.value?.toMap(groupMap)

                if (studentMap["kelompok"] != null || studentMap["kelompok"] != "null") {
                    groupData.value?.toMap(groupMap)
                    //Save Group Mapping to SharedPreference
                    for ((key,value) in groupMap){
                        Log.i(key, value)
                        Preference(this).save(key,value)
                    }
                }

                //Saving Student Mapping to SharedPref
                for ((key, value) in studentMap) {
                    Log.i(key, value)
                    Preference(this).save(key, value)
                }

                var studentName = Preference(this).getPrefString("student_name")
                studentName?.showToast()

                startActivity(Intent(this, StudentContainer::class.java))

            } else {
                anim_loading.visibility = View.GONE
                "Username dan Password Salah".showToast()
            }
        })


    }

    private fun buttonLayoutBinding() {
        btnShowLoginMentor.setOnClickListener {
            lyt_login_mentor.visibility = View.VISIBLE
            lyt_login_mentor.animation = loadAnimation(this, R.anim.bottom_appear)
        }

        btnCloseLoginMentor.setOnClickListener {
            lyt_login_mentor.visibility = View.GONE
            lyt_login_mentor.animation = loadAnimation(this, R.anim.item_animation_gone_bottom)
        }
        btnShowLoginSiswa.setOnClickListener {
            lyt_login_student.visibility = View.VISIBLE
            lyt_login_student.animation = loadAnimation(this, R.anim.bottom_appear)
        }

        btnCloseLoginStudent.setOnClickListener {
            lyt_login_student.visibility = View.GONE
            lyt_login_student.animation = loadAnimation(this, R.anim.item_animation_gone_bottom)
        }

    }
}