package com.crazyhitty.chdev.ks.news.util.extensions

import android.widget.EditText
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */

/**
 * Listen for text changes on this edit text.
 */
fun EditText.onTextChanged(textChanged:(String) -> Unit) {
    RxTextView.textChanges(this)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { textChanged(it.toString()) }
}