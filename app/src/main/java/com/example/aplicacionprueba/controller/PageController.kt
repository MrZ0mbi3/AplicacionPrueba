package com.example.aplicacionprueba.controller

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.aplicacionprueba.vista.ActualMoviesFragment
import com.example.aplicacionprueba.vista.FavoriteMoviesFragment

class PageController  (fm: FragmentManager, nTabs: Int): FragmentStatePagerAdapter(fm) {
    var numTabs: Int = nTabs

    override fun getCount(): Int {
        return numTabs
    }

    override fun getItem(position: Int): Fragment{
        when(position)
        {
            0 -> return ActualMoviesFragment()
            1 -> return FavoriteMoviesFragment()
            else -> {
                return  ActualMoviesFragment()
            }

        }
    }
}