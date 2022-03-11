package com.example.aplicacionprueba.model

data class DbUsers (private val listUsers:MutableList<User>) {

    fun findUserByUsername(name: String): User? {
        listUsers.forEach {
            if (it.userName.equals(name)){
                return it
            }
        }
        return null
    }

    fun add(newUser: User) {
        listUsers.add(newUser)
    }

}