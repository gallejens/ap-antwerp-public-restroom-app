package edu.ap.mobile_development_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.ap.mobile_development_project.entities.Toilet
import edu.ap.mobile_development_project.services.DatabaseService

class MainActivity : AppCompatActivity() {
    lateinit var bottomNav: BottomNavigationView

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<ListViewAdapter.ViewHolder>? = null

    private val database = DatabaseService(this)
    private lateinit var toiletList: List<Toilet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //if (database.readData().isEmpty()) {
            // TODO: inflate db when empty
        //}

        val listRecyclerView: RecyclerView = findViewById(R.id.list_recycler_view)
        layoutManager = LinearLayoutManager(this)
        listRecyclerView.layoutManager = layoutManager

        // TODO: Replace tempdate with db read
        var tempData : List<Toilet> = listOf(Toilet(), Toilet())

        toiletList = tempData

        adapter = ListViewAdapter(tempData)
        listRecyclerView.adapter = adapter

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
    }
}