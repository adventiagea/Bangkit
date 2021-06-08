package com.dicoding.picodiploma.libas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variables(
    var temp: Int? = 0,
    var humid: Int? = 0,
    var rain: Int? = 0,
    var wind: Int? = 0
): Parcelable {
}