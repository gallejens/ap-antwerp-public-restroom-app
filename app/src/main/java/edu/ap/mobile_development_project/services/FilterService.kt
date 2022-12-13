package edu.ap.mobile_development_project.services

import android.content.Context
import android.location.Location
import edu.ap.mobile_development_project.entities.Gender
import edu.ap.mobile_development_project.entities.Toilet
import kotlin.math.roundToInt

class FilterService {
    private var mDatabase: DatabaseService? = null

    var requireWheelchair = false
    var requireBaby = false
    var requireMale = false
    var requireFemale = false

    var currentLocation: Location? = null

    fun build(context: Context) {
        val database = DatabaseService(context)
        mDatabase = database


        if (database.readData().isEmpty()) {
            DataMigrationService().getJsonData()
                .forEach { database.insertData(it) }
        }
    }

    fun getAll(): List<Toilet> {
        val allToilets = mDatabase?.readData() ?: emptyList()
        val toiletsWithDistance = mutableListOf<Toilet>()
        for (t in allToilets) {
            val toiletLocation = Location("tLoc")
            toiletLocation.longitude = t.longitude!!
            toiletLocation.latitude = t.latitude!!
            var distance = currentLocation?.distanceTo(toiletLocation)
            if (distance == null) distance = 0f
            distance = (distance / 100f).roundToInt() / 10f
            t.distance = distance
            toiletsWithDistance.add(t)
        }
        toiletsWithDistance.sortBy { it.distance }

        return toiletsWithDistance
            .filter { !requireWheelchair || it.wheelchair == true }
            .filter { !requireBaby || it.baby == true }
            .filter { !requireMale || it.gender in listOf(Gender.UNISEX, Gender.MALE) }
            .filter { !requireFemale || it.gender in listOf(Gender.UNISEX, Gender.FEMALE) }
    }

    private fun recalculateDistances() {
        println("Hello")
    }

    companion object {
        val instance = FilterService()
    }
}