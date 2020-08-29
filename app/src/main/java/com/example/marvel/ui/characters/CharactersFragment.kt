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
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.marvel.paging.EndlessScrollListener
import com.example.marvel.ui.characterDetails.MarvelAdapter


class CharactersFragment : Fragment(), CharactersListener,RecyclerViewClickListener {

    private lateinit var factory: CharactersViewModelFactory
    private lateinit var viewModel: CharactersViewModel
    var navController: NavController? = null
    public lateinit var charactersAdapter : CharactersAdapter
    public lateinit var searchResultAdapter : CharactersAdapter


    var my_page = 1
    lateinit var result_List: MutableList<CharactersResponse.Data.Result>

    var totalPages:Int=0

    lateinit var endlessScrollListener: EndlessScrollListener
    //private val kProgressHUD: KProgressHUD? = null
    //lateinit var searchPeopleAdapter: PopularPeopleAdapter
    var my_search_page = 1
    //    lateinit var result_search_List: MutableList<result>
    var Status:Boolean=false
    var totalSearchPages:Int=0
    lateinit var endlessSearchScrollListener: EndlessScrollListener
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
        result_List=  mutableListOf<CharactersResponse.Data.Result>()
        val networkConnectionInterceptor= NetworkConnectionInterceptor(requireContext())
        val api = CharactersApi(networkConnectionInterceptor)
        val repository = CharactersRepository(api)

        factory = CharactersViewModelFactory(
            repository
        )
        viewModel = ViewModelProviders.of(this, factory).get(CharactersViewModel::class.java)
        viewModel.charactersListener=this
        charactersAdapter= CharactersAdapter(result_List,this)
        rvCharacters.adapter =charactersAdapter
        ScrollSearch()
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
       // SearchKeyBoard()
        EditSearchChanger()
    }

    private fun ScrollSearch() {
        rvCharacters.layoutManager = LinearLayoutManager(requireContext())
        endlessScrollListener = object : EndlessScrollListener(rvCharacters.layoutManager!!) {
            override fun onLoadMore(currentPage: Int, totalItemCount: Int) {
                if (currentPage < totalPages) {
                    my_page++
                    searchCharactersResult(my_page)
                }

            }

            override fun onScroll(firstVisibleItem: Int, dy: Int, scrollPosition: Int) {}
        }
    }

    private fun EditSearchChanger() {
        etSearch.addTextChangedListener(object : TextWatcher {


            override fun afterTextChanged(s: Editable) {
               /* if(s.isEmpty())
                {
                    getCharactersList()
                }else{
                    searchCharactersResult()
                }*/

                initScroll()
                result_List.clear()

                my_page = 1
                totalPages=0

                if(s.isEmpty()){
                    Status=false
                    charactersList(my_page)
                }else {

                    Status = true
                    searchCharactersResult(my_page)
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

    private fun searchCharactersResult(page:Int) {
        viewModel.getSearchCharactersResult(etSearch.text.toString(),"5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1",page)
        try {
            viewModel.searchCharactersResult.observe(viewLifecycleOwner, Observer { searchData ->
                this.totalPages = (searchData.offset!!+1)*(searchData.limit!!)
                // Toast.makeText(this,result_List.size.toString(),Toast.LENGTH_LONG).show()
                result_List.addAll(searchData.results)
                charactersAdapter.notifyItemRangeInserted(
                    charactersAdapter.getItemCount(),
                    result_List.size
                )

                rvCharacters.addOnScrollListener(endlessScrollListener)


            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
        }

    }

   /* private fun SearchKeyBoard() {
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
*/
    private fun getCharactersList() {
        initScroll()
        charactersList(my_page);
       /* viewModel.getCharacters("5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1")
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
        }*/

    }

    private fun charactersList(page: Int) {
        viewModel.getCharacters("5e04a468ed4195a738dd34e8fdf9b639","7aefd3336721c23e03f8765ec7e41ac5","1",0)
        try {
            viewModel.characters.observe(viewLifecycleOwner, Observer { charactersData ->

                /*rvCharacters.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.setHasFixedSize(true)
                    it.adapter =
                        CharactersAdapter(characters,this)
                }*/
                this.totalPages = (charactersData.offset!!+1)*(charactersData.limit!!)
                result_List.addAll(charactersData.results)

                charactersAdapter.notifyItemRangeInserted(
                    charactersAdapter.getItemCount(),
                    result_List.size
                )

                rvCharacters.addOnScrollListener(endlessScrollListener)

            }) }  catch (e: ApiException) {
            e.printStackTrace()
            onFailure(e.message.toString())

        } catch (e: NoInternetException) {
            e.printStackTrace()
            onFailure(e.message.toString())
        }
    }

    private fun initScroll() {
        rvCharacters.layoutManager = LinearLayoutManager(requireContext())
        rvCharacters.setItemAnimator(DefaultItemAnimator())

        endlessScrollListener = object : EndlessScrollListener(rvCharacters.layoutManager!!) {
            override fun onLoadMore(currentPage: Int, totalItemCount: Int) {
                if (Status == false) {
                    if (currentPage < totalPages) {
                        my_page++
                        charactersList(my_page)
                    }
                } else {
                    if (currentPage < totalPages) {
                        my_page++
                        searchCharactersResult(my_page)
                    }
                }
            }

            override fun onScroll(firstVisibleItem: Int, dy: Int, scrollPosition: Int) {}
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
                    "image" to characterItem.thumbnail?.path+"."+characterItem.thumbnail?.extension,
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