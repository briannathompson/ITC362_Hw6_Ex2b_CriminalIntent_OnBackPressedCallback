package com.bignerdranch.android.criminalintent

import android.content.Context
import androidx.room.Room
import com.bignerdranch.android.criminalintent.database.CrimeDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

/*  A repository class encapsulates the logic for accessing data from a single
    source or a set of sources. It determines how to fetch and store a particular set of data.
    The UI code will request all the data from the repository

    A repository encapsulates the logic for adding data so it's all in one spot */

// 12.20 create a constant to store database name
private const val DATABASE_NAME = "crime-database"

/* CrimeRepository is a singleton, there will only be one instance of it in the app process
*   Marking the constructor as private ensures no components can "go rogue and create their own instance" */
class CrimeRepository private constructor(context: Context) {

    // 13.25 create a coroutineScope constructor property and pass it GlobalScope
    //   as the default parameter for a new coroutine scope
    /*  GlobalScope lives longer than a viewModelScope, so it's good to update your db
        in the background when the user moves away from CrimeDetailFragment */
    private val coroutineScope: CoroutineScope = GlobalScope


    /*  Room.databaseBuilder() creates a concrete implementation of the abstract CrimeDatabase using
        three parameters:
            1. a Context object, since the database is accessing the filesystem
            2. the database class you want Room to create
            3. the name of the database file you want Room to create */
    private val database: CrimeDatabase = Room
        .databaseBuilder(
            context.applicationContext, // we pass applicationContext bc the singleton will live longer than any activity classes
            CrimeDatabase::class.java,
            DATABASE_NAME
        )
        /* createFromAsset(databaseFilePath) */
        .createFromAsset(DATABASE_NAME)
        .build()


    fun getCrimes(): Flow<List<Crime>> = database.crimeDao().getCrimes()
    suspend fun getCrime(id: UUID): Crime = database.crimeDao().getCrime(id)

    // 13.23 Create a function that allows you to access the DAO update function through this repository
    /*suspend*/ fun updateCrime(crime: Crime) {
        // 13.25 Use the new coroutineScope property to save the updated crime in the database
        coroutineScope.launch {
            database.crimeDao().updateCrime(crime)
        }
    }


    companion object {      // Create a companion object
        private var INSTANCE: CrimeRepository? = null

        // Make CrimeRepository a singleton by adding two functions to the companion object:
        // 1st function: Initializes a new instance of the repository
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }
        // 2nd function: Access the repository
        fun get(): CrimeRepository {
            return INSTANCE ?:
            throw IllegalStateException("CrimeRepository must be initialized")
        }
    }
}
