package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo


data class GBP(
    @ColumnInfo(name = "code_gbp")
    val code: String?,
    @ColumnInfo(name = "symbol_gbp")
    val symbol: String?,
    @ColumnInfo(name = "rate_gbp")
    val rate: String?,
    @ColumnInfo(name = "description_gbp")
    val description: String?,
    @ColumnInfo(name = "rate_float_gbp")
    val rate_float: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(code)
        parcel.writeString(symbol)
        parcel.writeString(rate)
        parcel.writeString(description)
        parcel.writeDouble(rate_float)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GBP> {
        override fun createFromParcel(parcel: Parcel): GBP {
            return GBP(parcel)
        }

        override fun newArray(size: Int): Array<GBP?> {
            return arrayOfNulls(size)
        }
    }
}