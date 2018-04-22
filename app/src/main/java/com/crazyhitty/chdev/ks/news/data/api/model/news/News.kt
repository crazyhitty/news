package com.crazyhitty.chdev.ks.news.data.api.model.news

data class News(
        val totalResults: Int? = null,
        val articles: List<ArticleItem?>? = null,
        val status: String? = null) {
    override fun equals(other: Any?): Boolean {
        if (other !is News) return false
        if (totalResults != other.totalResults) return false
        if (status != other.status) return false
        if (articles == null && other.articles != null) return false
        if (articles != null && other.articles == null) return false
        if (articles == null && other.articles == null) return true
        for (i in 0..((articles?.size ?: 0) - 1)) {
            if (articles?.get(i)?.equals(other.articles?.get(i)) == false) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = totalResults ?: 0
        result = 31 * result + (articles?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }
}
