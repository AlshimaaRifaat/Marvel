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

    private val _characters = MutableLiveData<CharactersResponse.Data>()
    val characters: LiveData<CharactersResponse.Data>
    get() = _characters

    private val _searchCharactersResult = MutableLiveData<CharactersResponse.Data>()
    val searchCharactersResult: LiveData<CharactersResponse.Data>
        get() = _searchCharactersResult

    fun getCharacters(apikey:String,hash:String,ts:String,page:Int) {
        job = Coroutines.ioThenMain(
            { repository.getCharacters(apikey,hash,ts,page) },
            { _characters.value = it?.data }
        )
    }

    fun getSearchCharactersResult(nameStartsWith:String,apikey:String,hash:String,ts:String,page:Int) {
        searchResultJob = Coroutines.ioThenMain(
            { repository.searchCharacters(nameStartsWith,apikey,hash,ts,page) },
            { _searchCharactersResult.value = it?.data }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if(::job.isInitialized) job.cancel()
        if(::searchResultJob.isInitialized) searchResultJob.cancel()
    }
}