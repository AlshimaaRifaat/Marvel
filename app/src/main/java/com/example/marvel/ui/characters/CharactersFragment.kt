package com.example.marvel.ui.characters

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.marvel.R
import com.example.marvel.models.CharactersResponse
import com.example.marvel.network.CharactersApi
import com.example.marvel.network.NetworkConnectionInterceptor
import com.example.marvel.repositories.ApiException
import com.example.marvel.repositories.CharactersRepository
import com.example.marvel.repositories.NoInternetException

import com.example.marvel.util.hide
import com.example.marvel.util.show
import com.example.marvel.util.snackbar
import kotlinx.android.synthetic.main.fragment_characters.*
import android.widget.TextView.OnEditorActionListener
import com.example.marvel.ui.characterDetails.MarvelAdapter


class CharactersFragment : Fragment(), CharactersListener,RecyclerViewClickListener {

    private lateinit var factory: CharactersViewModelFactory
    private lateinit var viewModel: CharactersViewModel
    var navController: NavController? = null
    public lateinit var searchResultAdapter : CharactersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_characters, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val networkConnectionInterceptor= NetworkConnectionInterceptor(requireContext())
        val api = CharactersApi(networkConnectionInterceptor)
        val repository = CharactersRepository(api)

        factory = CharactersViewModelFactory(
            repository
        )
        viewModel = ViewModelProviders.of(this, factory).get(CharactersViewModel::class.java)
        viewModel.charactersListener=this
        getCharactersList()

        icSearch.setOnClickListener { view->
            cardView.visibility=View.VISIBLE
            tLabel.visibility=View.GONE
            icSearch.visibility=View.GONE
            tCancel.visibility=View.VISIBLE
        }
        tCancel.setOnClickListener { view->
            tLabel.visibility=View.VISIBLE
            cardView.visibility=View.GONE
            icSearch.visibility=View.VISIBLE
            tCancel.visibility=View.GONE
        }
        SearchKeyBoard()
        EditSearchChanger()
    }

    private fun EditSearchChanger() {
        etSearch.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty())
                {
                    getCharactersList()
                }else{
                    searchCharactersResult()
                }



            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {


            }
        })
    }

    private fun searchCharactersResult() {
        viewModel.getSearchCharactersResult(etSearch.text.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
        try {
            viewModel.searchCharactersResult.observe(viewLifecycleOwner, Observer { searchResultList ->

                rvCharacters.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    searchResultAdapter=CharactersAdapter(searchResultList,this)
                    it.adapter =searchResultAdapter

                }

            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
        }

    }

    private fun SearchKeyBoard() {
        etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!etSearch.text.toString().isEmpty()) {
                    searchCharactersResult();
                }
                return@OnEditorActionListener true
            }
            false
        })



    }

    private fun getCharactersList() {
        viewModel.getCharacters("5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
        try {
            viewModel.characters.observe(viewLifecycleOwner, Observer { characters ->

                rvCharacters.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    it.adapter =
                        CharactersAdapter(characters,this)
                }

            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
        }

    }


    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(charactersResponse: CharactersResponse.Data.Result) {
        progress_bar.hide()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        root_layout.snackbar(message)
    }

    override fun onRecyclerViewItemClick(view: View, characterItem: CharactersResponse.Data.Result) {
        when(view.id){
            R.id.item_root_layout -> {
                val bundle = bundleOf(
                    "image" to characterItem.thumbnail.path+"."+characterItem.thumbnail.extension,
                    "name" to characterItem.name,
                    "description" to characterItem.description,
                    "id" to characterItem.id.toString()
                )
                navController!!.navigate(
                    R.id.action_charactersFragment_to_characterDetailsFragment,
                    bundle
                )
            }

        }
    }

}