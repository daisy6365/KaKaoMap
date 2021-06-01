package com.example.kakaomap

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity(){
    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapView = MapView(this)
        val mapViewContainer = map_view
        mapViewContainer.addView(mapView)

//        val marker = MapPOIItem()
//        marker.itemName = "현재위치"
//        marker.tag = 0
//        marker.mapPoint = MapPoint.mapPointWithGeoCoord(37.447947139378655, 126.71071137858627)
//        marker.markerType = MapPOIItem.MarkerType.BluePin
//        marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
//        mapView.addPOIItem(marker)

            val permissionCheck =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                val lm: LocationManager =
                    getSystemService(Context.LOCATION_SERVICE) as LocationManager
                try {
                    val userNowLocation: Location? =
                        lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                    val uLatitude = userNowLocation!!.latitude
                    val uLongitude = userNowLocation!!.longitude
                    val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    mapView.setMapCenterPoint(uNowPosition, true)

                    val marker = MapPOIItem()
                    marker.itemName = "현재위치"
                    marker.tag = 0
                    marker.mapPoint = MapPoint.mapPointWithGeoCoord(uLatitude, uLongitude)
                    marker.markerType = MapPOIItem.MarkerType.BluePin
                    marker.selectedMarkerType = MapPOIItem.MarkerType.RedPin
                    mapView.addPOIItem(marker)

                } catch (e: NullPointerException) {
                    Log.e("LOCATION_ERROR", e.toString())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        ActivityCompat.finishAffinity(this)
                    } else {
                        ActivityCompat.finishAffinity(this)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    System.exit(0)

                }
            } else {
                Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    PERMISSIONS_REQUEST_CODE
                )
            }

    }
}