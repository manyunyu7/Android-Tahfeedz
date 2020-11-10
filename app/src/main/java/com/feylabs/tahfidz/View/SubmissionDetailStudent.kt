package com.feylabs.tahfidz.View

import android.os.Bundle
import com.example.jean.jcplayer.model.JcAudio
import com.feylabs.tahfidz.Model.SubmissionModel
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.URL
import com.feylabs.tahfidz.View.Base.BaseActivity
import kotlinx.android.synthetic.main.activity_submission_detail_student.*


class SubmissionDetailStudent : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_detail_student)

        val dataIntentParcel = intent.getParcelableExtra<SubmissionModel>("data") as SubmissionModel

        var mp3Pathz = dataIntentParcel.audio
        var mp3Path = mp3Pathz.replaceFirst(".","");
        "$mp3Path".showToast()

        var title = intent.getStringExtra("title")
        val jcAudios: ArrayList<JcAudio> = ArrayList()
        jcAudios.add(JcAudio.createFromURL("$title", URL.MP3+mp3Path));
        jcplayer.initPlaylist(jcAudios, null);
        textSource.setText(URL.MP3+mp3Path)


    }
}