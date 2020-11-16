package com.feylabs.tahfidz.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.Base.BaseActivity
import kotlinx.android.synthetic.main.activity_correction_detail.*
import kotlinx.android.synthetic.main.activity_detail_correction.correction_note

class CorrectionDetailActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correction_detail)

        val getParcelize = intent.getParcelableExtra<SubmissionModel>("data")
        val textCorrection = getParcelize?.correction.toString()
        var mp3URL = intent.getStringExtra("url").toString()

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


        //Checking if user student or teacher
        if (Preference(this).getPrefString("login_type") == "student") {
            tv_titleDetailCorrection.text = "Koreksi Hafalan"
            tv_descDetailCorrection.text =
                "Berikut adalah hasil koreksi hafalan yang sudah anda setorkan kepada pembimbing"
            btnSaveCorrection.visibility = View.GONE
        } else {
            tv_descDetailCorrection.text = "Masukkan Koreksi Dari Bacaan Quran yang dibacakan Siswa"
            tv_titleDetailCorrection.text = "Nilai Bacaan Siswa"
            btnSaveCorrection.visibility = View.VISIBLE
        }
        correction_note_webs.settings.javaScriptEnabled=true
        correction_note_webs.loadData(textCorrection, "text/html; charset=utf-8", "UTF-8")
        "Test".showToast()
    }
}