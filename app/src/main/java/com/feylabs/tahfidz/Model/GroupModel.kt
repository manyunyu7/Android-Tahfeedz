package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GroupModel(
  val group_id :String,
  val group_name : String,
  val group_type :String
) : Parcelable