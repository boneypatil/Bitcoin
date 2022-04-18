package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_rate")
data class CoinRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @Embedded val time: Time?,
    val disclaimer: String?,
    @Embedded val bpi: Bpi?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readParcelable(Time::class.java.classLoader),
        parcel.readString(),
        parcel.readParcelable(Bpi::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(disclaimer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CoinRate> {
        override fun createFromParcel(parcel: Parcel): CoinRate {
            return CoinRate(parcel)
        }

        override fun newArray(size: Int): Array<CoinRate?> {
            return arrayOfNulls(size)
        }
    }
}