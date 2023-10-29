package com.example.lifecalculator

import android.location.Geocoder
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lifecalculator.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import android.content.Context
import android.location.Address
import com.google.android.gms.maps.model.LatLng


class ViewModel: ViewModel() {

    private val _country: MutableLiveData<List<CountryData>> = MutableLiveData(emptyList())
    val country: LiveData<List<CountryData>> = _country

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> = _selectedDate

    private val _lifeExpectancy = MutableLiveData<Double>()
    val lifeExpectancy = _lifeExpectancy

    private val _location = MutableLiveData<String>()
    val location = _location

    init{
        _lifeExpectancy.value = 80.00
        _selectedDate.value = Date()
        _location.value ="United States"
    }


    fun updateCountryName(country:String){
        _location.value = country
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        _selectedDate.value = calendar.time
    }

    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val response = RetrofitInstance.api.getCountryData()
                _country.postValue(response)
            }
            catch (e: Exception){
                Log.e("ViewModel", "Error fetching data: ${e.message}")
            }
        }
    }


}