package com.crazyhitty.chdev.ks.news.util.extensions

import android.content.Context
import android.content.Intent

const val EXTRA_ID = "id"

fun Context.showScreenFromAction(action: String, id: String? = null) {
    val intent = Intent(action)
    intent.`package` = applicationContext.packageName
    if (id != null) {
        intent.putExtra(EXTRA_ID, id)
    }
    startActivity(intent)
}