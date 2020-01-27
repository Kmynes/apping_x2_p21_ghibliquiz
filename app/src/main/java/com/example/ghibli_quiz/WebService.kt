package com.example.ghibli_quiz

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface WebService {
    @GET
    fun filmItem(@Url url:String): Call<FilmItem>

    @GET("people")
    fun listAllPeople():Call<List<PeopleItem>>
}