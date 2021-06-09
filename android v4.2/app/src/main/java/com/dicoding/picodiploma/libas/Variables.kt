package com.dicoding.picodiploma.libas

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Variables(
    var temp: Double? = 0.00,
    var humid: Int? = 0,
    var rain: Int? = 0,
    var wind: Double? = 0.00
): Parcelable {
}