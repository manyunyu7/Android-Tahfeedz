package com.feylabs.tahfidz.View.Student

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.MotAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.View.QuranModulesViews.ListSurahActivity
import com.feylabs.tahfidz.ViewModel.MotivationViewModel
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_student_home.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [StudentHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StudentHomeFragment : BaseFragment() {
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
        Picasso.get().cancelTag("all")
    }

    override fun onResume() {
        super.onResume()
        updatingStudentGroupDataUI()
        studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)
        studentViewModel.getStudentData(
            Preference(requireContext()).getPrefString("student_id").toString()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerMotCardStudentInfo.startShimmer()
        menuQuranCard.setOnClickListener {
            startActivity(Intent(requireContext(),ListSurahActivity::class.java))
        }
        downloadPicasso(profile_pic)
        studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)
        studentViewModel.statusGetUpdated.observe(viewLifecycleOwner, Observer {
            if (it==1) {
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
            } else {
                updatingStudentGroupDataUI()
                anim_loading.visibility = View.GONE
            }

            //Update
            updatingStudentGroupDataUI()
        })

    }



    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        updatingStudentGroupDataUI()
        settingUpStudentMotUI()
    }

    private fun updatingStudentGroupDataUI() {
        shimmerMotCardStudentInfo.hideShimmer()
        if (Preference(requireContext()).getPrefString("id_group")!="null") {
            Log.i("id_group",Preference(requireContext()).getPrefString("id_group").toString())
            studentGroup.text = "${Preference(requireContext()).getPrefString("group_name")}"
            studentMentor.text = Preference(requireContext()).getPrefString("group_mentor_name")
            student_mentor_contact.text =
                "${Preference(requireContext()).getPrefString("group_mentor_contact")}"
        } else {
            studentGroup.text = "${getString(R.string.group)} :\nAnda Belum Terdaftar Di Kelompok"
            studentMentor.text = "-"
            student_mentor_contact.text= "-"
        }
    }

    private fun settingUpStudentMotUI() {
        shimmerMotCardStudentInfo.hideShimmer()
        student_name_home.text = Preference(requireContext()).getPrefString("student_name")
        val motViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MotivationViewModel::class.java)
        motViewModel.getMot()
        motViewModel.listMot.observe(viewLifecycleOwner, Observer {
            shimmerMotLayout.hideShimmer()
            shimmerMotLayout.visibility = View.GONE
            if (it != null) {
                recycler_mot.setHasFixedSize(true)
                recycler_mot.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                var motAdapter = MotAdapter()
                motAdapter.setData(it)
                recycler_mot.adapter = motAdapter
            } else {

            }
        })
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