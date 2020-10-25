package com.example.tvshowermvpretrofitrxjavakotlinapp.presenter

import com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces.PresenterInterface
import com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces.ViewInterface
import com.example.tvshowermvpretrofitrxjavakotlinapp.callback.Callback
import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel
import com.example.tvshowermvpretrofitrxjavakotlinapp.repository.DataRepository

class DataPresenter(_view: ViewInterface): PresenterInterface {

    var view: ViewInterface? = _view

    override fun start() {
        view?.init()
    }

    override fun loadDataFromRepo(user: String) {

        DataRepository().callNetwork(user, object: Callback<DataModel>{

            override fun returnResult(t: DataModel) {
                view?.loadDataModel(t)
            }

            override fun returnError(message: String) {
                view?.showError(message)
            }

        })
    }

    override fun onDestroy() {
        view = null
    }
}