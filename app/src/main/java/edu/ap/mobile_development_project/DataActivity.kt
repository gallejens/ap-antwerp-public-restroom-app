package edu.ap.mobile_development_project

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import okhttp3.*
import okio.IOException
import java.io.StringReader

class Properties(
    @Json(name = "ID")
    val id: Int?

)

class Geometry(
    val coordinates: Array<Any>?
)


data class Toilet(
    val geometry: Geometry,
    val properties: Properties

) {
    override fun toString(): String = "Toilet( coordinates=[${geometry.coordinates?.get(0)}, ${geometry.coordinates?.get(1)}], id=${properties.id}  )"
}





class DataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val btn_data = findViewById<Button>(R.id.button2);

        btn_data.setOnClickListener {
            println("Initiating data flow");
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson")
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("OUR_APP", e.toString())
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        val body: String = response.body!!.string();

                        println(body)
                        val klaxonInstance = Klaxon()

                        var parsedString = klaxonInstance.parseJsonObject(StringReader(body));

                        val toiletArray = parsedString.array<Any>("features");

                        val toilets = toiletArray?.let { klaxonInstance.parseFromJsonArray<Toilet>(it) }

                        println(toilets)

                        for (toilet: Toilet in toilets!!) {
                            println(toilet.toString())
                        }

                    }
                }
            })



        }
    }
}