package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import java.util.*

// Create a CrimeListViewModel class that extends (inherits from) ViewModel
class CrimeListViewModel : ViewModel() {

    // Add a property to store a list of Crimes
    val crimes = mutableListOf<Crime>()

    // In the following init block, populate the list with dummy data
    init {
        for (i in 0 until 100) { // this will populate the list with 100 Crime objects
            val crime = Crime(
                id = UUID.randomUUID(),
                title ="Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0
            )
            crimes += crime
        }
    }
}