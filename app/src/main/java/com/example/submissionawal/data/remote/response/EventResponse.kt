package com.example.submissionawal.data.remote.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class EventResponse(

	@field:SerializedName("listEvents")
	val listEvents: List<ListEventsItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.createTypedArrayList(ListEventsItem),
		parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
		parcel.readString()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeTypedList(listEvents)
		parcel.writeValue(error)
		parcel.writeString(message)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<EventResponse> {
		override fun createFromParcel(parcel: Parcel): EventResponse {
			return EventResponse(parcel)
		}

		override fun newArray(size: Int): Array<EventResponse?> {
			return arrayOfNulls(size)
		}
	}
}

annotation class Parcelize


@Parcelize
data class ListEventsItem(

	@field:SerializedName("summary")
	val summary: String? = null,

	@field:SerializedName("mediaCover")
	val mediaCover: String? = null,

	@field:SerializedName("registrants")
	val registrants: Int? = null,

	@field:SerializedName("imageLogo")
	val imageLogo: String? = null,

	@field:SerializedName("link")
	val link: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("ownerName")
	val ownerName: String? = null,

	@field:SerializedName("cityName")
	val cityName: String? = null,

	@field:SerializedName("quota")
	val quota: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("beginTime")
	val beginTime: String? = null,

	@field:SerializedName("endTime")
	val endTime: String? = null,

	@field:SerializedName("category")
	val category: String? = null
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readValue(Int::class.java.classLoader) as? Int,
		parcel.readString(),
		parcel.readString(),
		parcel.readString()
	)

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeString(summary)
		parcel.writeString(mediaCover)
		parcel.writeValue(registrants)
		parcel.writeString(imageLogo)
		parcel.writeString(link)
		parcel.writeString(description)
		parcel.writeString(ownerName)
		parcel.writeString(cityName)
		parcel.writeValue(quota)
		parcel.writeString(name)
		parcel.writeValue(id)
		parcel.writeString(beginTime)
		parcel.writeString(endTime)
		parcel.writeString(category)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<ListEventsItem> {
		override fun createFromParcel(parcel: Parcel): ListEventsItem {
			return ListEventsItem(parcel)
		}

		override fun newArray(size: Int): Array<ListEventsItem?> {
			return arrayOfNulls(size)
		}
	}
}
