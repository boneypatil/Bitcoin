package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo


data class EUR(
    @ColumnInfo(name = "code_eur")
    val code: String?,
    @ColumnInfo(name = "symbol_eur")
    val symbol: String?,
    @ColumnInfo(name = "rate_eur")
    val rate: String?,
    @ColumnInfo(name = "description_eur")
    val description: String?,
    @ColumnInfo(name = "rate_float_eur")
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

    companion object CREATOR : Parcelable.Creator<EUR> {
        override fun createFromParcel(parcel: Parcel): EUR {
            return EUR(parcel)
        }

        override fun newArray(size: Int): Array<EUR?> {
            return arrayOfNulls(size)
        }
    }
}