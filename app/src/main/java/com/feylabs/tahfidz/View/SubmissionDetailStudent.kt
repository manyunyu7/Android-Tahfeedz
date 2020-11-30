package com.feylabs.tahfidz.View

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.webkit.DownloadListener
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.feylabs.tahfidz.ViewModel.SubmissionViewModel
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_submission_detail_student.*


class SubmissionDetailStudent : BaseActivity()  {
    lateinit var mp : MediaPlayer
    var currentStat = "null"
    var timeWhenStopped: Long = 0 // for chronometer


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_detail_student)

        val dataIntentParcel = intent.getParcelableExtra<SubmissionModel>("data") as SubmissionModel
        var mp3Path = dataIntentParcel.audio
        mp3Path = mp3Path.replaceFirst(".", "")
        mp3Path = mp3Path.replaceFirst("/", "")
        mp3Path = mp3Path.replace(" ", "%20")
        Log.d("audioPath",mp3Path)

        mp3View.settings.javaScriptEnabled = true
        mp3View.setDownloadListener(object : DownloadListener {
            override fun onDownloadStart(
                url: String?, userAgent: String?,
                contentDisposition: String?, mimetype: String?,
                contentLength: Long
            ) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        })
        mp3View.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(URL.MP3_MOBILE+mp3Path)
                return true
            }
        }
        mp3View.loadUrl(URL.MP3_MOBILE+mp3Path)
        Log.i("MP3View URL",URL.MP3_MOBILE+mp3Path)

        //init mpPlayer
        mp=MediaPlayer()


        val submissionViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())
            .get(SubmissionViewModel::class.java)




        sub_det_id.text="#SE"+dataIntentParcel.id
        sub_det_date.text=dataIntentParcel.date
        sub_start_end.text=dataIntentParcel.start +"-"+dataIntentParcel.end
        sub_det_status.text=dataIntentParcel.status

        val title = intent.getStringExtra("title")
        textSource.text = URL.MP3 + mp3Path

        btnStart.setOnClickListener {
            if (currentStat=="null"){
                playSound(URL.MP3+mp3Path)
                playState()
            }else{
                resumeSound()
            }
        }

        btnPause.setOnClickListener {
            pauseState()
        }

        btnStop.setOnClickListener {
            stopSound()
        }



        btnConfDeleteSubmission.setOnClickListener {
            Alerter.create(this)
                .setTitle("Anda Yakin Ingin Menghapus Setoran Ini ?")
                .setText("Setoran yang sudah dihapus tidak dapat dikembalikan lagi")
                .addButton("HAPUS", R.style.AlertButton, View.OnClickListener {
                    submissionViewModel.deleteSubmission(dataIntentParcel.id)
                    Alerter.clearCurrent(this)
                    Alerter.create(this)
                        .setBackgroundColorInt(R.color.colorBlueWaves)
                        .setTitle("Menghapus Setoran")
                        .setText("Menghapus Data, Mohon Tunggu Sebentar")
                        .enableProgress(true)
                        .setProgressColorRes(R.color.colorWhite)
                        .show()


                    submissionViewModel.statusDelete.observe(this, Observer {
                        if(it){
                            Alerter.hide()
                            "Berhasil Menghapus Setoran".showToast()
                            finish()
                            super.onBackPressed()
                        }else{
                            Alerter.hide()
                            "Gagal Menghapus Setoran, Hubungi Admin untuk menghapus setoran".showToast()
                        }
                    })
                })
                .addButton("BATALKAN", R.style.AlertButton, View.OnClickListener {
                    Alerter.hide()
                })
                .show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mp.stop()
    }


    private fun playState() {
        currentStat = "playing"
        btnStart.visibility=View.GONE
        btnPause.visibility=View.VISIBLE
        btnStop.isEnabled = true
        statusPlay.text = "\nMemutar Audio"
        timer.base = SystemClock.elapsedRealtime()
        timer.start()
    }

    private fun pauseState(){
        currentStat = "paused"
        btnPause.visibility = View.GONE
        btnStart.visibility = View.VISIBLE
        timer.stop()
        //COUNTING THE TIME WHEN CHRONOMETER STOPPED THEN ADD IT TO THE timeWhenStopped
        timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
        statusPlay.text = "\nRekaman Dijeda"

        //PAUSE MEDIA PLAYER
        pauseSound(timeWhenStopped.toInt())
    }


    private fun playSound(path: String) {
        try {
            mp.reset()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
            "Played".showToast()
        } catch (e: Exception) {
            e.toString().showToast()
            Log.e("ErrorStream",e.toString())
        }
    }

    private fun resumeSound() {
        try {
            timer.base = timeWhenStopped
            timer.start()
            btnPause.visibility=View.VISIBLE
            btnStart.visibility=View.GONE
            mp.seekTo(timeWhenStopped.toInt())
            mp.start()
            "Played".showToast()
        } catch (e: Exception) {
            e.toString().showToast()
            Log.e("ErrorStream",e.toString())
        }
    }

    private fun pauseSound(timePaused : Int) {
        try {
            if (mp.isPlaying && mp != null) {
                mp.pause()
                mp.seekTo(timePaused)
            } else {
                "Tidak Ada Audio Yang Sedang Dimainkan".showToast()
            }
        } catch (e: Exception) {
            e.toString().showToast()
        }
    }

    private fun stopSound() {
        try {
            if (mp.isPlaying && mp != null) {
                mp.pause()
                mp.seekTo(0)
            } else {
                "Tidak Ada Audio Yang Sedang Dimainkan".showToast()
            }
        } catch (e: Exception) {
            e.toString().showToast()
        }
    }

}