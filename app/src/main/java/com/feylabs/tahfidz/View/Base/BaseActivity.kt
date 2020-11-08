package com.feylabs.tahfidz.View.Base

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    public fun String.showToast(){
        Toast.makeText(this@BaseActivity,this,Toast.LENGTH_LONG).show()
    }
}