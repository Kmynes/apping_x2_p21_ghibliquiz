package com.example.ghibli_quiz

import java.io.Serializable

data class Activity_Data(
    val is_good_response: Boolean,
    val film_url:String,
    val info_film:List<String>
):Serializable