package com.chen.vax.dashplay.ui.Main


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chen.vax.dashplay.R
import kotlinx.android.synthetic.main.dash.*
import org.json.JSONObject

class DashboardFragment : Fragment(),SensorEventListener{

    private lateinit var localMan: LocationManager
    lateinit var localgps: Location
    private var check = false

    private var topSpeed:Double=0.0

    private var h:Int=0

    lateinit var sensorManager: SensorManager
    lateinit var accelerometer: Sensor
    lateinit var magnetometer: Sensor

    var currentDegree = 0.0f
    var lastAccelerometer = FloatArray(3)
    var lastMagnetometer = FloatArray(3)
    var lastAccelerometerSet = false
    var lastMagnetometerSet = false




    var miOrKm: Boolean = false  //false  km  true  mi


    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.dash, container, false)
        val wea: TextView = root.findViewById(R.id.wea)

        dashboardViewModel.text.observe(this, Observer {
            wea.text = weatherinfo(getLocal().latitude.toString(),getLocal().longitude.toString())

        })
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sp.text=getLocal().speed.toString()+"km/h"
        sp.setOnClickListener {
            if (sp.text.contains("mph")) {
                sp.text =""+ getCurrentSpeed(getLocal().speed)+"km/h"
                miOrKm = true
            } else {
                sp.text =""+ getCurrentSpeed(getLocal().speed)+"mph"
                miOrKm = false
            }
        }
        sensorManager = activity!!.getSystemService(AppCompatActivity.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        start.setOnClickListener {
            time.setBase(SystemClock.elapsedRealtime())
             h = ((SystemClock.elapsedRealtime() - time.getBase()) / 1000 / 60).toInt()
            Toast.makeText(activity,"Your trip starts!",Toast.LENGTH_LONG).show()
            time.start()
        }


        stop.setOnClickListener {
            time.stop()
            h = ((SystemClock.elapsedRealtime() - time.getBase()) / 1000).toInt()
            time.stop()

            if (getLocal().speed >= topSpeed) {
                Toast.makeText(activity, "Your trip ends in " + h.toString() + "s"+"\nTop Speed: " + getLocal().speed + "km/h", Toast.LENGTH_LONG).show()

            } else {
                Toast.makeText(activity, "Your trip ends in " + h.toString() + "s"+"\nTop Speed: " + topSpeed + "km/h", Toast.LENGTH_LONG).show()
            }
        }


        dis.text="Traveled Distance: "+getDistance()+"km"

    }


    private fun getCurrentSpeed(speed: Float):String {
        if (miOrKm) {
            return (speed / 0.6214).toInt().toString()
        } else {
            return speed.toInt().toString()
        }
    }


    @SuppressLint("MissingPermission")
    fun getLocal():Location {
        localMan =context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        check = localMan.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (check) {
            localMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0F, object :
                LocationListener {
                override fun onLocationChanged(location: Location?) {
                    if (location != null) {
                        localgps = location
                    }
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

                }

                override fun onProviderEnabled(provider: String?) {

                }

                override fun onProviderDisabled(provider: String?) {

                }
            })
            val localGpsLocation = localMan.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (localGpsLocation != null)
                localgps = localGpsLocation
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
        return localgps

    }



    fun getDistance():Int {

        localgps.latitude=0.0
        localgps.longitude=0.0

        val lat1 = (Math.PI / 180) * getLocal().latitude
        val lat2 = (Math.PI / 180) * localgps.latitude

        val lon1 = (Math.PI / 180) * getLocal().longitude
        val lon2 = (Math.PI / 180) * localgps.longitude

        var R = 6371
        var d:Double
        d = Math.acos(
            Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)
        ) * R;

        d +=d

        return (d*1000*1000).toInt()

    }

    fun weatherinfo(lat:String, lon: String): String {
        val url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat+ "&lon=" + lon+ "&appid=10a55a4d082b785aa8185b3fa50b35f9&units=metric"

        Volley.newRequestQueue(getActivity()).add(
            StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                val main = JSONObject(response.toString()).getJSONObject("main")
                val tem=main.getDouble("temp").toInt().toString()

                val weather= JSONObject(response.toString()).getJSONArray("weather")
                val des= weather.getJSONObject(0).getString("main")

                val name= JSONObject(response.toString()).getString("name")

                wea.text= ""+tem+"℃\t\t"+des+"\n"+name
            }, Response.ErrorListener {

                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show()
            })
        )

        return wea.toString()
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this, accelerometer)
        sensorManager.unregisterListener(this, magnetometer)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor === accelerometer) {
            lowPass(event.values, lastAccelerometer)
            lastAccelerometerSet = true
        } else if (event.sensor === magnetometer) {
            lowPass(event.values, lastMagnetometer)
            lastMagnetometerSet = true
        }

        if (lastAccelerometerSet && lastMagnetometerSet) {
            val r = FloatArray(9)
            if (SensorManager.getRotationMatrix(r, null, lastAccelerometer, lastMagnetometer)) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(r, orientation)
                val degree = (Math.toDegrees(orientation[0].toDouble()) + 360).toFloat() % 360

                val rotateAnimation = RotateAnimation(
                    currentDegree,
                    -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f)
                rotateAnimation.duration = 1000
                rotateAnimation.fillAfter = true

                comp.startAnimation(rotateAnimation)
                currentDegree = -degree
            }
        }
    }

    fun lowPass(input: FloatArray, output: FloatArray) {
        val alpha = 0.05f

        for (i in input.indices) {
            output[i] = output[i] + alpha * (input[i] - output[i])
        }
    }







}