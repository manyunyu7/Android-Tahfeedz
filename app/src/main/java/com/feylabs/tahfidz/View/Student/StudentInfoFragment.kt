package com.feylabs.tahfidz.View.Student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crowdfire.cfalertdialog.CFAlertDialog
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.SharedView.UserChangeProfile
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.View.SharedView.MainActivity
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_student_info.*
import kotlinx.android.synthetic.main.layout_change_password.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentInfoFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    lateinit var studentViewModel: StudentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onPause() {
        super.onPause()
        Picasso.get().cancelTag("all-profile")
    }

    override fun onResume() {
        super.onResume()
        downloadPicasso(ivProfilePict)
        studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)
        studentViewModel.getStudentData(
            Preference(requireContext()).getPrefString("student_id").toString()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)
        return inflater.inflate(R.layout.fragment_student_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateLayout()

        //ADD PHOTO TO UI
        downloadPicasso(ivProfilePict)
        //Getting Student Data from ViewModel
        studentViewModel.getStudentData(
            Preference(requireContext()).getPrefString("student_id").toString()
        )

        btnCloseChangePassword.setOnClickListener {
            lyt_change_password.apply {
                animation = loadAnimation(requireContext(), R.anim.item_animation_gone_bottom)
                visibility = View.GONE
            }
        }

        btnChangePassword.setOnClickListener {
            lyt_change_password.apply {
                animation = loadAnimation(requireContext(), R.anim.bottom_appear)
                visibility = View.VISIBLE
            }
            btnSaveChangePassword.setOnClickListener {
                val oldPass = etOldPassword.text.toString()
                val newPass = etNewPassword.text.toString()
                val newPassConf = etNewPasswordConfirmation.text.toString()
                if (oldPass.isBlank() || newPass.isBlank() || newPassConf.isBlank()) {
                    "Lengkapi Bidang Yang Ada Terlebih Dahulu".showToast()
                } else {
                    if (newPass != newPassConf) {
                        etNewPassword.error = "Password Tidak Sesuai"
                        etNewPasswordConfirmation.error = "Password Tidak Sesuai"
                    } else {
                        anim_loading.visibility=View.VISIBLE
                        studentViewModel.changePassword(
                            Preference(requireContext()).getPrefString("student_id").toString(),
                            oldPass, newPass
                        )
                    }
                }
            }
        }

        studentViewModel.statusChangePassword.observe(viewLifecycleOwner, Observer {
            when (it) {
                3 -> {
                    //Do Nothing;
                }
                1 -> {
                    anim_loading.visibility=View.GONE
                    "Update Password Berhasil".showToast()
                    cfAlert(
                        "Berhasil Mengupdate Password",
                        R.color.alert_default_icon_color, R.color.colorWhite
                    )
                }
                2 -> {
                    anim_loading.visibility=View.GONE
                    "Update Password Gagal".showToast()
                    cfAlert(
                        "Gagal Mengupdate Password",
                        R.color.colorRedPastel, R.color.colorWhite
                    )
                }
            }
        })


        btnChangeProfile.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), UserChangeProfile::class.java))
        }

        btnRefreshProfile.setOnClickListener {
            studentViewModel.getStudentData(
                Preference(requireContext()).getPrefString("student_id").toString()
            )
        }

        studentViewModel.statusGetUpdated.observe(viewLifecycleOwner, Observer {
            if (it == 3) {
                anim_loading.visibility = View.VISIBLE
            }
            if (it == 1) {
                anim_loading.visibility = View.GONE
                val studentData = studentViewModel.getStudentData()
                val groupData = studentViewModel.getGroupData()
                val studentMap = mutableMapOf<String, String>()
                val groupMap = mutableMapOf<String, String>()

                //Saving value of studentData from LiveData to studentMap
                studentData.value?.toMap(studentMap)
                groupData.value?.toMap(groupMap)

                if (studentMap["kelompok"] != null || studentMap["kelompok"] != "null") {
                    groupData.value?.toMap(groupMap)
                    //Save Group Mapping to SharedPreference
                    for ((key, value) in groupMap) {
                        Log.i(key, value)
                        Preference(requireContext()).save(key, value)
                    }
                }
                //Saving Student Mapping to SharedPref
                for ((key, value) in studentMap) {
                    Log.i(key, value)
                    Preference(requireContext()).save(key, value)
                }
                tv_name.text = studentMap["student_name"]
                et_name.setText(studentMap["student_name"])
                et_nisn.text = studentMap["student_nisn"]
                et_email.setText(studentMap["student_email"])
                et_contact.setText(studentMap["student_contact"])
                tv_class.text = studentMap["student_class"]
            } else {
                anim_loading.visibility = View.GONE

            }
            updateLayout()
        })

        btnSaveChange.setOnClickListener {
            if (et_name.text.toString().length < 3) {
                et_name.requestFocus()
                et_name.error = "Mohon Isi Bidang Ini"
            } else {
                anim_loading.visibility = View.VISIBLE
                val name = et_name.text.toString()
                val contact = et_contact.text.toString()
                val email = et_email.text.toString()
                val id = Preference(requireContext()).getPrefString("student_id").toString()
                studentViewModel.updateData(
                    id, name, email, contact
                )
            }
        }


        studentViewModel.statusUpdateBasic.observe(viewLifecycleOwner, Observer {
            if (it!=3){
                anim_loading.visibility=View.GONE
            }
            if (it == 1) {
                "Berhasil Update Data".showToast()
                studentViewModel.getStudentData(
                    Preference(requireContext()).getPrefString("student_id").toString()
                )
            }
            if (it == 0) {
                "Gagal Mengupdate Data, Periksa Koneksi Internet Anda".showToast()
            }
        })




        btnLogout.setOnClickListener {
            val cfAlert = CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Anda Yakin Ingin Logout ??")
                .addButton(
                    "Logout",
                    -1,
                    -1,
                    CFAlertDialog.CFAlertActionStyle.POSITIVE,
                    CFAlertDialog.CFAlertActionAlignment.END
                ) { dialog, which ->
                    //Call Delete Submission Function
                    //Clear Preferences
                    Preference(requireContext()).clearPreferences()
                    //LOGOUT
                    requireActivity().finish()
                    requireContext().startActivity(
                        Intent(
                            requireContext(),
                            MainActivity::class.java
                        )
                    )
                    dialog.dismiss()
                }
                .addButton(
                    "BATAL", -1, -1, CFAlertDialog.CFAlertActionStyle.NEGATIVE,
                    CFAlertDialog.CFAlertActionAlignment.END
                ) { dialog, which ->
                    dialog.dismiss()
                }
            cfAlert.show()
        }
    }

    private fun updateLayout() {
        downloadPicasso(ivProfilePict)
        val pref = Preference(requireContext())
        tv_name.text = pref.getPrefString("student_name")
        et_name.setText(pref.getPrefString("student_name"))
        et_nisn.text = pref.getPrefString("student_nisn")
        et_email.setText(pref.getPrefString("student_email"))
        et_contact.setText(pref.getPrefString("student_contact"))
        tv_class.text = pref.getPrefString("student_class")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment StudentInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentInfoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}