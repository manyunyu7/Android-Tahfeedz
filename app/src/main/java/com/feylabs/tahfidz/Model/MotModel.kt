package com.feylabs.tahfidz.Model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MotModel(
    var id : String,
    var img : String,
    var title : String,
    var content : String
) : Parcelable {

}