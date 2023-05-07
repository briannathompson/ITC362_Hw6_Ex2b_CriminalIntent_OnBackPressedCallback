package com.bignerdranch.android.criminalintent.database

import androidx.room.Dao
import androidx.room.Query
import com.bignerdranch.android.criminalintent.Crime
import java.util.*

// 12.14 Create a DAO (Data Access Object) called CrimeDao
@Dao        // @Dao tells Room that CrimeDao is a data access object
interface CrimeDao {

    /*  @Query annotation means that getCrimes() and getCrime() are meant to PULL info from database (not insert, update, or delete).
    *   @Query expects a string containing a SQL command as input */

    // Returns all crimes in the crime database table
    @Query("SELECT * FROM crime")
    suspend fun getCrimes(): List<Crime>   // Adding "suspend" allows you to asynchromously call these functions within a coroutine

    // Returns only a crime that matches a specified id
    @Query("SELECT * FROM crime WHERE id=(:id)")
    suspend fun getCrime(id: UUID): Crime

}