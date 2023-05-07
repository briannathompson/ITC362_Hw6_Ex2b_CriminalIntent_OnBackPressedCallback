package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

/*  CrimeDetailViewModel needs the id to load for when CrimeDetailViewModel is created.
    The most effective way is to declare the crime id as a constructor parameter,
        except ViewModel can only have a constructor with no args or a single SavedStateHandle arg.
    We can add more args to our ViewModel by adding a class that implements the ViewModelProvider.Factory interface
    ViewModelProvider.Factory knows how to make ViewModel instances (like how a car factory knows how to make cars)*/
class CrimeDetailViewModel(crimeId: UUID) : ViewModel() {
    // Properties:
    /* val is read-only (unlike var, which is read/write) */
    private val crimeRepository = CrimeRepository.get() // references our repository (where we make data calls)
    private val _crime: MutableStateFlow<Crime?> = MutableStateFlow(null) // create a mutable list (which means we can add and subtract from it)
    val crime: StateFlow<Crime?> = _crime.asStateFlow() // Want to expose data as STateFlow not MutableStateFlow so the data can't be mutated by its consumers

    // 13.17 building a factory for CrimeDetailViewModel
    init {
        viewModelScope.launch {
            _crime.value = crimeRepository.getCrime(crimeId)
        }
    }
}

class CrimeDetailViewModelFactory(
    private val crimeId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}

