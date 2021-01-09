package com.example.tvshowermvpretrofitrxjavakotlinapp.repository

import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import io.reactivex.Observable

interface DataRepository {

    fun callNetwork(user: String): Observable<DataModel>
}