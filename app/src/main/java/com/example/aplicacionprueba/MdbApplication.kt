package com.example.aplicacionprueba

import android.app.Application
import androidx.room.Room
import com.example.aplicacionprueba.controller.MdbApi
import com.example.aplicacionprueba.db.MdbData
import com.example.aplicacionprueba.db.room.MdbDataBaseRoom

class MdbApplication: Application (){
    val mdbApi : MdbApi by lazy { MdbApi(data,roomDB) }
    val data: MdbData by lazy { MdbData(roomDB) }
    val roomDB by lazy{ Room.databaseBuilder(applicationContext, MdbDataBaseRoom::class.java,"DB").build()}

}