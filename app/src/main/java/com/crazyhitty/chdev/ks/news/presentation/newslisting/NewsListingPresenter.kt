package com.crazyhitty.chdev.ks.news.presentation.newslisting

import com.crazyhitty.chdev.ks.news.BuildConfig
import com.crazyhitty.chdev.ks.news.domain.newslisting.FetchNewsUseCase
import com.crazyhitty.chdev.ks.news.util.extensions.applyLoadingView
import com.crazyhitty.chdev.ks.news.util.extensions.applySchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Implementation of [NewsListingContract.Presenter]
 *
 * @author  Kartik Sharma (cr42yh17m4n@gmail.com)
 */
class NewsListingPresenter @Inject constructor(
        private val view: NewsListingContract.View,
        private val fetchNewsUseCase: FetchNewsUseCase,
        private val mapper: NewsViewModelMapper
) : NewsListingContract.Presenter() {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onAttach() {
        super.onAttach()
        fetchNews()
    }

    override fun onDetach() {
        compositeDisposable.clear()
        super.onDetach()
    }

    override fun fetchNews() {
        fetchNewsUseCase.execute("ars-technica", 20, 1)
                .applySchedulers()
                .applyLoadingView(view)
                .subscribe({
                    view.render(mapper.transform(it))
                }, {
                    it.printStackTrace()
                })
    }

    override fun onNewsItemClicked() {
        view.showScreen(BuildConfig.ACTION_SPLASH)
    }
}