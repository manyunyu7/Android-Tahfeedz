package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubmissionModel(
    val id : String,
    val id_student : String,
    val date : String,
    val studentName : String,
    var status : String,
    val start : String,
    val end :String,
    val audio : String
) : Parcelable