package com.example.marvel.ui.characters

import android.view.View
import com.example.marvel.data.models.CharactersResponse

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, characterItem: CharactersResponse.Data.Result)
}