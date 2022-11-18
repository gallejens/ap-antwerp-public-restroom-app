package edu.ap.mobile_development_project.services

import android.util.Log
import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import edu.ap.mobile_development_project.entities.Toilet
import okhttp3.*
import okio.IOException
import java.io.StringReader
import java.net.URL

class Properties(
    @Json(name = "ID")
    val id: Int?

)

class Geometry(
    val coordinates: Array<Any>?
)


data class ToiletJSON(
    val geometry: Geometry,
    val properties: Properties

) {
    override fun toString(): String = "Toilet( coordinates=[${geometry.coordinates?.get(0)}, ${geometry.coordinates?.get(1)}], id=${properties.id}  )"
}





class DataMigrationService {

    private val client = OkHttpClient()

    private fun getRequest(url: String): String? {
        var result: String? = null
        try {
            val request = Request.Builder().url(url).build()   // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
        }
        catch(err:Error) {
            print("Error when executing get request: "+err.localizedMessage)
        }
        return result
    }

    fun getJsonData() : List<Toilet> {

        val toiletList = mutableListOf<Toilet>()

        val klaxonInstance = Klaxon()
        val jsonData = getRequest("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson")
        var parsedString = klaxonInstance.parseJsonObject(StringReader(jsonData));
        val toiletArray = parsedString.array<Any>("features");

        val toilets = toiletArray?.let { klaxonInstance.parseFromJsonArray<ToiletJSON>(it) }

        println(toilets)

        for (toilet: ToiletJSON in toilets!!) {
            println(toilet.toString())
        }


        return toiletList
    }

}