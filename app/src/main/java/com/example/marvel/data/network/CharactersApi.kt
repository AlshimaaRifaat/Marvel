package com.example.marvel.data.network


import com.example.marvel.data.models.CharactersResponse
import com.example.marvel.data.models.MarvelResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface CharactersApi {
    @GET("characters")
    suspend fun getCharacters(
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int
    ) : Response<CharactersResponse>

    @GET("characters")
    suspend fun searchCharacters(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String,
        @Query("offset") offset: Int
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

    @GET("characters/{characterId}/stories")
    suspend fun getStoriesList(
        @Path(value = "characterId", encoded = true) characterId:String,
        @Query("apikey") apikey: String,
        @Query("hash") hash: String,
        @Query("ts") ts: String
    ) : Response<MarvelResponse>

    companion object{
        operator fun invoke(networkConnectionInterceptor: NetworkConnectionInterceptor) : CharactersApi {
            val interceptor= HttpLoggingInterceptor();

            interceptor.level= HttpLoggingInterceptor.Level.BODY


            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS) // connect timeout
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)

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