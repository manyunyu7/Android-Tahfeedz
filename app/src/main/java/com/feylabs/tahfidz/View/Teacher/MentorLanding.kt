package com.feylabs.tahfidz.View.Teacher

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.GroupAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.BaseView.BaseActivity
import com.feylabs.tahfidz.View.QuranModulesViews.ListSurahActivity
import com.feylabs.tahfidz.ViewModel.GroupViewModel
import kotlinx.android.synthetic.main.activity_mentor_landing.*
import kotlinx.android.synthetic.main.layout_loading_transparent.*
import kotlinx.android.synthetic.main.layout_menu_mentor.*

class MentorLanding : BaseActivity() {
    lateinit var adapterListGroup: GroupAdapter
    lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_landing)
        groupViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GroupViewModel::class.java)
        adapterListGroup = GroupAdapter()

        swipeRefreshMentorLanding.setOnRefreshListener {
            swipeRefreshMentorLanding.isRefreshing = false
            adapterListGroup.clearData()
            groupViewModel.retrieveGroupList(Preference(this).getPrefString("mentor_id").toString())
            anim_loading.visibility = View.VISIBLE
        }

        //Check Internet Connection
        if (!isOnline()) {
            NoInternet(
                R.color.alert_default_icon_color, R.color.colorWhite
            )
        }
        groupViewModel.retrieveGroupList(Preference(this).getPrefString("mentor_id").toString())
        groupViewModel.groupList.observe(this, Observer {
            adapterListGroup.clearData()
            if (it != null) {
                anim_loading.visibility = View.GONE
                adapterListGroup.setData(it)
            } else {
                anim_loading.visibility = View.GONE
                statusGroup.visibility = View.VISIBLE
                statusGroup.text = "Belum Ada Kelompok Bimbingan"
            }
        })



        setLayout()


        rv_group.setHasFixedSize(true)
        rv_group.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)



        menuQuranCard.setOnClickListener {
            startActivity(Intent(this, ListSurahActivity::class.java))
        }
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this, MentorProfile::class.java))
        }



        rv_group.adapter = adapterListGroup
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun setLayout() {
        downloadPicassoTeacher(mentorPhotos)
        teacher_name_home.text = Preference(this).getPrefString("mentor_name")
    }
}