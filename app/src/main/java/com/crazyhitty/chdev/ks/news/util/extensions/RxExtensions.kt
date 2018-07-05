package com.crazyhitty.chdev.ks.news.util.extensions

import com.crazyhitty.chdev.ks.news.base.LoadingView
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <Type : Any> Observable<Type>.applySchedulers(): Observable<Type> {
    return compose { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Completable.applySchedulers(): Completable {
    return compose { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <Type : Any> Single<Type>.applySchedulers(): Single<Type> {
    return compose { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <Type : Any> Maybe<Type>.applySchedulers(): Maybe<Type> {
    return compose { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Completable.applyLoadingView(loadingView: LoadingView): Completable {
    return doOnSubscribe { loadingView.showLoading() }
            .doOnTerminate { loadingView.hideLoading() }
}

fun <T> Single<T>.applyLoadingView(loadingView: LoadingView): Single<T> {
    return doOnSubscribe { loadingView.showLoading() }
            .doAfterTerminate { loadingView.hideLoading() }
}

fun <T> Maybe<T>.applyLoadingView(loadingView: LoadingView): Maybe<T> {
    return doOnSubscribe { loadingView.showLoading() }
            .doAfterTerminate { loadingView.hideLoading() }
}