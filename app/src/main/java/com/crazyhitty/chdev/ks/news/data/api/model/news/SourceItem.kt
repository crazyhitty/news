package com.crazyhitty.chdev.ks.news.data.api.model.news

import android.os.Parcel
import android.os.Parcelable
import android.text.Spannable

data class SourceItem(val id: String? = null,
                      val name: String? = null,
                      val description: String? = null,
                      val url: String? = null,
                      val category: String? = null,
                      val language: String? = null,
                      val country: String? = null,
                      var selected: Boolean? = null,
                      @Transient var spannableName: Spannable? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readValue(Boolean::class.java.classLoader) as? Boolean)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeString(language)
        parcel.writeString(country)
        parcel.writeValue(selected)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourceItem> {
        override fun createFromParcel(parcel: Parcel): SourceItem {
            return SourceItem(parcel)
        }

        override fun newArray(size: Int): Array<SourceItem?> {
            return arrayOfNulls(size)
        }
    }
}