package com.example.marvel.ui.characterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.data.models.MarvelResponse
import com.example.marvel.databinding.RowMarvelBinding


class MarvelAdapter(private val comics: List<MarvelResponse.Data.Result>,
                   private val listener: CharacterDetailsFragment
) : RecyclerView.Adapter<MarvelAdapter.ComicsViewHolder>(){

    override fun getItemCount() = comics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ComicsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_marvel,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        holder.rowMarvelBinding.comicsItem = comics[position]
       holder.rowMarvelBinding.itemMarvelLayout.setOnClickListener {
            listener.onRecyclerMarvelItemClick(holder.rowMarvelBinding.itemMarvelLayout,position, comics)
        }

    }


    inner class ComicsViewHolder(
        val rowMarvelBinding: RowMarvelBinding
    ) : RecyclerView.ViewHolder(rowMarvelBinding.root)

}