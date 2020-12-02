package com.feylabs.tahfidz.View.BaseView

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

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

    fun downloadPicassoTeacher(target:ImageView) {
        //Setting Up URL
        val  url = URL.MENTOR_PHOTO + Preference(this).getPrefString("mentor_photo")
        Picasso.get()
            .load(url)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .fit()
            .placeholder(R.drawable.empty_profile)
            .error(R.drawable.empty_profile)
            .into(target, object : com.squareup.picasso.Callback {
                override fun onSuccess() {
                    Log.i("fan-url-teacher-photo",url)
                }

                override fun onError(e: java.lang.Exception?) {
                    Log.i("fan-url-teacher-photo",url)
                }
            })
    }

    fun View.showz(){
        this.visibility=View.VISIBLE
    }

    fun View.gonez(){
        this.visibility=View.GONE
    }

    fun cfAlert(message:String,bgColor:Int,textColor:Int) {
        val cfAlert = CFAlertDialog.Builder(this)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle(message)
            .setBackgroundColor(bgColor)
            .setTextColor(textColor)
            .setCancelable(true)
            .addButton(
                "OK", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.END
            ) { dialog, which ->
                dialog.dismiss()
            }
        cfAlert.show()
    }

    fun NoInternet(bgColor:Int,textColor:Int) {
        val cfAlert = CFAlertDialog.Builder(this)
            .setDialogStyle(CFAlertDialog.CFAlertStyle.NOTIFICATION)
            .setTitle("Koneksi Internet Tidak Ditemukan , Aplikasi Tidak Dapat Berjalan Tanpa Koneksi Internet, Silakan periksa koneksi internet anda dan coba lagi nanti")
            .setBackgroundColor(bgColor)
            .setTextColor(textColor)
            .setCancelable(true)
            .addButton(
                "Tutup Aplikasi", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE,
                CFAlertDialog.CFAlertActionAlignment.END
            ) { dialog, which ->
                dialog.dismiss()
                finishAffinity()
            }
        cfAlert.show()
    }

    fun isOnline(): Boolean {
        val context = this
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    val connectivityManager=getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val networkInfo=connectivityManager.activeNetworkInfo
                    return  networkInfo!=null && networkInfo.isConnected
                }
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
        }
        return false
    }
}