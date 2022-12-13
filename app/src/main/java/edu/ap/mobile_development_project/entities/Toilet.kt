package edu.ap.mobile_development_project.entities

enum class Gender(val value: Int) {
    MALE(1),
    FEMALE(2),
    UNISEX(3),
    UNKNOWN(4);

    companion object {
        fun fromInt(value: Int) = Gender.values().first { it.value == value }
    }
}

class Toilet (
    val id: Int?,
    val description : String?,
    val opening_hours : String?,
    val gender : Gender? ,
    val address : String?,
    val wheelchair : Boolean? ,
    val baby : Boolean?,
    val longitude : Double? ,
    val latitude : Double? ,
    var distance : Float?,
) {
    override fun toString(): String {
        return "Toilet(id=$id, description=$description, opening_hours=$opening_hours"
    }
}