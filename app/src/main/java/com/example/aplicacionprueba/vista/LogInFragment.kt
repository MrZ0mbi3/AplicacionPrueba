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
import androidx.fragment.app.Fragment
import com.example.aplicacionprueba.MdbApplication
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.db.room.mapper.toMovie
import com.example.aplicacionprueba.db.room.mapper.toUser
import com.example.aplicacionprueba.db.room.mapper.toUserEntity
import com.example.aplicacionprueba.model.Movie
import com.example.aplicacionprueba.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Toast.makeText(context, "onCreate Fragment log in", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onCreate Fragment log in")
    }

    override fun onAttach(context: Context) {
        Toast.makeText(context, "onAttach Fragment log in", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onAttach Fragment log in")
        super.onAttach(context)
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
            autenticarUsuario(nameUser.toString(), userPassword.toString())
            Log.d("Sign in", "user" + nameUser)
            verificarUsuario(nameUser.toString())
            val newFragment = SecondFragment()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.FragmentContainer, newFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        Log.d("lifeCycle", "onCreateView Fragment log in")
        return vista
    }


    fun verificarUsuario(name: String) {
        val context = (requireActivity().applicationContext as MdbApplication)
        val mdbApi = context.mdbApi
        val mdbData = context.data
        val mdbRoom = context.roomDB

        CoroutineScope(Dispatchers.Main.immediate).launch {
            withContext(Dispatchers.IO) {
                if (mdbRoom.UserDao().getUserByName(name) != null) {

                    val currentUser = mdbRoom.UserDao().getUserByName(name)
                    val favorites = mdbRoom.UserDao().getFavoriteMovieList()
                        .first { it.user.userName == currentUser.userName }.favoritos.map {
                            it.toMovie(
                                currentUser.userName
                            )
                        }
                    mdbData.actualUser =
                        currentUser.toUser(mutableSetOf<Movie>().also { it.addAll(favorites) })
                    Log.d("Sign in", "User is not new" + mdbData.actualUser.userName)
                } else {
                    val newUser = User(name, mutableSetOf())

                    mdbRoom.UserDao().insertUser(newUser.toUserEntity())
                    val currentUser = mdbRoom.UserDao().getUserByName(name)
                    val favorites = mdbRoom.UserDao().getFavoriteMovieList()
                        .first { it.user.userName == currentUser.userName }.favoritos.map {
                            it.toMovie(
                                currentUser.userName
                            )
                        }
                    mdbData.actualUser =
                        currentUser.toUser(mutableSetOf<Movie>().also { it.addAll(favorites) })
                    Log.d("Sign in", "User is new" + mdbData.actualUser.userName)
                }

            }


        }
    }


    fun autenticarUsuario(userName: String, userPassword: String) {
        with((requireActivity().applicationContext as MdbApplication)) {
            mdbApi.requestAuthToken(
                "3/authentication/token/new?api_key=267e487850dbcabfc3958c5f0b40bb10",
                "3/authentication/token/validate_with_login?api_key=267e487850dbcabfc3958c5f0b40bb10",
                userName,
                userPassword
            )
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("lifeCycle", "onActivityCreated Fragment log in")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        Log.d("lifeCycle", "onStart Fragment log in")
        super.onStart()
    }

    override fun onResume() {
        Log.d("lifeCycle", "onResume Fragment log in")
        super.onResume()
    }

    override fun onPause() {
        Log.d("lifeCycle", "onPause Fragment log in")
        super.onPause()
    }

    override fun onStop() {
        Log.d("lifeCycle", "onStop Fragment log in")
        super.onStop()
    }

    override fun onDestroyView() {
        Log.d("lifeCycle", "onDestroyView Fragment log in")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "onDestroy Fragment log in")
        super.onDestroy()
    }

    override fun onDetach() {
        Log.d("lifeCycle", "onDetach Fragment log in")
        super.onDetach()
    }
}