package com.example.marvel.ui.characterDetails

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.marvel.R
import com.example.marvel.data.models.MarvelResponse
import com.example.marvel.data.network.CharactersApi
import com.example.marvel.data.repositories.CharacterDetailsRepository
import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.snackbar
import com.stfalcon.imageviewer.StfalconImageViewer
import kotlinx.android.synthetic.main.fragment_character_details.*
import kotlinx.android.synthetic.main.fragment_characters.*


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailsFragment : Fragment(),CharacterDetailsListener,RecyclerViewMarvelClick {
    private var image: String? = null
    var name: String? = null
    private var description: String? = null
    private var characetrId: String? = null
    private lateinit var factory: CharacterDetailsViewModelFactory
    private lateinit var viewModel: CharacterDetailsViewModel

    private lateinit var seriesFactory: CharacterDetailsViewModelFactory
    private lateinit var seriesViewModel: CharacterDetailsViewModel
    public lateinit var seriesAdapter :MarvelAdapter

    private lateinit var storiesFactory: CharacterDetailsViewModelFactory
    private lateinit var storiesViewModel: CharacterDetailsViewModel
    public lateinit var storiesAdapter :MarvelAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        image = arguments?.getString("image")
        name = arguments?.getString("name")
        description = arguments?.getString("description")
        characetrId= arguments?.getString("id")

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tCharacterName.text = name
        tCharacterDescription.text = description
        Glide.with(this).load(image).into(characterImage)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val api = CharactersApi()
        val repository = CharacterDetailsRepository(api)

        factory = CharacterDetailsViewModelFactory(repository)
        seriesFactory = CharacterDetailsViewModelFactory(repository)
        storiesFactory = CharacterDetailsViewModelFactory(repository)

        viewModel = ViewModelProviders.of(this, factory).get(CharacterDetailsViewModel::class.java)
        seriesViewModel = ViewModelProviders.of(this, factory).get(CharacterDetailsViewModel::class.java)
        storiesViewModel = ViewModelProviders.of(this, factory).get(CharacterDetailsViewModel::class.java)
        viewModel.characterDetailsListener=this
        getComicsList()
        getSeriesList()
        getStoriesList()

    }
    val isConnected: Boolean
        get() {
            return (context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo?.isConnected == true
        }
    private fun getStoriesList() {
        if(isConnected) {
            viewModel.getStoriesList(
                characetrId.toString(),
                "5e04a468ed4195a738dd34e8fdf9b639",
                "7aefd3336721c23e03f8765ec7e41ac5",
                "1"
            )
            viewModel.stories.observe(viewLifecycleOwner, Observer { storiesList ->
                rvStories.also {
                    it.layoutManager =
                        LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                    it.setHasFixedSize(true)
                    storiesAdapter = MarvelAdapter(storiesList, this)
                    it.adapter = storiesAdapter

                }
                progressBar.hide()

            })
        }else{
            Toast.makeText(context,"check network connection!", Toast.LENGTH_LONG).show()
        }

    }

    private fun getSeriesList() {
        if(isConnected) {
        viewModel.getSeriesList(characetrId.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
            seriesViewModel.series.observe(viewLifecycleOwner, Observer {series ->
                rvSeries.also {
                    it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    it.setHasFixedSize(true)
                     seriesAdapter=MarvelAdapter(series,this)
                    it.adapter =seriesAdapter

                }
                progressBar.hide()

            })
        }else{
            Toast.makeText(context,"check network connection!", Toast.LENGTH_LONG).show()
        }
    }

    private fun getComicsList() {
        if(isConnected){
        viewModel.getComicsList(characetrId.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
            viewModel.comics.observe(viewLifecycleOwner, Observer { comics ->

                rvComics.also {
                    it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    it.setHasFixedSize(true)
                    it.adapter =
                        MarvelAdapter(comics,this)
                }
                progressBar.hide()

            })
        }else{
            Toast.makeText(context,"check network connection!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onStarted() {
        progressBar.show()

    }

    override fun onSuccess(charactersResponse: MarvelResponse.Data.Result) {
        progressBar.hide()
    }

    override fun onFailure(message: String) {
        progressBar.hide()
        root_layout.snackbar(message)
    }

    override fun onRecyclerMarvelItemClick(
        view: View,
        position: Int,
        marvelList: List<MarvelResponse.Data.Result>) {
        StfalconImageViewer.Builder<MarvelResponse.Data.Result>(context, marvelList) { view, marvelResult ->
            Glide.with(this).load(marvelResult.thumbnail.path+"."+marvelResult.thumbnail.extension).into(view)
        }
            .withStartPosition(position)
            .show()

    }


}