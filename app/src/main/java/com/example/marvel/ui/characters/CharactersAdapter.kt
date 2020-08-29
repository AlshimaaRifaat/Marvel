package com.example.marvel.ui.characters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.databinding.RowCharactersBinding
import com.example.marvel.models.CharactersResponse

class CharactersAdapter (private val characters: List<CharactersResponse.Data.Result>,
                         private val listener: RecyclerViewClickListener
) : RecyclerView.Adapter<CharactersAdapter.CharactersViewHolder>(){

    override fun getItemCount() = characters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharactersViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_characters,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        holder.rowCharactersBinding.characterItem = characters[position]
        holder.rowCharactersBinding.itemRootLayout.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.rowCharactersBinding.itemRootLayout, characters[position])
        }

    }


    inner class CharactersViewHolder(
        val rowCharactersBinding: RowCharactersBinding
    ) : RecyclerView.ViewHolder(rowCharactersBinding.root)

}