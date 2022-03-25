package com.example.aplicacionprueba.vista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.controller.FavoriteMovieAdapter
import com.example.aplicacionprueba.controller.MdbApi
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.viewmodel.MovieState
import com.example.aplicacionprueba.viewmodel.MovieViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FavoriteMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteMoviesFragment : Fragment(), MdbData.MdbDataObservers,MdbApi.CallbackFavoriteMovies{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var adapter: FavoriteMovieAdapter ?= null
    private val favoriteMovieListViewModel: MovieViewModel by viewModels(ownerProducer = { requireActivity() },factoryProducer = {
        MovieViewModel.Factory(appContext = this.requireContext().applicationContext)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        initFavorite()


        Log.d("Favorite","inicio fragmento favorite")
        adapter?.movies?.forEach {
            Log.d("Favorite",it.title)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite_movies, container, false)
        initRecyclerViewFav(view)


        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressFavorite)
        favoriteMovieListViewModel.favoriteMovielistModel.observe(viewLifecycleOwner) {
            when (it){
                is MovieState.LoadingMovies -> progressBar.isVisible = it.activate
                is MovieState.MovieError -> Toast.makeText(requireActivity(),"No hay conexion a internet",Toast.LENGTH_LONG).show()
                is MovieState.MovieSuccess -> adapter?.add(it.data)
            }
        }
    }
    fun initFavorite(){
        adapter = FavoriteMovieAdapter(mutableListOf())
        favoriteMovieListViewModel.getFavoriteMovies()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FavoriteMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavoriteMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initRecyclerViewFav(view:View){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFavoriteMovies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    override fun onFavoriteUpdate(listMovies:List<Movie>) {
        adapter?.add(listMovies)
    }

    override fun onFavoriteMoviesSuccess(movieList: List<Movie>) {
        adapter?.add(movieList)
    }

    override fun onFavoriteMoviesError() {
        TODO("Not yet implemented")
    }

    override fun onDetach() {
        super.onDetach()
    }
}