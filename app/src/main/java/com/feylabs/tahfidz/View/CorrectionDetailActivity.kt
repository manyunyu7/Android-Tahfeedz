package com.feylabs.tahfidz.View

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.Base.BaseActivity
import com.feylabs.tahfidz.ViewModel.CorrectionViewModel
import kotlinx.android.synthetic.main.activity_correction_detail.*

class CorrectionDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correction_detail)

        val getParcelize = intent.getParcelableExtra<SubmissionModel>("data")
        val textCorrection = getParcelize?.correction.toString()
        var mp3URL = intent.getStringExtra("url").toString()
        var idSubmission = getParcelize?.id

        tv_titleDetailCorrection.setOnClickListener {
            finish()
            onBackPressed()
        }

        //SETTING UP MP3========================================================================
        correctionMp3.settings.javaScriptEnabled = true
        correctionMp3.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
        correctionMp3.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(mp3URL)
                return true
            }
        }
        correctionMp3.loadUrl(mp3URL)
        Log.i("MP3View URL", mp3URL)

        correction_note_student.settings.javaScriptEnabled = true
        //Checking if user student or teacher
        if (Preference(this).getPrefString("login_type") == "student") {
            tv_titleDetailCorrection.text = "Koreksi Hafalan"
            tv_descDetailCorrection.text =
                "Berikut adalah hasil koreksi hafalan yang sudah anda setorkan kepada pembimbing"
            btnRefresh.visibility = View.GONE

            var url_sub_id = URL.SHOW_CORRECTION+getParcelize?.id?.removeRange(0,3)
            correction_note_student.loadUrl(url_sub_id)
            correction_note_student.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    view?.loadUrl(url_sub_id)
                    return true
                }
            }
            Log.i("url",  url_sub_id)

        } else {
            anim_loading.visibility=View.VISIBLE
            cardMP3.visibility=View.GONE
            tv_descDetailCorrection.text = "Masukkan Koreksi Dari Bacaan Quran yang dibacakan Siswa"
            tv_titleDetailCorrection.text = "Nilai Bacaan Siswa"
            btnRefresh.visibility = View.VISIBLE
            var url_sub_id = URL.INPUT_CORRECTION+getParcelize?.id?.removeRange(0,3)

            correction_note_student.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    anim_loading.visibility=View.VISIBLE
                    view?.loadUrl(url_sub_id)
                    return true
                }
                override fun onPageFinished(view: WebView?, url: String?) {
                    anim_loading.visibility=View.GONE
                    }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    anim_loading.visibility=View.VISIBLE
                }
            }
            btnRefresh.setOnClickListener {
                correction_note_student.loadUrl(url_sub_id)
            }
        }

        val correctionViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(CorrectionViewModel::class.java)



    }
}