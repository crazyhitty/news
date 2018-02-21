package com.crazyhitty.chdev.ks.news.data.api.model.news

import android.os.Parcel
import android.os.Parcelable

data class Source(val id: String? = null,
                  val name: String? = null,
                  val description: String? = null,
                  val url: String? = null,
                  val category: String? = null,
                  val language: String? = null,
                  val country: String? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeString(language)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(parcel: Parcel): Source {
            return Source(parcel)
        }

        override fun newArray(size: Int): Array<Source?> {
            return arrayOfNulls(size)
        }
    }

}
