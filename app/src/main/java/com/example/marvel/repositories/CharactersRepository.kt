package com.example.marvel.repositories

import com.example.marvel.network.CharactersApi

class CharactersRepository(
    private val api: CharactersApi
) : SafeApiRequest() {

    suspend fun getCharacters(apikey:String,hash:String,ts:String) = apiRequest { api.getCharacters(apikey,hash,ts) }
    suspend fun searchCharacters(nameStartsWith:String,apikey:String,hash:String,ts:String) = apiRequest { api.searchCharacters(nameStartsWith,apikey,hash,ts) }

}