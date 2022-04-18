package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import com.squareup.moshi.Json

data class Bpi(
    @field:Json(name = "USD")
    @Embedded val uSD: USD?,
    @field:Json(name = "GBP")
    @Embedded
    val gBP: GBP?,
    @field:Json(name = "EUR")
    @Embedded
    val eUR: EUR?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(USD::class.java.classLoader),
        parcel.readParcelable(GBP::class.java.classLoader),
        parcel.readParcelable(EUR::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Bpi> {
        override fun createFromParcel(parcel: Parcel): Bpi {
            return Bpi(parcel)
        }

        override fun newArray(size: Int): Array<Bpi?> {
            return arrayOfNulls(size)
        }
    }
}