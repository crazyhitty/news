package com.crazyhitty.chdev.ks.news.data.api.model.news

import android.text.Spannable

data class SourceItem(val id: String? = null,
                      val name: String? = null,
                      val description: String? = null,
                      val url: String? = null,
                      val category: String? = null,
                      val language: String? = null,
                      val country: String? = null,
                      var selected: Boolean? = null,
                      @Transient var spannableName: Spannable? = null)