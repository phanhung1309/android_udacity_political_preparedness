package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch
import timber.log.Timber

class RepresentativeViewModel : ViewModel() {

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address?>()
    val address: LiveData<Address?>
        get() = _address

    private val apiService = CivicsApi.retrofitService

    init {
        _address.value = Address("", "", "", "New York", "")
    }

    fun getRepresentativesList(address: Address?) {
        viewModelScope.launch {
            _representatives.value = emptyList()
            if (address != null) {
                try {
                    _address.value = address
                    val (offices, officials) = apiService.getRepresentatives(_address.value?.toFormattedString()!!)
                    _representatives.value =
                        offices.flatMap { office -> office.getRepresentatives(officials) }
                } catch (exception: Exception) {
                    Timber.e("Error: %s", exception.localizedMessage)
                }
            }
        }
    }

    fun getRepresentativesList() {
        Timber.d("address: %s", _address.value)
        getRepresentativesList(_address.value)
    }
}
