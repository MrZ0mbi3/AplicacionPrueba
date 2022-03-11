package com.example.aplicacionprueba

import android.app.Application
import com.example.aplicacionprueba.controller.MdbApi
import com.example.aplicacionprueba.db.MdbData

class MdbApplication: Application (){
    val mdbApi : MdbApi by lazy { MdbApi(data) }
    val data: MdbData = MdbData()
}