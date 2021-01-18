package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubmissionModel(
    var id : String,
    val id_student : String,
    val date : String,
    val studentName : String,
    var status_text : String,
    var status_code : String,
    val start : String,
    val end :String,
    val audio : String,
    val score_itqan : String,
    val score_ahkam : String,
    val score_makhroj : String,
    val score_tajwid : String,
    val score_final : String,
    val correction : String
) : Parcelable