package com.example.tvshowermvpretrofitrxjavakotlinapp.callback

interface Callback<T> {

    fun returnResult(t:T)
    fun returnError(message: String)
}