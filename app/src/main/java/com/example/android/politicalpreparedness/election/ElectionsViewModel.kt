package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionsViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _upcomingElections = MutableLiveData<List<Election>>()
    val upcomingElections: LiveData<List<Election>>
        get() = _upcomingElections

    private val _savedElections = MutableLiveData<List<Election>>()
    val savedElections: LiveData<List<Election>>
        get() = _savedElections

    private val _navigateToVoterInfo = MutableLiveData<Election?>()
    val navigateToVoterInfo: LiveData<Election?>
        get() = _navigateToVoterInfo

    private val apiService = CivicsApi.retrofitService

    init {
        getUpcomingElectionsFromAPI()
        getSavedElectionsFromLocalDatabase()
    }

    private fun getSavedElectionsFromLocalDatabase() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _savedElections.postValue(dataSource.getElections())
            }
        }
    }

    private fun getUpcomingElectionsFromAPI() {
        viewModelScope.launch {
            try {
                val electionResponse = apiService.getElections()
                _upcomingElections.value = electionResponse.elections
            } catch (exception: Exception) {
                Timber.e("Error: %s", exception.localizedMessage)
                _upcomingElections.value = ArrayList()
            }
        }
    }

    fun displayVoterInfo(election: Election) {
        _navigateToVoterInfo.value = election
    }

    fun displayVoterInfoComplete() {
        _navigateToVoterInfo.value = null
    }

    fun refreshElections() {
        getSavedElectionsFromLocalDatabase()
    }
}