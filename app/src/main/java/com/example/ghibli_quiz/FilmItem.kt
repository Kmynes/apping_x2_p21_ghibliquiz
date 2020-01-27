package com.example.ghibli_quiz

data class FilmItem (
    val id:String,
    val title:String,
    val description:String,
    val director:String,
    val producer:String,
    val release_date:Int,
    val rt_score: Int
)