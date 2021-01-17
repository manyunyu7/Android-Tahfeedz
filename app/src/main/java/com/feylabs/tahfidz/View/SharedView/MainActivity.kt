package com.feylabs.tahfidz.View.SharedView

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.AdminAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.feylabs.tahfidz.View.Student.StudentContainer
import com.feylabs.tahfidz.View.Teacher.MentorLanding
import com.feylabs.tahfidz.View.Teacher.TeacherDashboard
import com.feylabs.tahfidz.ViewModel.AdminViewModel
import com.feylabs.tahfidz.ViewModel.MentorViewModel
import com.feylabs.tahfidz.ViewModel.MotivationViewModel
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_contact_admin.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_login_student.*
import kotlinx.android.synthetic.main.layout_login_ustadz.*

class MainActivity : BaseActivity() {

    override fun onResume() {
        super.onResume()
        if (Preference(this).getPrefString("student_id") != null) {
            startActivity(Intent(this, StudentContainer::class.java))
        }
        if (Preference(this).getPrefString("login_type") == "mentor") {
            startActivity(Intent(this, MentorLanding::class.java))
        }
        adminViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(AdminViewModel::class.java)
        adminViewModel.getAdmin()
    }

    lateinit var adminViewModel: AdminViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Preference(this).getPrefString("student_id") != null) {
            finish()
            startActivity(Intent(this, StudentContainer::class.java))
        }
        if (Preference(this).getPrefString("login_type") == "mentor") {
            finish()
            startActivity(Intent(this, MentorLanding::class.java))
        }
        buttonLayoutBinding()


        val studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)

        val mentorViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MentorViewModel::class.java)

        adminViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(AdminViewModel::class.java)

        adminViewModel.getAdmin()
        adminViewModel.statusGetAdmin.observe(this, Observer {
            if (it==3){
                anim_loading.visibility=View.VISIBLE
            }else{
                anim_loading.visibility=View.GONE
            }

            if (it==1){
                //Do Nothing
            }

            //If Internet is Not Available
            if (it==0){
                cfAlert(
                    "Koneksi Internet Tidak Ditemukan , Periksa Koneksi Internet Anda atau Coba Lagi Nanti"
                    , 0, -1
                )
            }
        })

        val adminAdapter = AdminAdapter()
        adminViewModel.dataAdmin.observe(this, Observer {
            if (it!=null){
                adminAdapter.setData(it)
                rv_admin.setHasFixedSize(true)
                rv_admin.layoutManager=LinearLayoutManager(this)
                rv_admin.adapter=adminAdapter
            }else{
                "Tidak Ada Data Admin".showToast()
            }
        })

        btnLoginMentor.setOnClickListener {
            val usr = etUsernameMentor.text.toString()
            val pass = etPasswordMentor.text.toString()
            if (usr.isEmpty() || pass.isEmpty()) {
                "Mohon Isi Username dan Password Terlebih Dahulu".showToast()
            } else {
                mentorViewModel.loginMentor(usr, pass)
                anim_loading.visibility = View.VISIBLE
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
        mentorViewModel.loginStatus.observe(this, Observer {
            if (it == 3) {
                anim_loading.visibility = View.VISIBLE
            } else {
                anim_loading.visibility = View.GONE
            }

            if (it == 1) {
                val mentorMap = mentorViewModel.getMentorData()
                for ((key, value) in mentorMap) {
                    Log.i("-prefM$key", value)
                    Preference(this).save(key, value)
                }
                finish()
                startActivity(Intent(this, MentorLanding::class.java))
            }

            if (it == 0) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Gagal Terhubung Dengan Server , Periksa Koneksi Internet Anda atau Coba Lagi Nanti"
                    , 0, -1
                )
            }

            if (it == 2) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Username atau Password Salah"
                    , 0, -1
                )
            }
        })

        studentViewModel.studentData.observe(this, Observer {
            for ((key, value) in it) {
                Log.i("data-student="+key, value)
                Preference(this).save(key, value)
            }
        })

        studentViewModel.groupData.observe(this, Observer {
            for ((key, value) in it) {
                Log.i("dataGroup=$key", value)
                Preference(this).save(key, value)
            }
        })

        studentViewModel.statusLogin.observe(this, Observer {
            if (it == 3) {
                anim_loading.visibility = View.VISIBLE
            }
            if (it == 2) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Username atau Password Salah"
                    , 0, -1
                )
            }
            if (it == 1) {
                "Login Berhasil".showToast()
                anim_loading.visibility = View.GONE
                val studentData = studentViewModel.getStudentData()
                val groupData = studentViewModel.getGroupData()
                val studentMap = mutableMapOf<String, String>()
                val groupMap = mutableMapOf<String, String>()

                //Saving value of studentData from LiveData to studentMap
                studentData.value?.toMap(studentMap)
                groupData.value?.toMap(groupMap)

                Log.i("StudentData", studentData.toString())
                Log.i("StudentMap", studentMap.toString())


                var studentName = Preference(this).getPrefString("student_name")
                studentName?.showToast()

                finish()
                startActivity(Intent(this, StudentContainer::class.java))
            }

            if (it == 0) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Gagal Terhubung Dengan Server , Periksa Koneksi Internet Anda atau Coba Lagi Nanti"
                    , 0, -1
                )
            }

        })


    }

    private fun buttonLayoutBinding() {
        btnShowLoginMentor.setOnClickListener {
            startActivity(
                Intent(this, TeacherDashboard::class.java)
            )
//            lyt_login_mentor.visibility = View.VISIBLE
//            lyt_login_mentor.animation = loadAnimation(this, R.anim.bottom_appear)
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

        btnForgotPassword.setOnClickListener {
            adminViewModel.getAdmin()
            lyt_contact_admin.visibility=View.VISIBLE
            lyt_contact_admin.animation= loadAnimation(this,R.anim.bottom_appear)
        }

        btnCloseAdmin.setOnClickListener {
            lyt_contact_admin.visibility=View.GONE
            lyt_contact_admin.animation= loadAnimation(this,R.anim.item_animation_gone_bottom)
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}