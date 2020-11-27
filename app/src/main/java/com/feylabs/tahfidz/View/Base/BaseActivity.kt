package com.feylabs.tahfidz.View.Base

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_student_info.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*

open class BaseActivity : AppCompatActivity() {

    fun String.showToast(){
        Toast.makeText(this@BaseActivity,this,Toast.LENGTH_LONG).show()
    }
    fun downloadPicasso(target:ImageView) {
        //Setting Up URL
        val  url = URL.STUDENT_PHOTO + Preference(this).getPrefString("student_photo")
        Picasso.get()
            .load(url)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.empty_profile)
            .error(R.drawable.empty_profile)
            .into(target, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("fan-url-profile-info",url)
                }
            })
    }
}