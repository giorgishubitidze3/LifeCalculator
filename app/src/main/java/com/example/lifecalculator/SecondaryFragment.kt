package com.example.lifecalculator

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class SecondaryFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_secondary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel : ViewModel = ViewModelProvider(requireActivity())[ViewModel::class.java]
        val ageText: TextView = view.findViewById(R.id.tvYourAge1)
        val timeText: TextView = view.findViewById(R.id.tvYourAge2)
        val leftAgeText: TextView = view.findViewById(R.id.tvAgeLeft1)
        val leftTimeText: TextView = view.findViewById(R.id.tvAgeLeft2)
        val tvLifeExpectancy: TextView = view.findViewById(R.id.tvAgeExpectancy)
        var birthdate: Date? = Date()
        var countryData: List<CountryData>? = null
        var ageExpectancy: Double = 80.00
        var countryName: String = "United States"





        val handler = Handler(Looper.getMainLooper())

        viewModel.location.observe(viewLifecycleOwner){location ->
            countryName = location
        }

        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            birthdate = date
        }

        viewModel.lifeExpectancy.observe(viewLifecycleOwner){age ->
            ageExpectancy = age
        }

        viewModel.country.observe(viewLifecycleOwner){country ->
            countryData = country
        }


        val lifeBoth: Double? = countryData?.find { it.country == countryName }?.lifeBoth

        if (lifeBoth != null) {
            ageExpectancy = lifeBoth
        }
        fun updateAge(birthdate: Date, targetAgeInYears: Int){
            val currentDate = Date()

            val ageInSeconds = (currentDate.time - birthdate.time) / 1000
            val ageInYears = ageInSeconds / (60 * 60 * 24 * 365)
            val ageInMonths = (ageInSeconds / (60 * 60 * 24 * 30)).toInt()
            val ageInDays = (ageInSeconds / (60 * 60 * 24)).toInt()
            val ageInHours = (ageInSeconds / (60 * 60)).toInt()
            val ageInMinutes = (ageInSeconds / 60).toInt()
            val ageInSecondsRemaining = ageInSeconds.toInt()

            val remainingYears = targetAgeInYears - ageInYears
            val remainingMonths = (targetAgeInYears * 12) - ageInMonths
            val remainingDays = (targetAgeInYears * 12 * 30) - ageInDays
            val remainingHours = (targetAgeInYears * 12 * 30 * 24) - ageInHours
            val remainingMinutes = (targetAgeInYears * 12 * 30 * 24 * 60) - ageInMinutes
            val remainingSeconds = (targetAgeInYears * 12 * 30 * 24 * 60 * 60)  - ageInSecondsRemaining


            handler.post {
                ageText.text = "$ageInYears years\n $ageInMonths months\n $ageInDays days"
                timeText.text = "$ageInHours hours\n $ageInMinutes minutes\n $ageInSecondsRemaining seconds"

                leftAgeText.text="$remainingYears years\n $remainingMonths months\n $remainingDays days"
                leftTimeText.text="$remainingHours hours\n $remainingMinutes minutes\n $remainingSeconds seconds"
            }
        }


        tvLifeExpectancy.text = "${viewModel.location.value} ${ageExpectancy.toInt()} years"


        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                birthdate?.let { updateAge(it, ageExpectancy.toInt()) }
            }
        }, 0, 1000)



    }

}