package com.bignerdranch.android.criminalintent

import java.util.*

// Create the Crime data class and add 4 properties
data class Crime(
    val id: UUID,           // an ID to uniquely identify the instance
    val title: String,      // a descriptive title, like “Toxic sink dump” or “Someone stole my yogurt!”
    val date: Date,         // a date
    val isSolved: Boolean   // a Boolean indication of whether the crime has been solved
)
