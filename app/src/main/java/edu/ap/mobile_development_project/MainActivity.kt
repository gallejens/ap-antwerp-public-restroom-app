package edu.ap.mobile_development_project

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.ap.mobile_development_project.entities.Toilet
import edu.ap.mobile_development_project.services.FilterService

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView

    private var layoutManager: RecyclerView.LayoutManager? = null

    private lateinit var toiletList: List<Toilet>
    private lateinit var listRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Permissions
        if (hasPermissions()) {
            initList()
        }
        else {
            requestPermissions()
        }
    }

    private fun initList() {
        listRecyclerView = findViewById(R.id.list_recycler_view)
        layoutManager = LinearLayoutManager(this)
        listRecyclerView.layoutManager = layoutManager

        FilterService.instance.build(this)
        updateToilets()

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    FilterService.instance.currentLocation = location
                    updateToilets()
                }
            }
        }

        // Navbar things
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.list
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list -> {
                    true
                }
                R.id.map -> {
                    startActivity(Intent(this, MapActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }

        // Filter related things
        val maleFilterCheckbox = findViewById<CheckBox>(R.id.maleFilter)
        val femaleFilterCheckbox = findViewById<CheckBox>(R.id.femaleFilter)
        val babyFilterCheckbox = findViewById<CheckBox>(R.id.babyFilter)
        val wheelchairFilterCheckbox = findViewById<CheckBox>(R.id.wheelchairFilter)

        maleFilterCheckbox.isChecked = FilterService.instance.requireMale
        femaleFilterCheckbox.isChecked = FilterService.instance.requireFemale
        babyFilterCheckbox.isChecked = FilterService.instance.requireBaby
        wheelchairFilterCheckbox.isChecked = FilterService.instance.requireWheelchair

        maleFilterCheckbox.setOnClickListener{
            FilterService.instance.requireMale = !FilterService.instance.requireMale
            updateToilets()
        }
        femaleFilterCheckbox.setOnClickListener{
            FilterService.instance.requireFemale = !FilterService.instance.requireFemale
            updateToilets()
        }
        babyFilterCheckbox.setOnClickListener{
            FilterService.instance.requireBaby = !FilterService.instance.requireBaby
            updateToilets()
        }
        wheelchairFilterCheckbox.setOnClickListener{
            FilterService.instance.requireWheelchair = !FilterService.instance.requireWheelchair
            updateToilets()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION), 100)
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
                initList()
            } else {
                finish()
            }
        }
    }

    private fun updateToilets() {
        toiletList = FilterService.instance.getAll()
        val adapter = ListViewAdapter(toiletList)
        listRecyclerView.adapter = adapter
    }
}