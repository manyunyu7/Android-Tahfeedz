package com.feylabs.tahfidz.View

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.View.Base.BaseFragment
import com.feylabs.tahfidz.ViewModel.SurahViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.fragment_student_submission.*
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentSubmissionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentSubmissionFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    lateinit var mr: MediaRecorder
    lateinit var mp: MediaPlayer
    var timeWhenStopped: Long = 0 // for chronometer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_submission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()             // CHECKING PERMISSION
        addSurat()

        mr = MediaRecorder()
        mp = MediaPlayer()


        spinnerSurahStart?.setItems(s)
        spinnerSurahEnd?.setItems(s)
        val mapSurah = mutableMapOf<String,Int>()

        //Mapping Surah with each of it ayah count
        for (i in - 0  until s.size){
            mapSurah[s[i]] = a[i]
        }

        spinnerSurahStart.setOnItemSelectedListener { view, position, id, item ->
            val ayahSpecStart = mutableListOf<Int>()
            val ayahCount : Int = mapSurah[item.toString()] ?:0
            for ( i in 0 until ayahCount){
                ayahSpecStart.add(i+1)
            }
            spinnerAyahStart.setItems(ayahSpecStart)
        }

        spinnerSurahEnd.setOnItemSelectedListener { view, position, id, item ->
            val ayahSpecEnd = mutableListOf<Int>()
            val ayahCount : Int = mapSurah[item.toString()] ?:0
            for ( i in 0 until ayahCount){
                ayahSpecEnd.add(i+1)
            }
            spinnerAyahEnd.setItems(ayahSpecEnd)
        }

        btnUpload.setOnClickListener {
            spinnerSurahStart.getItems<MutableList<String>>()[spinnerAyahStart.selectedIndex].toString().showToast()
        }






        btnStop.visibility = View.VISIBLE
        btnStop.isEnabled = false
        var path = context?.getExternalFilesDir(null)?.absolutePath + "/audio.wav"

        val waveRecorder = WaveRecorder(path)
//        if (!File(path).exists()) {
//            btnPlayLastRecorder.isEnabled = false
//            btnStopLastRecorder.isEnabled = false
//        }

        val mediaStorageDir = File(context?.externalCacheDir?.absolutePath, "Rekaman Tahfidz")
        Log.i("path ", context?.externalCacheDir?.absolutePath.toString())
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("App", "failed to create directory")
            }
        }

        //START RECORDING
        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            btnStop.isEnabled = true
            statusRecord.text = "Merekam Suara"
            waveRecorder.startRecording()
            timer.base = SystemClock.elapsedRealtime()
            timer.start()
        }

        btnPause.setOnClickListener {
            btnPause.visibility = View.GONE
            btnResume.visibility = View.VISIBLE
            timer.stop()

            //COUNTING THE TIME WHEN CHRONOMETER STOPPED THEN ADD IT TO THE timeWhenStopped
            timeWhenStopped = timer.base - SystemClock.elapsedRealtime()
            statusRecord.text = "Rekaman Dijeda"
            waveRecorder.pauseRecording()

        }

        btnResume.setOnClickListener {

            btnResume.visibility = View.GONE
            btnPause.visibility = View.VISIBLE

            //ADJUSTING CHRONOMETER VALUE WITH TIME PASSED WHEN IN PAUSE STATE
            timer.base = SystemClock.elapsedRealtime() + timeWhenStopped
            timer.start()

            statusRecord.text = "Merekam Suara"
            waveRecorder.resumeRecording()
        }

        btnStop.setOnClickListener {
            waveRecorder.stopRecording()
            btnStart.visibility = View.VISIBLE
            timer.base = SystemClock.elapsedRealtime()
            timer.stop()
            statusRecord.text = getString(R.string.label_init_record)
            lastRecorderFileLabel.text = path
        }

        btnPlayLastRecorder.setOnClickListener {
            playSound(path)
        }
        btnStopLastRecorder.setOnClickListener {
            stopSound()
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            btnStart.isEnabled = true
        }
    }

    private fun playSound(path:String) {
        try {
            mp.reset();
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            e.toString().showToast()
        }
    }

    private fun stopSound() {
        try {
            if (mp.isPlaying && mp != null) {
                mp.pause();
                mp.seekTo(0);
            } else {
                "Tidak Ada Audio Yang Sedang Dimainkan".showToast()
            }
        } catch (e: Exception) {
            e.toString().showToast()
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentSubmissionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentSubmissionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }


    }
}