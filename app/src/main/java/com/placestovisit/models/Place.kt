package com.placestovisit.models

data class Place(
    val id : String = "",
    val title : String = "",
    val city : String = "",
    val description : String = "",
    var isExpandable : Boolean = true,
    var imageUrl : String = ""
)
