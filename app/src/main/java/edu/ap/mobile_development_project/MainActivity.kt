package edu.ap.mobile_development_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn_data = findViewById<Button>(R.id.button);

        btn_data.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }
    }
}