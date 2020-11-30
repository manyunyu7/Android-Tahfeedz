package com.feylabs.tahfidz.View.Teacher

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.SharedView.UserChangeProfile
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.feylabs.tahfidz.View.MainActivity
import com.feylabs.tahfidz.ViewModel.MentorViewModel
import kotlinx.android.synthetic.main.activity_mentor_profile.*
import kotlinx.android.synthetic.main.layout_change_password.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*

class MentorProfile : BaseActivity() {

    lateinit var mentorViewModel: MentorViewModel
    var mentorMap = mutableMapOf<String, String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_profile)
        downloadPicassoTeacher(ivProfilePict)
        anim_loading.showz()
        mentorViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MentorViewModel::class.java)

        mentorViewModel.getUpdatedData(
            Preference(this).getPrefString("mentor_id").toString(),
            this
        )

        btnCloseChangePassword.setOnClickListener {
            lyt_change_password.apply {
                animation = AnimationUtils.loadAnimation(
                    this@MentorProfile,
                    R.anim.item_animation_gone_bottom
                )
                visibility = View.GONE
            }
        }

        btnSaveChange.setOnClickListener {
            if (et_name.text.toString().length < 3) {
                et_name.requestFocus()
                et_name.error = "Mohon Isi Bidang Ini"
            } else {
                anim_loading.visibility = View.VISIBLE
                val name = et_name.text.toString()
                val contact = et_contact.text.toString()
                val email = et_email.text.toString()
                val id = Preference(this).getPrefString("mentor_id").toString()

                mentorViewModel.updateData(
                    id, name, email, contact
                )
            }
        }

        btnChangeProfile.setOnClickListener {
            val intent = Intent(this,UserChangeProfile::class.java)
                intent.putExtra("mentor",true)
            startActivity(intent)
        }

        btnRefreshProfile.setOnClickListener{
            mentorViewModel.getUpdatedData(
                Preference(this).getPrefString("mentor_id").toString(),
                this
            )
            downloadPicassoTeacher(ivProfilePict)
        }

        mentorViewModel.statusUpdateBasic.observe(this, Observer {
            if (it == 1) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Berhasil Update Data",
                    R.color.alert_default_icon_color, R.color.colorWhite
                )
            }
            if (it == 0) {
                anim_loading.visibility = View.GONE
                cfAlert(
                    "Gagal Mengupdate Data, Periksa Koneksi Internet Anda",
                    R.color.alert_default_icon_color, R.color.colorWhite
                )
            }
        })


        btnChangePassword.setOnClickListener {
            lyt_change_password.apply {
                animation = AnimationUtils.loadAnimation(this@MentorProfile, R.anim.bottom_appear)
                visibility = View.VISIBLE
            }
        }

        mentorViewModel.statusGetUpdated.observe(this, Observer {
            updateLayout()
            if (it == 3) {
                anim_loading.showz()
            }
            if (it == 1) {
                anim_loading.gonez()
            }
            if (it == 2 || it == 0) {
                anim_loading.gonez()
                "Gagal Mengambil Data Terbaru Dari Server".showToast()
            }
        })





        btnLogout.setOnClickListener {
            val cfAlert = CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Anda Yakin Ingin Logout ??")
                .addButton(
                    "Logout",
                    -1,
                    -1,
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.END
                ) { dialog, which ->
                    //Call Delete Submission Function
                    //Clear Preferences
                    Preference(this).clearPreferences()
                    //LOGOUT
                   finish()
                  startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    dialog.dismiss()
                }
                .addButton(
                    "BATAL", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.END
                ) { dialog, which ->
                    dialog.dismiss()
                }
            cfAlert.show()
        }
    }

//    private fun updateListener() {
//        mentorViewModel.getUpdatedData(
//            Preference(this).getPrefString("student_id").toString(),
//            this)
//        mentorViewModel.statusGetUpdated.observe(this, Observer {
//            if (it == 3) {
//                anim_loading.visibility = View.VISIBLE
//            }
//            if (it == 1) {
//                anim_loading.visibility = View.GONE
//                val mentorMap = mentorViewModel.getMentorData()
//                for ((key, value) in mentorMap) {
//                    Log.i("-prefM$key", value)
//                    Preference(this).save(key, value)
//                }
//            } else {
//                anim_loading.visibility = View.GONE
//            }
//
//            updateLayout()
//            tv_name.setText(mentorMap["mentor_name"])
//            et_name.setText(mentorMap["mentor_name"])
//            et_contact.setText(mentorMap["mentor_contact"])
//            et_email.setText(mentorMap["mentor_email"])
//        })
//    }

    private fun updateLayout() {
        val pref = Preference(this)
        tv_name.text = pref.getPrefString("mentor_name")
        et_name.setText(pref.getPrefString("mentor_name"))
        et_contact.setText(pref.getPrefString("mentor_contact"))
        et_email.setText(pref.getPrefString("mentor_email"))
    }
}