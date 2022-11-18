package edu.ap.mobile_development_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import edu.ap.mobile_development_project.entities.Toilet
import edu.ap.mobile_development_project.services.DatabaseService

class MainActivity : AppCompatActivity() {
    private lateinit var mapButton: Button

    private val database = DatabaseService(this)
    private lateinit var toiletList: List<Toilet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        mapButton = findViewById<Button>(R.id.map_button)
        mapButton.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        if (database.readData().isEmpty()) {

        }


    }
}