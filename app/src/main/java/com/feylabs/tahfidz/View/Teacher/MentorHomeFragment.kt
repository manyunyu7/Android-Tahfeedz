package com.feylabs.tahfidz.View.Teacher

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
import com.feylabs.tahfidz.Model.GroupMemberAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseFragment
import com.feylabs.tahfidz.View.SharedView.MentorQuiz
import com.feylabs.tahfidz.ViewModel.GroupViewModel
import kotlinx.android.synthetic.main.fragment_teacher_home.*
import kotlinx.android.synthetic.main.layout_menu_group.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TeacherHomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MentorHomeFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var mentor_id = ""
    lateinit var groupMemberAdapter: GroupMemberAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        val groupViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
                .get(GroupViewModel::class.java)
        groupViewModel.retrieveGroupAnnouncement(
            Preference(requireContext()).getPrefString("group_temp").toString()
        )
        groupViewModel.retrieveGroupMember(mentor_id)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mentor_id = Preference(requireContext()).getPrefString("mentor_id").toString()

        groupMemberAdapter = GroupMemberAdapter()

        val groupViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
                .get(GroupViewModel::class.java)

        groupViewModel.retrieveGroupAnnouncement(
            Preference(requireContext()).getPrefString("group_temp").toString()
        )
        groupViewModel.retrieveGroupMember(mentor_id)



        groupViewModel.statusRetrieveGroupAnnouncement.observe(viewLifecycleOwner, Observer {
            if (it == 3) {
                anim_loading.visibility = View.VISIBLE
            } else {
                anim_loading.visibility = View.GONE
            }
            if (it == 0 || it == 2) {
                "Gagal Mengambil Data Pengumuman Grup , Periksa Koneksi Internet Anda atau Coba Lagi Nanti".showToast()
            }
        })

        groupViewModel.group_announcement_text.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                studentAnnouncementFill.setText(it)
            } else {
                studentAnnouncementFill.text = null
                studentAnnouncementFill.hint = "Buat Pengumuman Disini"
            }
        })

        btnSaveAnnouncement.setOnClickListener {
            groupViewModel.updateGroupAnnouncement(
                Preference(requireContext()).getPrefString("group_temp").toString(),
                studentAnnouncementFill.text.toString()
            )
        }



        groupViewModel.statusUpdateAnnouncement.observe(viewLifecycleOwner, Observer {
            if (it==3){
                anim_loading.visibility=View.VISIBLE
            }else{
                anim_loading.visibility=View.GONE
            }
            if (it==1){
                //If Announcement Updated, Retrieve the new One
                groupViewModel.retrieveGroupAnnouncement(
                    Preference(requireContext()).getPrefString("group_temp").toString()
                )
                "Berhasil Mengupdate Pengumuman".showToast()
            }
            if (it==0){
                "Gagal Mengupdate Pengumuman, Periksa Koneksi Internet Anda dan Coba Lagi Nanti".showToast()
            }
            if (it==2){
                "Gagal Mengupdate Pengumuman".showToast()
            }
        })

        rv_list_member.setHasFixedSize(true)
        rv_list_member.layoutManager = LinearLayoutManager(requireContext())

        btnMenuQuiz.setOnClickListener {
            requireContext().startActivity(
                Intent(
                    requireContext(),
                    MentorQuiz::class.java
                )
            )
        }




        anim_loading.visibility = View.VISIBLE



        groupViewModel.statusRetrieveGroupMember.observe(viewLifecycleOwner, Observer {
            when (it) {
                3 -> {
                    anim_loading.visibility = View.VISIBLE
                }
                1 -> {
                    anim_loading.visibility = View.GONE
                    populateRecyclerView()
                }
                2 -> {
                    anim_loading.visibility = View.GONE
                    "Terjadi Kesalahan".showToast()
                }
                0 -> {
                    anim_loading.visibility = View.GONE
                    "Terjadi Error pada Koneksi"
                }
            }
        })
    }

    private fun populateRecyclerView() {
        val groupViewModel =
            ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory())
                .get(GroupViewModel::class.java)

        groupViewModel.groupMember.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                groupMemberAdapter.setData(it)
                Log.i("fan-data", groupMemberAdapter.itemCount.toString())
                rv_list_member.adapter = groupMemberAdapter
            } else {
                "Belum Ada Anggota Kelompok".showToast()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_teacher_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TeacherHomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MentorHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}