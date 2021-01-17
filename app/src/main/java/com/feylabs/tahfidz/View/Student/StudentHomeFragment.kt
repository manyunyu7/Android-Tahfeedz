package com.feylabs.tahfidz.View.Student

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.AdminAdapter
import com.feylabs.tahfidz.Model.MotAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.View.QuranModulesViews.ListSurahActivity
import com.feylabs.tahfidz.View.SharedView.MentorQuiz
import com.feylabs.tahfidz.ViewModel.AdminViewModel
import com.feylabs.tahfidz.ViewModel.MotivationViewModel
import com.feylabs.tahfidz.ViewModel.StudentViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_student_home.*
import kotlinx.android.synthetic.main.layout_contact_admin.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_menu_group.*
import kotlinx.android.synthetic.main.layout_menu_mentor.*

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
    lateinit var adminViewModel: AdminViewModel

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
        adminViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(AdminViewModel::class.java)
        adminViewModel.getAdmin()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shimmerMotCardStudentInfo.startShimmer()

        adminViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(AdminViewModel::class.java)
        adminViewModel.getAdmin()
        swipeRefreshStudentHome.setOnRefreshListener {
            swipeRefreshStudentHome.isRefreshing=false
            studentViewModel.getStudentData(Preference(requireContext()).getPrefString("student_ID").toString())
            anim_loading.visibility=View.VISIBLE
        }

        val adminAdapter = AdminAdapter()
        adminViewModel.dataAdmin.observe(viewLifecycleOwner, Observer {
            if (it!=null){
                adminAdapter.setData(it)
                rv_admin.setHasFixedSize(true)
                rv_admin.layoutManager=LinearLayoutManager(requireContext())
                rv_admin.adapter=adminAdapter
            }else{
                "Tidak Ada Data Admin".showToast()
            }
        })

        btn_call_admin.setOnClickListener {
            adminViewModel.getAdmin()
            lyt_contact_admin.visibility=View.VISIBLE
            lyt_contact_admin.animation= AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_appear)
        }

        btnCloseAdmin.setOnClickListener {
            lyt_contact_admin.visibility=View.GONE
            lyt_contact_admin.animation=
                AnimationUtils.loadAnimation(requireContext(), R.anim.item_animation_gone_bottom)
        }


        adminViewModel.statusGetAdmin.observe(viewLifecycleOwner, Observer {
            if (it==3){
                anim_loading.visibility=View.VISIBLE
            }else{
                anim_loading.visibility=View.GONE
            }

            if (it==1){
                //Do Nothing
            }

            //If Internet is Not Available
            if (it==0){
                cfAlert(
                    "Koneksi Internet Tidak Ditemukan , Periksa Koneksi Internet Anda atau Coba Lagi Nanti"
                    , 0, -1
                )
            }
        })

        if (!isOnline()) {
            NoInternet(
                R.color.alert_default_icon_color, R.color.colorWhite
            )
        }


        menuQuranCard.setOnClickListener {
            startActivity(Intent(requireContext(),ListSurahActivity::class.java))
        }
        btnMenuQuiz.setOnClickListener {
            requireContext().startActivity(
                Intent(requireContext(),
                    MentorQuiz::class.java)
            )
        }
        downloadPicasso(profile_pic)
        studentViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(StudentViewModel::class.java)
        studentViewModel.statusGetUpdated.observe(viewLifecycleOwner, Observer {
            if (it==3){
                anim_loading.visibility=View.VISIBLE
            }
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
            val announcement = Preference(requireContext()).getPrefString("group_announcement").toString()
            Log.i("Announcement",announcement)
            if (announcement=="" || announcement==null || announcement=="null"){
                studentAnnouncement.text="Belum Ada Pengumuman"
            }else{
                studentAnnouncement.text=announcement
            }
            student_mentor_contact.text =
                "${Preference(requireContext()).getPrefString("group_mentor_contact")}"
        } else {
            studentAnnouncement.text = "Anda Belum Memiliki Kelompok"
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