package com.dicoding.picodiploma.libas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variables(
    var temp: String? = "",
    var humid: String? = "",
    var rain: String? = "",
    var wind: String? = ""
): Parcelable {
}