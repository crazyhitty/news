package com.crazyhitty.chdev.ks.news.base

interface DomainToViewMapper<in Domain, out View> {

   fun transform(domainModel: Domain): View
}