package com.example.marvel.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvel.models.CharactersResponse
import com.example.marvel.repositories.CharactersRepository
import com.example.marvel.util.Coroutines
import kotlinx.coroutines.Job

class CharactersViewModel ( private val repository: CharactersRepository) : ViewModel() {
    var charactersListener: CharactersListener?=null
    private lateinit var job: Job
    private lateinit var searchResultJob: Job

    private val _characters = MutableLiveData<List<CharactersResponse.Data.Result>>()
    val characters: LiveData<List<CharactersResponse.Data.Result>>
    get() = _characters

    private val _searchCharactersResult = MutableLiveData<List<CharactersResponse.Data.Result>>()
    val searchCharactersResult: LiveData<List<CharactersResponse.Data.Result>>
        get() = _searchCharactersResult

    fun getCharacters(apikey:String,hash:String,ts:String) {
        job = Coroutines.ioThenMain(
            { repository.getCharacters(apikey,hash,ts) },
            { _characters.value = it?.data?.results }
        )
    }

    fun getSearchCharactersResult(nameStartsWith:String,apikey:String,hash:String,ts:String) {
        searchResultJob = Coroutines.ioThenMain(
            { repository.searchCharacters(nameStartsWith,apikey,hash,ts) },
            { _searchCharactersResult.value = it?.data?.results }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
        if(::searchResultJob.isInitialized) searchResultJob.cancel()
    }
}