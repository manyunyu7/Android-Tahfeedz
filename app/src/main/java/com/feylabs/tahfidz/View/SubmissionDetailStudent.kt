package com.feylabs.tahfidz.View

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.jean.jcplayer.model.JcAudio
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.Base.BaseActivity
import com.feylabs.tahfidz.ViewModel.SubmissionViewModel
import com.tapadoo.alerter.Alerter
import kotlinx.android.synthetic.main.activity_submission_detail_student.*


class SubmissionDetailStudent : BaseActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_detail_student)

        val dataIntentParcel = intent.getParcelableExtra<SubmissionModel>("data") as SubmissionModel
        val submissionViewModel = ViewModelProvider(this,ViewModelProvider.NewInstanceFactory())
            .get(SubmissionViewModel::class.java)


        val mp3Pathz = dataIntentParcel.audio
        val mp3Path = mp3Pathz.replaceFirst(".", "");
        Log.d("audioPath",mp3Path)

        sub_det_id.text="#SE"+dataIntentParcel.id
        sub_det_date.text=dataIntentParcel.date
        sub_start_end.text=dataIntentParcel.start +"-"+dataIntentParcel.end
        sub_det_status.text=dataIntentParcel.status

        val title = intent.getStringExtra("title")
        val jcAudios: ArrayList<JcAudio> = ArrayList()
        jcAudios.add(JcAudio.createFromURL("$title", URL.MP3 + mp3Path));
        jcplayer.initPlaylist(jcAudios, null);
        textSource.text = URL.MP3 + mp3Path

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
        jcplayer.kill()
    }
}