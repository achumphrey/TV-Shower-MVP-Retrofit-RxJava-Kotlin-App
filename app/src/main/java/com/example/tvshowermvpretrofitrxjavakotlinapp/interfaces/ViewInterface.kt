package com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces

import com.example.tvshowermvpretrofitrxjavakotlinapp.model.DataModel

interface ViewInterface {

    fun init()
    fun showError(message: String)
    fun loadDataModel(data: DataModel)
}