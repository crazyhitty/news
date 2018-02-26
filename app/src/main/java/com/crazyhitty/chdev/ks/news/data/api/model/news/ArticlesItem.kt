package com.crazyhitty.chdev.ks.news.data.api.model.news

import android.os.Parcel
import android.os.Parcelable

data class ArticlesItem(
        val publishedAt: String? = null,
        var publishedAtReadable: String? = null,
        val author: String? = null,
        val urlToImage: String? = null,
        val description: String? = null,
        val source: Source? = null,
        val title: String? = null,
        val url: String? = null): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(Source::class.java.classLoader),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(publishedAt)
        parcel.writeString(publishedAtReadable)
        parcel.writeString(author)
        parcel.writeString(urlToImage)
        parcel.writeString(description)
        parcel.writeParcelable(source, flags)
        parcel.writeString(title)
        parcel.writeString(url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ArticlesItem> {
        override fun createFromParcel(parcel: Parcel): ArticlesItem {
            return ArticlesItem(parcel)
        }

        override fun newArray(size: Int): Array<ArticlesItem?> {
            return arrayOfNulls(size)
        }
    }
}
