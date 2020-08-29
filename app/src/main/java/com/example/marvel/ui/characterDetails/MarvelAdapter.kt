package com.example.marvel.ui.characterDetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.databinding.RowComicsBinding
import com.example.marvel.models.MarvelResponse

class MarvelAdapter(private val comics: List<MarvelResponse.Data.Result>
                 /*   private val listener: CharacterDetailsFragment*/
) : RecyclerView.Adapter<MarvelAdapter.ComicsViewHolder>(){

    override fun getItemCount() = comics.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ComicsViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_comics,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ComicsViewHolder, position: Int) {
        holder.rowComicsBinding.comicsItem = comics[position]
       /* holder.rowComicsBinding.itemRootLayout.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.rowComicsBinding.itemRootLayout, comics[position])
        }*/

    }


    inner class ComicsViewHolder(
        val rowComicsBinding: RowComicsBinding
    ) : RecyclerView.ViewHolder(rowComicsBinding.root)

}