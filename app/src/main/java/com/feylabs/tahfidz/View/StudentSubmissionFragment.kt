package com.feylabs.tahfidz.View

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.View.Base.BaseFragment
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

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        )
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    android.Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                111
            )

        mr = MediaRecorder()
        mp = MediaPlayer()
        var path = context?.getExternalFilesDir(null)?.absolutePath + "/audio.mpeg4"


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

        btnStop.visibility=View.GONE

        btnPlayLastRecorder.setOnClickListener {
            playSound(path)
        }

        btnStopLastRecorder.setOnClickListener {
            mp.stop()
        }


        //START RECORDING
        btnStart.setOnClickListener {
            btnStart.visibility=View.GONE
            btnStop.visibility = View.VISIBLE

            mr.setAudioSource(MediaRecorder.AudioSource.MIC)
            mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            mr.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4)
            mr.setOutputFile(path)
            mr.prepare()
            mr.start()

            timer.base = SystemClock.elapsedRealtime()
            timer.start()
        }

        btnStop.setOnClickListener {
            mr.stop()

            btnStop.visibility = View.GONE
            btnStart.visibility = View.VISIBLE
            
            timer.stop()
            var a = File(path)
            a.length().toString().showToast()
            lastRecorderFileLabel.text = path
        }

        btnPlay.setOnClickListener {
            playSound(path)
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

        fun playSound(path : String){
            var mp = MediaPlayer()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        }
    }
}