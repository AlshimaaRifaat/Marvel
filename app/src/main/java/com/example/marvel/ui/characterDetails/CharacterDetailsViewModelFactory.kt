package com.example.marvel.ui.characterDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.marvel.repositories.CharacterDetailsRepository
import com.example.marvel.repositories.CharactersRepository
import com.example.marvel.ui.characters.CharactersViewModel

@Suppress("UNCHECKED_CAST")
class CharacterDetailsViewModelFactory(
    private val repository: CharacterDetailsRepository
) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharacterDetailsViewModel(repository) as T
    }

}