package com.example.aplicacionprueba.model

import com.google.gson.annotations.SerializedName

data class MdbResponseSessionId (@SerializedName("success") var success: Boolean,
                                 @SerializedName("session_id") var session_id:String)