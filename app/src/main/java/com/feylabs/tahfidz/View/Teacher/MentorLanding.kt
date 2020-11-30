package com.feylabs.tahfidz.View.Teacher

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.GroupAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.Base.BaseActivity
import com.feylabs.tahfidz.View.QuranModules.ListSurahActivity
import com.feylabs.tahfidz.ViewModel.GroupViewModel
import kotlinx.android.synthetic.main.activity_mentor_landing.*
import kotlinx.android.synthetic.main.activity_mentor_profile.*
import kotlinx.android.synthetic.main.layout_menu_mentor.*

class MentorLanding : BaseActivity() {
    lateinit var adapterListGroup: GroupAdapter
    lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_landing)
        groupViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GroupViewModel::class.java)

        downloadPicassoTeacher(mentorPhotos)

        adapterListGroup = GroupAdapter()
        groupViewModel.retrieveGroupList(Preference(this).getPrefString("mentor_id").toString())

        rv_group.setHasFixedSize(true)
        rv_group.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        teacher_name_home.text = Preference(this).getPrefString("mentor_name")

        menuQuranCard.setOnClickListener {
            startActivity(Intent(this,ListSurahActivity::class.java))
        }
        btnEditProfile.setOnClickListener {
            startActivity(Intent(this,MentorProfile::class.java))
        }



        groupViewModel.statusGroupList.observe(this, Observer {
            if (it) {
                //IF true
            } else {
                //IF False
            }
        })

        groupViewModel.groupList.observe(this, Observer {
            if (it != null) {
                adapterListGroup.setData(it)
                rv_group.adapter = adapterListGroup
            } else {

            }
        })
    }
}