package com.example.marvel.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marvel.repositories.CharactersRepository


@Suppress("UNCHECKED_CAST")
class CharactersViewModelFactory(
    private val repository: CharactersRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharactersViewModel(repository) as T
    }

}