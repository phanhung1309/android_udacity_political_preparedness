package com.example.android.politicalpreparedness.database

import androidx.room.*
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertElection(election: Election)

    @Query("SELECT * FROM election_table ORDER BY electionDay")
    fun getElections(): List<Election>

    @Query("SELECT * FROM election_table WHERE id = :id")
    fun getElectionById(id: Int): Election?

    @Delete
    fun deleteElection(election: Election)

    @Query("DELETE FROM election_table")
    fun clearElections()


}