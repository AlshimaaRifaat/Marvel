package com.example.marvel.ui.characterDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.marvel.R
import com.example.marvel.models.MarvelResponse
import com.example.marvel.network.CharactersApi
import com.example.marvel.network.NetworkConnectionInterceptor
import com.example.marvel.repositories.ApiException
import com.example.marvel.repositories.CharacterDetailsRepository
import com.example.marvel.repositories.NoInternetException
import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.snackbar
import kotlinx.android.synthetic.main.fragment_character_details.*
import kotlinx.android.synthetic.main.fragment_characters.*


/**
 * A simple [Fragment] subclass.
 * Use the [CharacterDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CharacterDetailsFragment : Fragment(),CharacterDetailsListener {
    private var image: String? = null
    var name: String? = null
    private var description: String? = null
    private var characetrId: String? = null
    private lateinit var factory: CharacterDetailsViewModelFactory
    private lateinit var viewModel: CharacterDetailsViewModel

    private lateinit var seriesFactory: CharacterDetailsViewModelFactory
    private lateinit var seriesViewModel: CharacterDetailsViewModel
    public lateinit var seriesAdapter :MarvelAdapter

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

        val networkConnectionInterceptor= NetworkConnectionInterceptor(requireContext())
        val api = CharactersApi(networkConnectionInterceptor)
        val repository = CharacterDetailsRepository(api)
        factory = CharacterDetailsViewModelFactory(repository)

        seriesFactory = CharacterDetailsViewModelFactory(repository)
        viewModel = ViewModelProviders.of(this, factory).get(CharacterDetailsViewModel::class.java)
        seriesViewModel = ViewModelProviders.of(this, factory).get(CharacterDetailsViewModel::class.java)
        viewModel.characterDetailsListener=this
        getComicsList()
        getSeriesList()

    }

    private fun getSeriesList() {
        viewModel.getSeriesList(characetrId.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
        try {
            seriesViewModel.series.observe(viewLifecycleOwner, Observer {series ->
                rvSeries.also {
                    it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    it.setHasFixedSize(true)
                     seriesAdapter=MarvelAdapter(series)
                    it.adapter =seriesAdapter

                }
                progressBar.hide()

            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
        }

    }

    private fun getComicsList() {
        viewModel.getComicsList(characetrId.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
        try {
            viewModel.comics.observe(viewLifecycleOwner, Observer { comics ->

                rvComics.also {
                    it.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                    it.setHasFixedSize(true)
                    it.adapter =
                        MarvelAdapter(comics)
                }
                progressBar.hide()

            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
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


}