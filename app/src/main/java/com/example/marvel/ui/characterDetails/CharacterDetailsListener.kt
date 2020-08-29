package com.example.marvel.ui.characterDetails

import com.example.marvel.models.MarvelResponse

interface CharacterDetailsListener {
    fun onStarted()
    fun onSuccess(charactersResponse: MarvelResponse.Data.Result)
    fun onFailure(message:String)
}