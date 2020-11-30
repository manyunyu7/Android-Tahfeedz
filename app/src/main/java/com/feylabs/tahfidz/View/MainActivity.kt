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
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.feylabs.tahfidz.View.Teacher.MentorLanding
import com.feylabs.tahfidz.ViewModel.MentorViewModel
import com.feylabs.tahfidz.ViewModel.MotivationViewModel
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_login_student.*
import kotlinx.android.synthetic.main.layout_login_ustadz.*

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        if (Preference(this).getPrefString("student_id") != null) {
            startActivity(Intent(this, StudentContainer::class.java))
        }
        if (Preference(this).getPrefString("login_type") == "mentor") {
            startActivity(Intent(this, MentorLanding::class.java))
        }
        buttonLayoutBinding()


        var studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)

        var mentorViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MentorViewModel::class.java)

        val motViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MotivationViewModel::class.java)

        btnLoginMentor.setOnClickListener {
            val usr = etUsernameMentor.text.toString()
            val pass = etPasswordMentor.text.toString()
            if (usr.isEmpty() || pass.isEmpty()) {
                "Mohon Isi Username dan Password Terlebih Dahulu".showToast()
            } else {
                mentorViewModel.loginMentor(usr, pass)
                anim_loading.visibility=View.VISIBLE
            }
        }

        btnLoginStudent.setOnClickListener {
            val usr = etUsernameStudent.text.toString()
            val pass = etPasswordStudent.text.toString()
            if (usr.isEmpty() || pass.isEmpty()) {
                "Mohon Isi Username dan Password Terlebih Dahulu".showToast()
            } else {
                studentViewModel.loginStudent(usr, pass)
                anim_loading.visibility = View.VISIBLE
            }
        }
        mentorViewModel.status.observe(this, Observer {
            if (it["code"] == "200") {
                val mentorMap = mentorViewModel.getMentorData()
                for ((key, value) in mentorMap) {
                    Log.i("-prefM$key", value)
                    Preference(this).save(key, value)
                }
                finish()
                startActivity(Intent(this, MentorLanding::class.java))
            } else {
                it["status"].toString().showToast()
            }
        })

        studentViewModel.status.observe(this, Observer {
            if (it) {
                anim_loading.visibility = View.GONE
                val studentData = studentViewModel.getStudentData()
                val groupData = studentViewModel.getGroupData()
                val studentMap = mutableMapOf<String, String>()
                val groupMap = mutableMapOf<String, String>()

                //Saving value of studentData from LiveData to studentMap
                studentData.value?.toMap(studentMap)
                groupData.value?.toMap(groupMap)

                if (studentMap["kelompok"] != null || studentMap["kelompok"] != "null") {
                    groupData.value?.toMap(groupMap)
                    //Save Group Mapping to SharedPreference
                    for ((key, value) in groupMap) {
                        Log.i(key, value)
                        Preference(this).save(key, value)
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