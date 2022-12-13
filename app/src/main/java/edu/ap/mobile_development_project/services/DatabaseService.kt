package edu.ap.mobile_development_project.services

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import edu.ap.mobile_development_project.entities.Gender
import edu.ap.mobile_development_project.entities.Toilet


const val DATABASE_NAME = "toilet.db"
const val TABLE_NAME = "toilet_data"

const val COLUMN_ID = "id"

const val COLUMN_DESCRIPTION = "description"
const val COLUMN_OPENING_HOURS = "opening_hours"
const val COLUMN_GENDER = "gender"
const val COLUMN_ADDRESS = "address"
const val COLUMN_WHEELCHAIR = "wheelchair"
const val COLUMN_BABY = "baby"
const val COLUMN_LOCATION = "location"

class DatabaseService(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DESCRIPTION VARCHAR(256), " +
                "$COLUMN_OPENING_HOURS VARCHAR(256)," +
                "$COLUMN_GENDER BOOLEAN," +
                "$COLUMN_ADDRESS VARCHAR(256)," +
                "$COLUMN_WHEELCHAIR BOOLEAN," +
                "$COLUMN_BABY VARCHAR(256)," +
                "$COLUMN_LOCATION VARCHAR(256)" +
                ")" )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun wipeData() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_NAME")
    }

    fun insertData(toilet: Toilet) {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DESCRIPTION, toilet.description)
        contentValues.put(COLUMN_OPENING_HOURS, toilet.opening_hours)
        contentValues.put(COLUMN_GENDER, toilet.gender?.value)
        contentValues.put(COLUMN_ADDRESS, toilet.address)
        contentValues.put(COLUMN_WHEELCHAIR, toilet.wheelchair)
        contentValues.put(COLUMN_BABY, toilet.baby)
        contentValues.put(COLUMN_LOCATION, "${toilet.longitude};${toilet.latitude}")
        val insertResult = this.writableDatabase.insert(TABLE_NAME, null, contentValues)
        if(insertResult == (-1).toLong())
            println("[DATABASE] Database insert failed.")
        else
            println("[DATABASE] Database insert succeeded.")
    }

    @SuppressLint("Range")
    fun readData() : MutableList<Toilet> {

        val toiletList : MutableList<Toilet> = ArrayList()
        val result = this.readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (result.moveToFirst()) {
            do {
                val splitLocation = result.getString(result.getColumnIndex(COLUMN_LOCATION)).split(";")

                val toilet = Toilet(
                    result.getInt(result.getColumnIndex(COLUMN_ID)),
                    result.getString(result.getColumnIndex(COLUMN_DESCRIPTION)),
                    result.getString(result.getColumnIndex(COLUMN_OPENING_HOURS)),
                    Gender.fromInt(result.getInt(result.getColumnIndex(COLUMN_GENDER))),
                    result.getString(result.getColumnIndex(COLUMN_ADDRESS)),
                    result.getInt(result.getColumnIndex(COLUMN_WHEELCHAIR)) == 1,
                    result.getInt(result.getColumnIndex(COLUMN_BABY)) == 1,
                    splitLocation[0].toDouble(),
                    splitLocation[1].toDouble(),
                    0f,
                )

                toiletList.add(toilet)
            } while (result.moveToNext())
        }

        return toiletList

    }



}