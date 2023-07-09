package com.example.android.politicalpreparedness.election.model

import com.example.android.politicalpreparedness.network.models.Division
import java.util.*

data class Election(
    val id: Int,
    val name: String,
    val electionDay: Date,
    val division: Division
)