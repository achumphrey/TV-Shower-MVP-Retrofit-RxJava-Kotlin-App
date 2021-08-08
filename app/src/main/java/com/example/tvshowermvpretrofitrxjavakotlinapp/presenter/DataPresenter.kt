package com.example.tvshowermvpretrofitrxjavakotlinapp.presenter

import com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces.PresenterInterface
import com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces.ViewInterface
import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import com.example.tvshowermvpretrofitrxjavakotlinapp.repository.DataReposImpl
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class DataPresenter(_view: ViewInterface) : PresenterInterface {

    var view: ViewInterface? = _view
    var compositeDisposable = CompositeDisposable()

    override fun start() {
        view?.init()
    }

    override fun loadDataFromRepo(user: String) {

        DataReposImpl().callNetwork(user)
            .subscribe(dataModelObserver())
    }

    private fun dataModelObserver(): Observer<DataModel> {

        return object : Observer<DataModel> {

            override fun onComplete() {
            }

            override fun onSubscribe(d: Disposable) {
                compositeDisposable.add(d)
            }

            override fun onNext(t: DataModel) {
                view?.loadDataModel(t)
            }

            override fun onError(e: Throwable) {
                view?.showError(e.message.toString())
            }
        }
    }

    override fun onDestroy() {
       view = null
       compositeDisposable.clear()
    }
}