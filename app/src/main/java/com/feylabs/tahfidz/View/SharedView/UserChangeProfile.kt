package com.feylabs.tahfidz.View.SharedView

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.API_Endpoint
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_user_change_profile.*
import kotlinx.android.synthetic.main.activity_user_change_profile.btnChangeProfile
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import org.json.JSONObject
import java.io.File


class UserChangeProfile : BaseActivity() {
    var imageFile: File? = null
    var isMentor = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_change_profile)

        isMentor = intent.getBooleanExtra("mentor", false)

        if (isMentor) {
            downloadPicassoTeacher(ivProfilePicChange)
        } else {
            downloadPicasso(ivProfilePicChange)
        }


        btnChangeProfile.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 22)
                checkAndRequestPermission()
            else openGallery()
        }

        btnSaveChangeProfile.setOnClickListener {
            if (imageFile == null) {
                makeToast("Anda Belum Melakukan Perubahan")
            } else {
                if (isMentor) {
                    updateProfilePicMentor()
                } else {
                    updateProfilePic()
                }

            }
        }


        btnBack.setOnClickListener {
            super.onBackPressed()
            finish()
        }
    }


    private fun openGallery() {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this@UserChangeProfile)
    }

    fun makeToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun updateProfilePicMentor() {
        anim_loading.visibility = View.VISIBLE
        AndroidNetworking.upload(API_Endpoint.MENTOR_UPDATE_IMG)
            .addMultipartFile("uploaded_files", imageFile)
            .addMultipartParameter("mentor_id", Preference(this).getPrefString("mentor_id"))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    anim_loading.visibility = View.GONE
                    if (response.getInt("response_code") == 1) {
                        Log.i("FAN-prof", response.toString())
                        cfAlert(
                            "Berhasil Mengganti Foto Profile",
                            R.color.alert_default_icon_color, R.color.colorWhite
                        )
                        makeToast("Berhasil Mengganti Foto Profile")
                    } else {
                        makeToast("Gagal Mengganti Foto Profile")
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("FAN-prof", anError.toString())
                    anim_loading.visibility = View.GONE
                    failed()
                    makeToast("Gagal Mengganti Foto Profil")
                }
            })
    }

    fun updateProfilePic() {
        anim_loading.visibility = View.VISIBLE
        AndroidNetworking.upload(API_Endpoint.STUDENT_UPDATE_IMG)
            .addMultipartFile("uploaded_files", imageFile)
            .addMultipartParameter("student_id", Preference(this).getPrefString("student_id"))
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    anim_loading.visibility = View.GONE
                    if (response.getInt("response_code") == 1) {
                        Log.i("FAN-prof", response.toString())
                        cfAlert(
                            "Berhasil Mengganti Foto Profile",
                            R.color.alert_default_icon_color, R.color.colorWhite
                        )
                        makeToast("Berhasil Mengganti Foto Profile")
                    } else {
                        makeToast("Gagal Mengganti Foto Profile Karena")
                    }
                }

                override fun onError(anError: ANError) {
                    Log.i("FAN-prof", anError.toString())
                    onBackPressed()
                    anim_loading.visibility = View.GONE
                    failed()
                    makeToast("Gagal Mengganti Foto Profil")
                }
            })
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                ivProfilePicChange.setImageURI(resultUri)
                imageFile = File(resultUri.path)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                makeToast("Accept All Permission Request")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else openGallery()
    }


    private fun failed() {
        val cfAlert = CFAlertDialog.Builder(this)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle("Gagal Mengupdate Profile")
            .setCancelable(true)
            .addButton(
                "OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.END
            ) { dialog, which ->
                dialog.dismiss()
            }
        cfAlert.show()
    }

}