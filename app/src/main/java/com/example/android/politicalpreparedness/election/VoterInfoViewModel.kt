package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.database.ElectionDao
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.AdministrationBody
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class VoterInfoViewModel(private val dataSource: ElectionDao) : ViewModel() {

    private val _election = MutableLiveData<Election>()
    val election: LiveData<Election> get() = _election

    private val _administrationBody = MutableLiveData<AdministrationBody>()
    val administrationBody: LiveData<AdministrationBody> get() = _administrationBody

    private val _isElectionSaved = MutableLiveData<Boolean>()
    val isElectionSaved: LiveData<Boolean> get() = _isElectionSaved

    private val _url = MutableLiveData<String?>()
    val url: LiveData<String?> get() = _url

    private val apiService = CivicsApi.retrofitService

    fun navigateToUrl(url: String) {
        _url.value = url
    }

    fun navigateToUrlComplete() {
        _url.value = null
    }

    fun getVoterInformation(electionId: Int, division: Division) {
        viewModelScope.launch {
            try {
                val savedElection = withContext(Dispatchers.IO) {
                    dataSource.getElectionById(electionId)
                }
                _isElectionSaved.value = savedElection != null

                val address = "${division.state}, ${division.country}"
                val voterInfoResponse = apiService.getVoterInfo(address, electionId)

                _election.value = voterInfoResponse.election
                _administrationBody.value =
                    voterInfoResponse.state?.first()?.electionAdministrationBody

            } catch (exception: Exception) {
                Timber.e("Error: %s", exception.localizedMessage)
            }
        }
    }

    fun onFollowButtonClicked() {
        viewModelScope.launch {
            _election.value?.let {
                if (_isElectionSaved.value == true) {
                    withContext(Dispatchers.IO) {
                        dataSource.deleteElection(it)
                    }
                    _isElectionSaved.value = false
                } else {
                    withContext(Dispatchers.IO) {
                        dataSource.insertElection(it)
                    }
                    _isElectionSaved.value = true
                }
            }
        }
    }

}