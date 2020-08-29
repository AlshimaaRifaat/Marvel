package com.example.marvel.ui.characterDetails

import android.view.View
import com.example.marvel.data.models.CharactersResponse
import com.example.marvel.data.models.MarvelResponse
import java.text.FieldPosition

interface RecyclerViewMarvelClick {
    fun onRecyclerMarvelItemClick(view: View,position: Int, marvelList:List<MarvelResponse.Data.Result>)
}