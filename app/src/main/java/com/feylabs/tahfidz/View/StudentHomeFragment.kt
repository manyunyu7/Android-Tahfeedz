package com.feylabs.tahfidz.View

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.ViewModel.StudentLoginViewModel
import kotlinx.android.synthetic.main.fragment_student_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentHomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        student_name.text=Preference(requireContext()).getPrefString("student_name")
        student_nisn.text=Preference(requireContext()).getPrefString("student_nisn")

        if (Preference(requireContext()).sharedPref.contains("id_group")){
            studentGroup.text="${Preference(requireContext()).getPrefString("group_name")}"
            studentMentor.text=Preference(requireContext()).getPrefString("group_mentor_name")
            student_mentor_contact.text="${Preference(requireContext()).getPrefString("group_mentor_contact")}"
        }else{
            studentGroup.text="${getString(R.string.group)} :\nAnda Belum Terdaftar Di Kelompok"
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentStudentHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StudentHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}