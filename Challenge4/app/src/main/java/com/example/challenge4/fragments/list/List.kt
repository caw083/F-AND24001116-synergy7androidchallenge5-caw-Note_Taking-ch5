package com.example.challenge4.fragments.list

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.challenge4.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.challenge4.data.DataStoreManager
import com.example.challenge4.databinding.FragmentListBinding
import com.example.weatherapi.weathergpsfolder.RetrofitClient
import com.example.weatherapi.weathergpsfolder.WeatherApi
import com.example.weatherapi.weathergpsfolder.weatherdata.WeatherResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class List : Fragment() {
    private val viewModel: ListViewModel by viewModels()
    private lateinit var binding: FragmentListBinding
    private lateinit var dataStoreManager: DataStoreManager
    private val apiKey = "b6f8d522644b7834a7abc01d5826a1ff"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.listBindingModel = ListBinding().apply {
            logoutBindingText = "Log Out"
        }
        val view = binding.root

        val fab: FloatingActionButton = view.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_list2_to_add3)
        }

        val notesData = viewModel.getAllNotes()
        notesData.observe(viewLifecycleOwner) { notes ->
            val size = notes.size
            showToast("Size of notes list: $size")
            val stringBuilder = StringBuilder()
            for (note in notes) {
                stringBuilder.append("Title: ${note.title}, Description: ${note.description}, Priority: ${note.priority}\n")
            }
            val dataString = stringBuilder.toString()
            showToast(dataString)

            val notesAdapter = NotesAdapter(requireContext(), notes, viewModel)
            val recyclerView: RecyclerView = view.findViewById(R.id.recyclerviewNotes)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = notesAdapter
        }

        // Weather related UI elements
        val tvTemperature: TextView = view.findViewById(R.id.tvTemperature)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvHumidity: TextView = view.findViewById(R.id.tvHumidity)
        val tvWindSpeed: TextView = view.findViewById(R.id.tvWindSpeed)
        val tvPressure: TextView = view.findViewById(R.id.tvPressure)
        val tvVisibility: TextView = view.findViewById(R.id.tvVisibility)
        val tvCloudiness: TextView = view.findViewById(R.id.tvCloudiness)
        val tvRain: TextView = view.findViewById(R.id.tvRain)
        val tvDateTime: TextView = view.findViewById(R.id.tvDateTime)

        val logout: Button = view.findViewById(R.id.logoutButton)
        dataStoreManager = DataStoreManager(requireContext())
        logout.setOnClickListener {
            lifecycleScope.launch {
                logoutLogic()
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        // Check for location permission on resume
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission()
        } else {
            // Permission already granted, proceed to get location and fetch weather
            getLocationAndFetchWeather()
        }
    }

    private fun requestLocationPermission() {
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
    }

    private suspend fun logoutLogic() {
        // Retrieve user data from DataStore
        val currentUserData = dataStoreManager.userData.first()

        // Update isLogin field in DataStore to false
        currentUserData?.let {
            val updatedUserData = it.copy(isLogin = false)
            dataStoreManager.storeUserData(updatedUserData)
        }
        // Navigate to the login screen
        findNavController().navigate(R.id.action_list2_to_login)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun getLocationAndFetchWeather() {
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        location?.let {
            val latitude = it.latitude
            val longitude = it.longitude

            val geocoder = Geocoder(requireContext())
            try {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val cityName = addresses[0].locality
                    val tvCity: TextView = view?.findViewById(R.id.tvCity)!!
                    tvCity.text = cityName

                    val tvTemperature: TextView = view?.findViewById(R.id.tvTemperature)!!
                    val tvDescription: TextView = view?.findViewById(R.id.tvDescription)!!
                    val tvHumidity: TextView = view?.findViewById(R.id.tvHumidity)!!
                    val tvWindSpeed: TextView = view?.findViewById(R.id.tvWindSpeed)!!
                    val tvPressure: TextView = view?.findViewById(R.id.tvPressure)!!
                    val tvVisibility: TextView = view?.findViewById(R.id.tvVisibility)!!
                    val tvCloudiness: TextView = view?.findViewById(R.id.tvCloudiness)!!
                    val tvRain: TextView = view?.findViewById(R.id.tvRain)!!
                    val tvDateTime: TextView = view?.findViewById(R.id.tvDateTime)!!

                    fetchWeather(latitude, longitude, tvTemperature, tvDescription, tvHumidity, tvWindSpeed, tvPressure, tvVisibility, tvCloudiness, tvRain, tvDateTime)
                } else {
                    showToast("City name not found")
                }
            } catch (e: IOException) {
                showToast("Geocoding error: ${e.message}")
            }
        } ?: run {
            showToast("Unable to retrieve GPS coordinates")
        }
    }

    private fun fetchWeather(
        latitude: Double,
        longitude: Double,
        tvTemperature: TextView,
        tvDescription: TextView,
        tvHumidity: TextView,
        tvWindSpeed: TextView,
        tvPressure: TextView,
        tvVisibility: TextView,
        tvCloudiness: TextView,
        tvRain: TextView,
        tvDateTime: TextView
    ) {
        val weatherApi = RetrofitClient.getClient().create(WeatherApi::class.java)
        val call = weatherApi.getWeatherForecast(latitude, longitude, apiKey)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    weatherResponse?.let {
                        val forecast = it.list.firstOrNull()
                        forecast?.let { firstForecast ->
                            val description = firstForecast.weather.firstOrNull()?.description ?: "No description"
                            val temp = firstForecast.main.temp - 273.15 // Convert Kelvin to Celsius
                            val humidity = firstForecast.main.humidity
                            val windSpeed = firstForecast.wind.speed
                            val pressure = firstForecast.main.pressure
                            val visibility = firstForecast.visibility
                            val cloudiness = firstForecast.clouds.all
                            val rainVolume = firstForecast.rain?.`3h` ?: 0.0
                            val dateTime = firstForecast.dt_txt

                            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                            val outputFormat = SimpleDateFormat("EEE, MMM d, yyyy h:mm a", Locale.getDefault())
                            val date: Date = inputFormat.parse(dateTime)
                            val formattedDate: String = outputFormat.format(date)

                            tvTemperature.text = "Temperature: %.2f°C".format(temp)
                            tvDescription.text = "Description: $description"
                            tvHumidity.text = "Humidity: $humidity%"
                            tvWindSpeed.text = "Wind Speed: $windSpeed m/s"
                            tvPressure.text = "Pressure: $pressure hPa"
                            tvVisibility.text = "Visibility: $visibility m"
                            tvCloudiness.text = "Cloudiness: $cloudiness%"
                            tvRain.text = "Rain Volume (3h): $rainVolume mm"
                            tvDateTime.text = "Date & Time: $formattedDate"
                        }
                    }
                } else {
                    showToast("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                showToast("Failure: ${t.message}")
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndFetchWeather()
            } else {
                showToast("Location permission denied")
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
