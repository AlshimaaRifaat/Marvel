package com.example.marvel.repositories

import com.example.marvel.network.CharactersApi

class CharacterDetailsRepository (
    private val api: CharactersApi
) : SafeApiRequest() {

    suspend fun getComics(characterId:String,apikey:String,hash:String,ts:String) = apiRequest { api.getComics(characterId,apikey,hash,ts) }
    suspend fun getSeries(characterId:String,apikey:String,hash:String,ts:String) = apiRequest { api.getSeries(characterId,apikey,hash,ts) }
    suspend fun getStoriesList(characterId:String,apikey:String,hash:String,ts:String) = apiRequest { api.getStoriesList(characterId,apikey,hash,ts) }

}