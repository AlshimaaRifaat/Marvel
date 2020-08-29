package com.example.marvel.repositories

import com.example.marvel.network.CharactersApi

class CharactersRepository(
    private val api: CharactersApi
) : SafeApiRequest() {

    suspend fun getCharacters(apikey:String,hash:String,ts:String,page:Int) = apiRequest { api.getCharacters(apikey,hash,ts,page) }
    suspend fun searchCharacters(nameStartsWith:String,apikey:String,hash:String,ts:String,page: Int) = apiRequest { api.searchCharacters(nameStartsWith,apikey,hash,ts,page) }

}