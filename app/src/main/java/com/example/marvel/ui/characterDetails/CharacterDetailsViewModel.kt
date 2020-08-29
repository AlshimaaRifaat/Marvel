package com.example.marvel.ui.characterDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel.models.MarvelResponse
import com.example.marvel.repositories.CharacterDetailsRepository
import com.example.marvel.util.Coroutines
import kotlinx.coroutines.Job

class CharacterDetailsViewModel ( private val repository: CharacterDetailsRepository) : ViewModel() {
    var characterDetailsListener: CharacterDetailsListener?=null
    private lateinit var job: Job
    private lateinit var seriesJob: Job
    private lateinit var storiesJob: Job

    private val _comics = MutableLiveData<List<MarvelResponse.Data.Result>>()
    val comics: LiveData<List<MarvelResponse.Data.Result>>
        get() = _comics

    private val _series = MutableLiveData<List<MarvelResponse.Data.Result>>()
    val series: LiveData<List<MarvelResponse.Data.Result>>
        get() = _series

    private val _stories = MutableLiveData<List<MarvelResponse.Data.Result>>()
    val stories: LiveData<List<MarvelResponse.Data.Result>>
        get() = _stories

    fun getComicsList(characterId:String,apikey:String,hash:String,ts:String) {
        job = Coroutines.ioThenMain(
            { repository.getComics(characterId,apikey,hash,ts) },
            { _comics.value = it?.data?.results }
        )
    }

    fun getSeriesList(characterId:String,apikey:String,hash:String,ts:String) {
        seriesJob = Coroutines.ioThenMain(
            { repository.getSeries(characterId,apikey,hash,ts) },
            { _series.value = it?.data?.results }
        )
    }
    fun getStoriesList(characterId:String,apikey:String,hash:String,ts:String) {
        storiesJob = Coroutines.ioThenMain(
            { repository.getStoriesList(characterId,apikey,hash,ts) },
            { _stories.value = it?.data?.results }
        )
    }
    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
        if(::seriesJob.isInitialized) seriesJob.cancel()
        if(::storiesJob.isInitialized) storiesJob.cancel()
    }
}