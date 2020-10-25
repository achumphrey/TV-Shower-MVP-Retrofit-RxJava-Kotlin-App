package com.example.tvshowermvpretrofitrxjavakotlinapp.network

import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WebServices {

    @GET("singlesearch/shows")
    fun getDataModelFromNetwork(
        @Query("q") user: String?
    ): Observable<DataModel>
}