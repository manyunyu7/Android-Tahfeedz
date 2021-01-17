package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdminModel(
    val id: String,
    val name: String,
    var contact: String,
    val desc: String,
    val email: String
) : Parcelable