package com.crazyhitty.chdev.ks.news.domain.newslisting

import com.crazyhitty.chdev.ks.news.domain.NewsRepository
import javax.inject.Inject

class FetchNewsUseCase @Inject constructor(private val repository: NewsRepository) {

    fun execute(sources: String, pageSize: Int, page: Int) = repository.fetchNews(sources, pageSize, page)
}