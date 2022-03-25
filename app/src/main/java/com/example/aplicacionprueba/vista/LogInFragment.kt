package com.example.aplicacionprueba.vista

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.viewmodel.MovieState
import com.example.aplicacionprueba.viewmodel.MovieViewModel


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val movieViewModel: MovieViewModel by viewModels(ownerProducer = { requireActivity() }, factoryProducer = {
        MovieViewModel.Factory(appContext = this.requireContext().applicationContext)
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista = inflater.inflate(R.layout.fragment_log_in, container, false)
        val nameUser = vista.findViewById<EditText>(R.id.editTextnameUser).text
        val userPassword = vista.findViewById<EditText>(R.id.editTextTextPassword).text
        val btnLogIn = vista.findViewById<Button>(R.id.buttonLogIn)

            btnLogIn.setOnClickListener {
                movieViewModel.autenticarUsuario(nameUser.toString(), userPassword.toString())
                Log.d("Sign in", "user" + nameUser)
                movieViewModel.verificarUsuario(nameUser.toString())
                val newFragment = SecondFragment()
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.FragmentContainer, newFragment)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel.logInModel.observe(viewLifecycleOwner) {
            when (it){
                is MovieState.LoadingMovies -> {}
                is MovieState.MovieError -> Toast.makeText(requireActivity(),"No hay conexion a internet",Toast.LENGTH_LONG).show()
                is MovieState.MovieSuccess -> {}
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
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}