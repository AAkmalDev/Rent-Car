@file:Suppress("DEPRECATION")

package uz.pixyz.rentcar

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import uz.pixyz.rentcar.databinding.ActivityMapsBinding
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(),
    OnMapReadyCallback,
    GoogleMap.OnMapClickListener,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private lateinit var mapsBinding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationPermissionGranted = false
    private var mLastLocation: Location? = null
    private var mCurrLocationMarker: Marker? = null
    private var PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String? = null
    private var city: String? = null
    private var country: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(mapsBinding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()

        val manager = this.getSystemService(LOCATION_SERVICE) as LocationManager

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder(this)
                .setMessage("GPS Enable")
                .setPositiveButton("Settings"
                ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
                .setNegativeButton("Cancel", null)
                .show()
        } else {
            Toast.makeText(this, "GPS is Enable!", Toast.LENGTH_LONG).show();
        }

        mapsBinding.button.setOnClickListener {
            if (address != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("maps", "${address}")
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Joyni belgilang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        googleApiClient?.connect()
    }


    override fun onResume() {
        super.onResume()
        if (googleApiClient == null || !googleApiClient?.isConnected!!) {
            buildApiClient()
            googleApiClient?.connect()
        }
    }

    private fun buildApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        buildApiClient()
        mMap.setOnMapClickListener(this)
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onMapClick(p0: LatLng?) {
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker!!.remove()
            latitude = p0!!.latitude
            longitude = p0.longitude
            val geocoder: Geocoder
            val addresses: List<Address>?
            geocoder = Geocoder(this, Locale.getDefault())
            latitude = p0.latitude
            longitude = p0.longitude
            Log.e("latitude", "latitude--$latitude")
            try {
                Log.e("latitude", "inside latitude--$latitude")
                addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (addresses != null && addresses.size > 0) {
                    address = addresses[0].getAddressLine(0)
                    city = addresses[0].locality
                    country = addresses[0].countryName
                    Log.d("Pizyas", "$address $city $country")
                    Log.d("Pizyas1", addresses[0].toString())
                }
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            mCurrLocationMarker =
                mMap.addMarker(MarkerOptions().position(LatLng(latitude!!, longitude!!)))
            mCurrLocationMarker!!.title = "${address}/${city} "
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude!!, longitude!!),
                    15f
                )
            )
            mapsBinding.mapsAddress.text = "${address}/${city} "
        } else {
            latitude = p0!!.latitude
            longitude = p0.longitude
            val geocoder: Geocoder
            val addresses: List<Address>?
            geocoder = Geocoder(this, Locale.getDefault())
            latitude = p0.latitude
            longitude = p0.longitude
            Log.e("latitude", "latitude--$latitude")
            try {
                Log.e("latitude", "inside latitude--$latitude")
                addresses = geocoder.getFromLocation(latitude!!, longitude!!, 1)
                if (addresses != null && addresses.size > 0) {
                    address = addresses[0].getAddressLine(0)
                    city = addresses[0].locality
                    country = addresses[0].countryName
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            mCurrLocationMarker =
                mMap.addMarker(MarkerOptions().position(LatLng(latitude!!, longitude!!)))
            mCurrLocationMarker!!.title = "${address}/${city} "
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(latitude!!, longitude!!),
                    15f
                )
            )
            mapsBinding.mapsAddress.text = "${address}/${city} "
        }

    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }


    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
    }

    override fun onLocationChanged(p0: Location?) {
        mLastLocation = p0
        if (mCurrLocationMarker == null) {
            mCurrLocationMarker!!.remove()
        }
        val latLng = LatLng(p0!!.getLatitude(), p0.getLongitude())
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val provider = locationManager.getBestProvider(Criteria(), true)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val locations = locationManager.getLastKnownLocation(provider!!)
        val providerList = locationManager.allProviders
        if (null != locations && providerList.size > 0) {
            val longitude = locations.longitude
            val latitude = locations.latitude
            val geocoder = Geocoder(
                applicationContext,
                Locale.getDefault()
            )
            try {
                val listAddresses = geocoder.getFromLocation(
                    latitude,
                    longitude, 1
                )
                if (null != listAddresses && listAddresses.size > 0) {
                    val state = listAddresses[0].adminArea
                    val country = listAddresses[0].countryName
                    val subLocality = listAddresses[0].subLocality
                    markerOptions.title(
                        "" + latLng + "," + subLocality + "," + state
                                + "," + country
                    )
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        mCurrLocationMarker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f))
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                googleApiClient,
                this
            )
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("Not yet implemented")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("Not yet implemented")
    }

}