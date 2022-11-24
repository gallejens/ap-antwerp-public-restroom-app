package edu.ap.mobile_development_project

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.util.ArrayList
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlay
import org.osmdroid.views.overlay.OverlayItem

class MapActivity : Activity() {
    lateinit var bottomNav: BottomNavigationView

    private lateinit var mMapView: MapView
    private var mMyLocationOverlay: ItemizedOverlay<OverlayItem>? = null
    private var items = ArrayList<OverlayItem>()
    private val urlNominatim = "https://nominatim.openstreetmap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Problem with SQLite db, solution :
        // https://stackoverflow.com/questions/40100080/osmdroid-maps-not-loading-on-my-device
        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = packageName
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConfig.osmdroidBasePath = basePath
        val tileCache = File(osmConfig.osmdroidBasePath, "tile")
        osmConfig.osmdroidTileCache = tileCache

        mMapView = findViewById(R.id.map_view)

        // Permissions
        if (hasPermissions()) {
            initMap()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }

        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.map
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.map -> {
                    true
                }
                else -> false
            }
        }
    }

    private fun hasPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (hasPermissions()) {
                initMap()
            } else {
                println("finish")
                finish()
            }
        }
    }

    private fun initMap() {
        mMapView?.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)


        mMapView?.controller?.setZoom(17.0)
        // default = Ellermanstraat 33
        setCenter(GeoPoint(51.23020595, 4.41655480828479), "Campus Ellermanstraat")
    }

    private fun addMarker(geoPoint: GeoPoint, name: String) {
        items.add(OverlayItem(name, name, geoPoint))
        mMyLocationOverlay = ItemizedIconOverlay(items, null, applicationContext)
        mMapView?.overlays?.add(mMyLocationOverlay)
    }

    private fun setCenter(geoPoint: GeoPoint, name: String) {
        mMapView?.controller?.setCenter(geoPoint)
        addMarker(geoPoint, name)
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }
}