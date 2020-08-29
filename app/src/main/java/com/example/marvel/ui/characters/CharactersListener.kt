package com.example.marvel.ui.characters

import com.example.marvel.models.CharactersResponse

interface CharactersListener {
    fun onStarted()
    fun onSuccess(charactersResponse: CharactersResponse.Data.Result)
    fun onFailure(message:String)
}