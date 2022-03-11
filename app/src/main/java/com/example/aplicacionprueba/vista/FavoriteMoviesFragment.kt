package com.example.aplicacionprueba.vista

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionprueba.MdbApplication
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.controller.FavoriteMovieAdapter
import com.example.aplicacionprueba.controller.MdbApi
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.model.Movie
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        adapter = FavoriteMovieAdapter((requireActivity().applicationContext as MdbApplication).data.actualUser.getFavoriteMovies().toMutableList())
        Log.d("Favorite","inicio fragmento favorite")
        adapter?.movies?.forEach {
            Log.d("Favorite",it.title)
        }


        with((requireActivity().applicationContext as MdbApplication)){
            mdbApi.callbackFavoriteMovies=this@FavoriteMoviesFragment
            //mdbApi.getFavoriteMovies("3/account/{account_id}/favorite/movies?api_key=267e487850dbcabfc3958c5f0b40bb10&session_id=",mdbApi.sessionId)
            data.addObserver(this@FavoriteMoviesFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_favorite_movies, container, false)
        adapter?.add(listOf(
            Movie(true, "", emptyList(), 1, "", "", "", 2.0, "", "", UUID.randomUUID().toString(), true, 1.0, 1),
            Movie(true, "", emptyList(), 2, "", "", "", 2.0, "", "", UUID.randomUUID().toString(), true, 1.0, 1),
            Movie(true, "", emptyList(), 3, "", "", "", 2.0, "", "", UUID.randomUUID().toString(), true, 1.0, 1)
        ))
        initRecyclerViewFav(view)


        // Inflate the layout for this fragment
        return view
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
        with((requireActivity().applicationContext as MdbApplication)){
            mdbApi.callbackFavoriteMovies= null
            data.removeObserver(this@FavoriteMoviesFragment)
        }
        super.onDetach()
    }
}