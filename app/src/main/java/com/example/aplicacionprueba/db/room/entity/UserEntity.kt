package com.example.aplicacionprueba.db.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity (
    @PrimaryKey(false)
    @ColumnInfo(name = "userName")
    val userName:String
    //@TypeConverters(Converters::class)
    )
{
}