package com.example.aplicacionprueba.model

import com.google.gson.annotations.SerializedName

data class MdbResponseAuthenticationToken (@SerializedName("success") var success: Boolean,
                                           @SerializedName("expires_at") var expiresAt:String,
                                           @SerializedName("request_token") var authToken: String)