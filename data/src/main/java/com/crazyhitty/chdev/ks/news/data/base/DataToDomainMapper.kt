package com.crazyhitty.chdev.ks.news.data.base

interface DataToDomainMapper<in Data, out Domain> {

   fun transform(dataModel: Data): Domain
}