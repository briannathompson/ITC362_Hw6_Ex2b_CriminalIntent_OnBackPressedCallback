package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.*

// Create a CrimeListViewModel class that extends (inherits from) ViewModel
class CrimeListViewModel : ViewModel() {

    // Add a property to store a list of Crimes
    val crimes = mutableListOf<Crime>()

    init {
        viewModelScope.launch {         // 12.2 Launch a coroutine
            for (i in 0 until 100) {
                val crime = Crime(
                    id = UUID.randomUUID(),
                    title = "Crime #$i",
                    date = Date(),
                    isSolved = i % 2 == 0
                )
                crimes += crime
            }
        }
    }
}