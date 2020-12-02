package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuizModel(
    val id_quiz : String,
    val group_id : String,
    val title : String,
    val desc : String,
    val gform_link : String,
    val created_at : String,
    val updated_at : String
) : Parcelable