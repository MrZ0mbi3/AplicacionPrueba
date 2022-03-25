package com.example.aplicacionprueba.vista

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.controller.MovieAdapter
import com.example.aplicacionprueba.viewmodel.MovieState
import com.example.aplicacionprueba.viewmodel.MovieViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ActualMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ActualMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val adapter: MovieAdapter by lazy { MovieAdapter(mutableListOf()) }
    private val movieListViewModel: MovieViewModel by viewModels(
        ownerProducer = { requireActivity() },
        factoryProducer = {
            MovieViewModel.Factory(appContext = this.requireContext().applicationContext)
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        initData()
    }

    private fun initData() {
        movieListViewModel.getActualMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_actual_movies, container, false)
        initRecycleActualMovie(view)


        // Inflate the layout for this fragment
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressActual)
        movieListViewModel.movielistModel.observe(viewLifecycleOwner) {
            when (it){
                is MovieState.LoadingMovies -> progressBar.isVisible = true
                is MovieState.MovieError -> {
                    progressBar.isVisible = false
                    Toast.makeText(requireActivity(),"No hay conexion a internet",Toast.LENGTH_LONG).show()
                }
                is MovieState.MovieSuccess -> {
                    progressBar.isVisible = false
                    adapter.add(it.data)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ActualMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ActualMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initRecycleActualMovie(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvActualMovies)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}