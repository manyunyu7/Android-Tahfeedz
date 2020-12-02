package com.feylabs.tahfidz.View.Student

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.ViewModel.UploadViewModel
import com.github.squti.androidwaverecorder.WaveRecorder
import kotlinx.android.synthetic.main.fragment_student_submission.*
import kotlinx.android.synthetic.main.fragment_student_submission.anim_upload
import kotlinx.android.synthetic.main.layout_loading_upload.*
import java.io.File
import java.util.*

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
    private lateinit var waveRecorder :  WaveRecorder
    var handler = android.os.Handler()
    var timeWhenStopped: Long = 0 // for chronometer

    //Timer, substitute for deprecated handler
    private val timerOS = Timer()

    //PATH FOR SAVING FILE
    var path = ""

    var startSaved = ""
    var endSaved = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onPause() {
        super.onPause()
        timerOS.cancel()
        waveRecorder.stopRecording()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerOS.cancel()
        waveRecorder.stopRecording()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_submission, container, false)
    }

    override fun onResume() {
        super.onResume()
        initializeSeekBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Defining File Path Location
        path = context?.getExternalFilesDir(null)?.absolutePath + "/audio.wav"
        // Checking Permission
        checkPermission()
        //Mapping Surah From Base Fragment for Populate Spinner
        addSurat()
        //Getting Student Data from Shared Preference
        initID()
        //Initiating and Preparing UI Data and Layout
        initUI()
        //Initiating Controller For Local Plyaer
        musicController()
        //Check if there is recorded saved locallly
        checkIfLocalFileExist()
        //Media Player for Playing Recorder File
        mr = MediaRecorder()
        mp = MediaPlayer()
        //Initiating Wave Recorder
        waveRecorder = WaveRecorder(path)

        btnStop.visibility = View.VISIBLE
        btnStop.isEnabled = false


        //CREATE MEDIA DIRECTORY
        val mediaStorageDir = File(context?.externalCacheDir?.absolutePath, "Rekaman Tahfidz")
        Log.i("path ", context?.externalCacheDir?.absolutePath.toString())
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.i("Tahfidz", "failed to create directory")
            }else{
                Log.i("Tahfidz","Success Create Directory")
            }
        }else{
            Log.i("Tahfidz","Already Exists")
        }

        //START RECORDING
        btnStart.setOnClickListener {
            btnStart.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            btnStop.isEnabled = true
            statusRecord.text = "\nMerekam Suara"
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
            statusRecord.text = "\nRekaman Dijeda"
            waveRecorder.pauseRecording()

        }

        btnResume.setOnClickListener {
            btnResume.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            //ADJUSTING CHRONOMETER VALUE WITH TIME PASSED WHEN IN PAUSE STATE
            timer.base = SystemClock.elapsedRealtime() + timeWhenStopped
            timer.start()
            statusRecord.text = "\nMerekam Suara"
            waveRecorder.resumeRecording()
        }

        btnStop.setOnClickListener {
            waveRecorder.stopRecording()
            saveLastRecordedLocally()
            checkIfLocalFileExist()

            btnStart.visibility = View.VISIBLE
            timer.base = SystemClock.elapsedRealtime()
            timer.stop()
            statusRecord.text = getString(R.string.label_init_record)
            lastRecorderFileLabel.text = path


        }


    }

    private fun checkIfLocalFileExist() {
        if (File(path).exists()) {
            btnUpload.isEnabled = true
            label_record.text = Preference(requireContext()).getPrefString("student_last_record")
            btnUpload.setOnClickListener {
                uploadFile()
            }
            seekBarLocal.isEnabled = false
        } else {
            btnUpload.setOnClickListener {
                "Belum Ada File Rekaman"
            }
            seekBarLocal.isEnabled = false
            label_record.text = "Belum Ada Rekaman Terakhir"
        }
    }

    private fun musicController() {
        seekBarLocal.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) mp.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        btnPlayLastRecorder.setOnClickListener {
            initializeSeekBar()
            playSound(path)
        }
        btnStopLastRecorder.setOnClickListener {
            stopSound()
        }
    }

    private fun initUI() {
        val init = mutableListOf(1, 2, 3, 4, 5, 6, 7)
        spinnerAyahStart.setItems(init)
        spinnerAyahEnd.setItems(init)
        spinnerSurahStart?.setItems(s)
        spinnerSurahEnd?.setItems(s)
        val mapSurah = mutableMapOf<String, Int>()
        //Mapping Surah with each of it ayah count
        for (i in 0 until s.size) {
            mapSurah[s[i]] = a[i]
        }

        spinnerSurahStart.setOnItemSelectedListener { view, position, id, item ->
            val ayahSpecStart = mutableListOf<Int>()
            val ayahCount: Int = mapSurah[item.toString()] ?: 0
            for (i in 0 until ayahCount) {
                ayahSpecStart.add(i + 1)
            }
            spinnerAyahStart.setItems(ayahSpecStart)
        }

        spinnerSurahEnd.setOnItemSelectedListener { view, position, id, item ->
            val ayahSpecEnd = mutableListOf<Int>()
            val ayahCount: Int = mapSurah[item.toString()] ?: 0
            for (i in 0 until ayahCount) {
                ayahSpecEnd.add(i + 1)
            }
            spinnerAyahEnd.setItems(ayahSpecEnd)
        }
    }

    private fun saveLastRecordedLocally() {
        val startName = spinnerSurahStart.text.toString()
        val endName = spinnerSurahEnd.text.toString()
        //Saving The Recorded Surah to Shared Preference
        val ayahStart = spinnerAyahStart.selectedIndex + 1
        val ayahEnd = spinnerAyahEnd.selectedIndex + 1
        val startForPref = "$startName:$ayahStart"
        val endForPref = "$endName:$ayahEnd"
        Preference(requireContext()).save("student_last_record", "$startForPref - $endForPref")
    }

    private fun uploadFile() {
        val uploadViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            UploadViewModel::class.java
        )
        anim_upload.visibility = View.VISIBLE
        val surahStart = (spinnerSurahStart.selectedIndex + 1)
        val surahEnd = spinnerSurahEnd.selectedIndex + 1
        val ayahStart = spinnerAyahStart.selectedIndex + 1
        val ayahEnd = spinnerAyahEnd.selectedIndex + 1

        saveLastRecordedLocally()

        //Variables to Upload Recorded Surah to Server
        val start = "$surahStart:$ayahStart"
        val end = "$surahEnd:$ayahEnd"



        uploadViewModel.uploadFile(File(path), xstudent_id, xstudent_name, xgroup_id, start, end)
        uploadViewModel.uploadedListener.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                textUploadListener.text=it
            }else{
                textUploadListener.text="Loading"
            }
        })
        uploadViewModel.status.observe(viewLifecycleOwner, Observer {
            if (it) {
                anim_upload.visibility = View.GONE
                checkIfLocalFileExist()
                cfAlert(
                    "Berhasil Mengupload Setoran",
                    R.color.alert_default_icon_color, R.color.colorWhite
                )
            } else {
                anim_upload.visibility = View.GONE
                cfAlert(
                    "Gagal Mengupload Setoran",
                    R.color.alert_default_icon_color, R.color.colorWhite
                )
                checkIfLocalFileExist()
            }
        })
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

    private fun initializeSeekBar() {
        seekBarLocal.max = mp.duration

        try{
            timerOS.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    try {
                        seekBarLocal.progress = mp.currentPosition
                    } catch (e: Exception) {
                        seekBarLocal.progress = 0
                    }
                }
            }, 0, 1000)
        }catch (e : Exception){
            //Nothing To DO
        }

    }

    private fun playSound(path: String) {
        try {
            mp.reset()
            mp.setDataSource(path)
            mp.prepare()
            mp.start()
        } catch (e: Exception) {
            "Belum Ada Rekaman".showToast()
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