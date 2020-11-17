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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mentor_landing)

        adapterListGroup = GroupAdapter()

        rv_group.setHasFixedSize(true)
        rv_group.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val groupViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(GroupViewModel::class.java)


        groupViewModel.retrieveGroupList(Preference(this).getPrefString("mentor_id").toString())
        groupViewModel.statusGroupList.observe(this, Observer {
            if (it) {
                "Berhasil".showToast()
            } else {
                "Gagal".showToast()
            }
        })

        groupViewModel.groupList.observe(this, Observer {
            if (it != null) {
                adapterListGroup.setData(it)
                rv_group.adapter=adapterListGroup
            } else {

            }
        })
    }
}