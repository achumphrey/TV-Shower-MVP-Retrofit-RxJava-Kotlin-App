package com.example.tvshowermvpretrofitrxjavakotlinapp.interfaces

interface PresenterInterface {

    fun start()
    fun loadDataFromRepo(user: String)
    fun onDestroy()
}