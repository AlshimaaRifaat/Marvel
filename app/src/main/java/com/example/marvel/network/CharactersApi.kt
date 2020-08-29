package com.example.marvel.network


import com.example.marvel.models.CharactersResponse
import com.example.marvel.models.MarvelResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ) : Response<CharactersResponse>

    @GET("characters")
    suspend fun searchCharacters(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ) : Response<CharactersResponse>

    @GET("characters/{characterId}/comics")
    suspend fun getComics(
        @Path(value = "characterId", encoded = true) characterId:String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ) : Response<MarvelResponse>

    @GET("characters/{characterId}/series")
    suspend fun getSeries(
        @Path(value = "characterId", encoded = true) characterId:String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ) : Response<MarvelResponse>


    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : CharactersApi {
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://gateway.marvel.com/v1/public/")
                .build()
                .create(CharactersApi::class.java)
        }
    }
}