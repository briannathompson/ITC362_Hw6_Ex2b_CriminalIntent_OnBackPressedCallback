package com.bignerdranch.android.criminalintent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


// 12.3 Delaying work: create a TAG to see how delaying work works
private const val TAG = "CrimeListViewModel"

class CrimeListViewModel : ViewModel() {

    val crimes = mutableListOf<Crime>() // property that stores list of crimes

    init {
        Log.d(TAG, "init starting")     // 12.3 Delaying work: Create a log that'll show that the previous init line ran

        viewModelScope.launch {     // 12.2 Launch a coroutine

            Log.d(TAG, "coroutine launched")        // 12.3 Delaying work: create a log that indicates the viewModelScope.launch { ran

            // 12.4 Define loadCrimes() suspending function: Moved code into loadCrimes()

            crimes += loadCrimes()
            Log.d(TAG, "Loading crimes finished")   // 12.3 Delaying work: log that the coroutine is finished
        }
    }

    // 12.4 Define a suspending function called loadCrimes()
    suspend fun loadCrimes(): List<Crime> {
        val result = mutableListOf<Crime>()
        delay(5000)// 12.3 Delaying work: delay for 5 seconds with delay(timeMillis:Long)
        for (i in 0 until 100) {
            val crime = Crime(
                id = UUID.randomUUID(),
                title = "Crime #$i",
                date = Date(),
                isSolved = i % 2 == 0
            )
            result += crime
        }
        return result
    }

}