package com.reasons.bitcoin.data

import android.os.Parcel
import android.os.Parcelable


data class Time (

	val updated : String?,
	val updatedISO : String?
): Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString()
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(updated)
		parcel.writeString(updatedISO)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Time> {
		override fun createFromParcel(parcel: Parcel): Time {
			return Time(parcel)
		}

		override fun newArray(size: Int): Array<Time?> {
			return arrayOfNulls(size)
		}
	}
}