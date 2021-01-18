package com.feylabs.tahfidz.Util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.arthurivanets.bottomsheets.BaseBottomSheet
import com.arthurivanets.bottomsheets.config.BaseConfig
import com.arthurivanets.bottomsheets.config.Config
import com.feylabs.tahfidz.R
import kotlinx.android.synthetic.main.layout_detail_submission.view.*

class SubBottomSheet(
    hostActivity : Activity,
    config : BaseConfig = Config.Builder(hostActivity).build()
) : BaseBottomSheet(hostActivity, config) {


    override fun onCreateSheetContentView(context : Context) : View {
        return LayoutInflater.from(context).inflate(
            R.layout.layout_detail_submission,
            this,
            false
        )
    }

}