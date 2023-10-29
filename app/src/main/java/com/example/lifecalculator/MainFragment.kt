package com.example.lifecalculator

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.lifecalculator.network.AppPermissions
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel : ViewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        viewModel.fetchData()
        val mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val textView: TextView? = view.findViewById(R.id.tvLifeCalculator)
        val btn: Button = view.findViewById(R.id.button)
        val datePicker: DatePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val permission = AppPermissions()
        permission.requestLocationPermission(requireActivity())


        viewModel.selectedDate.observe(
            viewLifecycleOwner,
        ) { date ->
            val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
            textView?.text = formattedDate
        }


        if (!permission.isLocationOk(requireActivity())) {
            permission.requestLocationPermission(requireActivity())
        }else{
            mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                val location: Location? = task.result
                if (location != null) {
                    val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                    val list =
                        geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    list?.get(0)?.let { viewModel.updateCountryName(it.countryName) }

                }
            }
        }


        datePicker.setOnDateChangedListener{view, year, monthOfYear, dayOfMonth ->
            viewModel.updateDate(year, monthOfYear, dayOfMonth)
        }

        btn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_secondaryFragment)
        }



    }
}