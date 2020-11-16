package com.feylabs.tahfidz.View

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import com.tapadoo.alerter.Alert
import com.tapadoo.alerter.Alerter
import com.tapadoo.alerter.OnHideAlertListener
import kotlinx.android.synthetic.main.fragment_student_info.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentInfoFragment : Fragment() {
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

        //Getting Student Data from ViewModel
        studentViewModel.getStudentData(
            Preference(requireContext()).getPrefString("student_id").toString()
        )
        studentViewModel.getStudentData(
            Preference(requireContext()).getPrefString("student_id").toString()
        )
        studentViewModel.studentData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                tv_name.text = it["student_name"]
                et_name.setText(it["student_name"])

                if (it["student_class"].toString()=="null"){
                    tv_class.visibility=View.GONE
                }else{
                    tv_class.visibility=View.VISIBLE
                    tv_class.text = it["student_class"]
                }
                et_nisn.text = it["student_nisn"].toString()
                et_contact.setText(it["student_contact"].toString())
                if (it["student_email"].toString() == "null") {
                    et_email.setText("-")
                } else {
                    et_email.setText(it["student_email"])
                }

            } else {
                Alerter.clearCurrent(requireActivity())
                Alerter.create(requireActivity())
                    .setBackgroundColorRes(R.color.colorRedPastel)
                    .setIcon(R.drawable.ic_baseline_not_interested_24)
                    .setTitle("Koneksi Tidak Stabil , Coba Lagi Nanti")
                    .setOnHideListener(OnHideAlertListener {

                    })
                    .show()
            }
        })

        btnLogout.setOnClickListener {
            Alerter.create(requireActivity())
                .setBackgroundColorRes(R.color.colorRedPastel)
                .setIcon(R.drawable.ic_baseline_power_settings_new_24)
                .setTitle("Anda Yakin Ingin Logout Dari Aplikasi ?")
                .addButton("Logout", R.style.AlertButton, View.OnClickListener {
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
                    Alerter.hide()
                })
                .addButton("Kembali", R.style.AlertButton, View.OnClickListener {
                    Alerter.hide()
                })
                .show()

        }
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