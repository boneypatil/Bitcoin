package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo


data class USD (
	@ColumnInfo(name = "code_usd")
	val code : String?,
	@ColumnInfo(name = "symbol_usd")
	val symbol : String?,
	@ColumnInfo(name = "rate_usd")
	val rate : String?,
	@ColumnInfo(name = "description_usd")
	val description : String?,
	@ColumnInfo(name = "rate_float_usd")
	val rate_float : Double
):Parcelable {
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

	companion object CREATOR : Parcelable.Creator<USD> {
		override fun createFromParcel(parcel: Parcel): USD {
			return USD(parcel)
		}

		override fun newArray(size: Int): Array<USD?> {
			return arrayOfNulls(size)
		}
	}
}