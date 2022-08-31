package com.example.fragments

import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.detailslist.R
import org.json.JSONObject
import java.net.URL
import java.util.*


class WeatherFragment : Fragment() {
    private var seconds: Int = 0
    private var name: String? = null
    private var running: Boolean? = false
    private var wasRunning: Boolean? = false
    private lateinit var mRequestQueue: RequestQueue
    val CITY: String = "dhaka,bd"
    val API: String = "06c921750b9a82d8f5d1294e1586276f" // Use API key
    private var geo_X: Double? = null
    private var geo_Y: Double? = null

    companion object {
        fun newInstance(): WeatherFragment {
            return WeatherFragment()
        }
    }

    fun sendData(x: Double, y: Double) {

        geo_X = x
        geo_Y = y
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun onClickSave() {
        running = false
        mRequestQueue = Volley.newRequestQueue(activity)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds")
            running = savedInstanceState.getBoolean("running")
            wasRunning = savedInstanceState.getBoolean("wasRunning")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.weather_stoper, container, false)
        weatherTask(view).execute()


        return view
    }


    inner class weatherTask(view: View) : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=$geo_X&lon=$geo_Y&&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                /* Populating extracted data into our views */
                view?.findViewById<TextView>(R.id.address)!!.text = address
                view?.findViewById<TextView>(R.id.updated_at)!!.text = updatedAtText
                view?.findViewById<TextView>(R.id.status)!!.text = weatherDescription.capitalize()
                view?.findViewById<TextView>(R.id.temp)!!.text = temp
                view?.findViewById<TextView>(R.id.temp_min)!!.text = tempMin
                view?.findViewById<TextView>(R.id.temp_max)!!.text = tempMax
                view?.findViewById<TextView>(R.id.sunrise)!!.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                view?.findViewById<TextView>(R.id.sunset)!!.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))
                view?.findViewById<TextView>(R.id.wind)!!.text = windSpeed
                view?.findViewById<TextView>(R.id.pressure)!!.text = pressure
                view?.findViewById<TextView>(R.id.humidity)!!.text = humidity


            } catch (e: Exception) {
                println("errror")
            }

        }
    }

    override fun onPause() {
        super.onPause()
        wasRunning = running
        running = false
    }

    override fun onResume() {
        super.onResume()
        if (wasRunning == true) {
            running = true
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("seconds", seconds)
        outState.putBoolean("running", running!!)
        outState.putBoolean("wasRunning", wasRunning!!)
    }


}