package com.feylabs.tahfidz.View.Base

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.feylabs.tahfidz.R
import dm.audiostreamer.AudioStreamingManager
import dm.audiostreamer.CurrentSessionCallback
import dm.audiostreamer.MediaMetaData
import kotlinx.android.synthetic.main.activity_music.*


class MusicActivity : AppCompatActivity(), CurrentSessionCallback {
    private lateinit var streamingManager : AudioStreamingManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        val context = this@MusicActivity
        checkPermission()
        streamingManager = AudioStreamingManager.getInstance(context)
        streamingManager.isPlayMultiple = false

        val obk = MediaMetaData()
        obk.mediaUrl="http://202.157.177.52/API-Tahfidz/submission/file/1605339733-A.%20Adibah%20Ibtisam%20Abbas.wav"
        obk.mediaTitle="Title"


        btnPlay.setOnClickListener {
            streamingManager.onPlay(obk)
        }
        btnPause.setOnClickListener {
            streamingManager.onPause()
        }
    }

    fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
               this,
                android.Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )
    }



    public override fun onStart() {
        super.onStart()
        if (streamingManager != null) {
            streamingManager.subscribesCallBack(this)
        }
    }

    public override fun onStop() {
        super.onStop()
        if (streamingManager != null) {
            streamingManager.unSubscribeCallBack()
        }
    }

    override fun updatePlaybackState(state: Int) {
        when (state) {
            PlaybackStateCompat.STATE_PLAYING -> {

            }
            PlaybackStateCompat.STATE_PAUSED -> {

            }
            PlaybackStateCompat.STATE_NONE -> {

            }
            PlaybackStateCompat.STATE_STOPPED -> {

            }
            PlaybackStateCompat.STATE_BUFFERING -> {

            }
        }
    }

    override fun playSongComplete() {}
    override fun currentSeekBarPosition(progress: Int) {}
    override fun playCurrent(indexP: Int, currentAudio: MediaMetaData?) {}
    override fun playNext(indexP: Int, CurrentAudio: MediaMetaData?) {}
    override fun playPrevious(indexP: Int, currentAudio: MediaMetaData?) {}


}