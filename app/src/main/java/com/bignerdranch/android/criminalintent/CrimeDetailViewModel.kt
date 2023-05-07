package com.bignerdranch.android.criminalintent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    // 13.20 Use a lambda expression that provides the latest crime available and have CrimeDetailFragment update it
    fun updateCrime(onUpdate: (Crime) -> Crime) {
        _crime.update { oldCrime ->
            oldCrime?.let { onUpdate(it) }
        }
    }

    // 13.24 Update the database when CrimeDetailViewModel is cleared by overriding onCleared() and
    //  use the viewModelScope class property to launch a coroutine. In the coroutine, access the latest value
    //  from the crime StateFlow and save it to the database
    override fun onCleared() {
        super.onCleared()
        //viewModelScope.launch {
            // 13.26 Call the updated function from outside a coroutine scope
            crime.value?.let { crimeRepository.updateCrime(it) }
        //}
    }


}

// 13.17 Create a factory class for CrimeDetailViewModel
class CrimeDetailViewModelFactory(
    private val crimeId: UUID       //add crimeId as a constructor parameter
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CrimeDetailViewModel(crimeId) as T
    }
}

