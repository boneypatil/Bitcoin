package com.reasons.bitcoin.service


import com.reasons.bitcoin.data.CoinRate
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface BitconService {

    @GET(value = "/v1/bpi/currentprice.json")
    fun fetchExploreDetails(): Deferred<CoinRate>
}