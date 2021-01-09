package com.example.tvshowermvpretrofitrxjavakotlinapp.repository

import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import com.example.tvshowermvpretrofitrxjavakotlinapp.network.RetrofitClient
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observable

class DataReposImpl: DataRepository {

    override fun callNetwork(user: String): Observable<DataModel> {

        val webServices = RetrofitClient().retrofitInstance

        return webServices.getDataModelFromNetwork(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}