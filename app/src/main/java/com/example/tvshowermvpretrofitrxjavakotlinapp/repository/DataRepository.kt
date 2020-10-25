package com.example.tvshowermvpretrofitrxjavakotlinapp.repository

import com.example.tvshowermvpretrofitrxjavakotlinapp.callback.Callback
import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import com.example.tvshowermvpretrofitrxjavakotlinapp.network.RetrofitClient
import com.example.tvshowermvpretrofitrxjavakotlinapp.network.WebServices
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observer
import io.reactivex.Observable

class DataRepository {

    var compositeDisposable = CompositeDisposable()

    fun callNetwork(user: String, callback: Callback<DataModel>){

        val retrofitClient = RetrofitClient()
        val webServices: WebServices = retrofitClient.retrofitInstance

        val dataModelObservable: Observable<DataModel> =
            webServices.getDataModelFromNetwork(user)
        dataModelObservable
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object: Observer<DataModel> {

                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                    compositeDisposable.add(d)
                }

                override fun onNext(t: DataModel) {
                    callback.returnResult(t)
                }

                override fun onError(e: Throwable) {
                    callback.returnError(e.message.toString())
                }
            })
    }
}