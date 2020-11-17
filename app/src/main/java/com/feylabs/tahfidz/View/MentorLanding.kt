package com.feylabs.tahfidz.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.feylabs.tahfidz.Model.GroupAdapter
import com.feylabs.tahfidz.R
import com.feylabs.tahfidz.Util.SharedPreference.Preference
import com.feylabs.tahfidz.View.Base.BaseActivity
import com.feylabs.tahfidz.ViewModel.GroupViewModel
import kotlinx.android.synthetic.main.activity_mentor_landing.*

class MentorLanding : BaseActivity() {
    lateinit var adapterListGroup: GroupAdapter
    lateinit var groupViewModel: GroupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_landing)
        groupViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GroupViewModel::class.java)

        adapterListGroup = GroupAdapter()
        groupViewModel.retrieveGroupList(Preference(this).getPrefString("mentor_id").toString())

        refreshLanding.setOnRefreshListener {
            refreshLanding.apply {
                isRefreshing = true
                groupViewModel.retrieveGroupList(Preference(this@MentorLanding).getPrefString("mentor_id").toString())
            }
        }

        rv_group.setHasFixedSize(true)
        rv_group.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)




        groupViewModel.statusGroupList.observe(this, Observer {
            if (it) {
                refreshLanding.isRefreshing=false
            } else {
                refreshLanding.isRefreshing=false
            }
        })

        groupViewModel.groupList.observe(this, Observer {
            if (it != null) {
                adapterListGroup.setData(it)
                rv_group.adapter = adapterListGroup
            } else {
                refreshLanding.isRefreshing=false
            }
        })
    }
}