package com.example.aplicacionprueba.vista

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.aplicacionprueba.R
import com.example.aplicacionprueba.controller.MdbService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        Toast.makeText(this, "OnCreate actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onCreate Activity")


    }

    override fun onStart() {
        Toast.makeText(this, "OnStart actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onStart Activity")
        super.onStart()
    }

    override fun onResume() {
        Toast.makeText(this, "OnResume actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onResume Activity")
        super.onResume()
    }

    override fun onPause() {
        Toast.makeText(this, "OnPause actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onPause Activity")
        super.onPause()
    }

    override fun onStop() {
        Toast.makeText(this, "onStop actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onStop Activity")
        super.onStop()
    }

    override fun onRestart() {
        Toast.makeText(this, "onRestart actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onRestart Activity")
        super.onRestart()
    }

    override fun onDestroy() {
        Toast.makeText(this, "onDestroy actividad 1", Toast.LENGTH_LONG).show()
        Log.d("lifeCycle", "onDestroy Activity")
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_square -> {
            // User chose the "Settings" item, show the app settings UI...
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }



}