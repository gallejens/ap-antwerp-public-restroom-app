package edu.ap.mobile_development_project.services

import android.os.StrictMode
import android.util.Log
import com.beust.klaxon.*
import edu.ap.mobile_development_project.entities.Gender
import edu.ap.mobile_development_project.entities.Toilet
import okhttp3.*
import java.io.StringReader

class Properties(
    val id: Int?,
    val wheelchairAccessible: Boolean
)

class Geometry(
    val coordinates: Array<Any>?
)

data class ToiletJSON(
    val properties: JsonObject
)

class DataMigrationService {

    private fun getRequest(url: String): String? {
        var result: String? = null
        try {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val request = Request.Builder().url(url).build()   // Execute request
            val response = OkHttpClient().newCall(request).execute()
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

        val propertiesConverter = object: Converter {
            override fun canConvert(cls: Class<*>) = cls == Toilet::class.java

            override fun fromJson(jv: JsonValue): Toilet {

                val innerProperties: JsonObject? = jv.obj?.obj("properties")
                val innerGeometry: JsonObject? = jv.obj?.obj("geometry")


                val gender: Gender = when(innerProperties?.string("DOELGROEP")) {
                    "man" -> Gender.MALE
                    "vrouw" ->  Gender.FEMALE
                    "man/vrouw" ->  Gender.UNISEX
                    else -> Gender.UNKNOWN
                }

                var street = innerProperties?.string("STRAAT")
                if (street == null) {
                    street = "Unknown"
                }
                var houseNumber = innerProperties?.string("HUISNUMMER")
                if (houseNumber == null) {
                    houseNumber = ""
                }
                var zipCode = innerProperties?.int("POSTCODE")
                if (zipCode == null) {
                    zipCode = 2000
                }
                var district = innerProperties?.string("DISTRICT")
                if (district == null) {
                    district = "Antwerpen"
                }

                val address = "$street $houseNumber, $zipCode $district"

                return Toilet(
                    innerProperties?.int("ID"),
                    innerProperties?.string("OMSCHRIJVING"),
                    innerProperties?.string("OPENINGSUREN_OPM"),
                    gender,
                    address,
                    innerProperties?.string("INTEGRAAL_TOEGANKELIJK") == "ja",
                    innerProperties?.string("LUIERTAFEL") == "ja",
                    innerGeometry?.array<Double>("coordinates")?.get(0),
                    innerGeometry?.array<Double>("coordinates")?.get(1),
                    0f
                )
            }

            override fun toJson(value: Any) = TODO()
        }

        var parsedString = klaxonInstance.parseJsonObject(StringReader(jsonData));
        val toiletArray = parsedString.array<JsonObject>("features") ;

        toiletArray?.forEach {
            toiletList.add(klaxonInstance.converter(propertiesConverter).parseFromJsonObject<Toilet>(it)!!)
        }

        return toiletList
    }
}