package com.example.ghibli_quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private fun fill_array_with_random(list: IntArray) {
        val l = (list.indices).toList()
        Collections.shuffle(l)
        for (i in 0..5)
            list[i] = l[i]
    }

    private fun call_second_request(service:WebService,
                                    info_films:MutableList<String>,
                                    people_list_to_display:MutableList<PeopleItem>,
                                    good_response:Int) {
        val ws_callback_film_item: Callback<FilmItem> = object : Callback<FilmItem>{
            override fun onFailure(call: Call<FilmItem>, t: Throwable) {
                Log.d("Info", "Error on get films")
            }

            override fun onResponse(call: Call<FilmItem>, response: Response<FilmItem>) {
                val codeSecondRequest = response.code()
                if (codeSecondRequest == 200) {
                    val body = response.body()
                    if (body != null) {
                        val msg = "Wich one of these character can be found in "
                        textView2.text = msg + "'" + body!!.title + "' " + "?"
                        info_films.add(body!!.director)
                        info_films.add(body!!.release_date.toString())
                        info_films.add(body!!.title)
                        info_films.add(body!!.description)
                        info_films.add(people_list_to_display[good_response].name)
                    }
                }
            }
        }

        val first_film = people_list_to_display[good_response].films[0]
        service.filmItem(first_film).enqueue(ws_callback_film_item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val explicit_intent = Intent(this,SecondActivity::class.java)
        var people_list: List<PeopleItem>;
        var people_list_to_display: MutableList<PeopleItem> = mutableListOf<PeopleItem>()
        val random_indexes =  IntArray(20)
        fill_array_with_random(random_indexes)
        var good_response: Int = (0..4).random()

        val info_films: MutableList<String> = mutableListOf<String>()
        val base_url = "https://ghibliapi.herokuapp.com/"
        val json_converter = GsonConverterFactory.create(GsonBuilder().create())
        val retrofit = Retrofit.Builder()
            .baseUrl(base_url)
            .addConverterFactory(json_converter)
            .build()
        val service: WebService = retrofit.create(WebService::class.java)
        val wsCallback: Callback<List<PeopleItem>> = object : Callback<List<PeopleItem>> {
            override fun onFailure(call: Call<List<PeopleItem>>, t: Throwable) {
                Log.d("Info", "Error on get people")
            }

            override fun onResponse(
                call: Call<List<PeopleItem>>,
                response: Response<List<PeopleItem>>
            ) {
                val code_first_request = response.code()
                if (code_first_request == 200) {
                    people_list = response.body()!!
                    for (i in 0..5) { //GET RANDOM PEOPLE INDEXES
                        val rand_index = random_indexes[i]
                        val people = people_list[rand_index]
                        people_list_to_display.add(people )
                    }

                    call_second_request(service, info_films, people_list_to_display, good_response)

                    val onItemClickListener : View.OnClickListener = View.OnClickListener { rowView ->
                        if (rowView.tag == good_response) {
                            explicit_intent.putExtra("RESPONSE_STATUS",
                                Activity_Data(true,
                                    people_list_to_display[rowView.tag as Int].films[0],
                                    info_films)
                            )
                            startActivity(explicit_intent)
                        }
                        else {
                            explicit_intent.putExtra("RESPONSE_STATUS", Activity_Data(false,
                                people_list_to_display[rowView.tag as Int].films[0], info_films))
                            startActivity(explicit_intent)
                        }
                    }

                    activity_main_character_list.setHasFixedSize(true)
                    activity_main_character_list.layoutManager = LinearLayoutManager(this@MainActivity)
                    activity_main_character_list.adapter = PeopleAdapter(
                        people_list_to_display,
                        this@MainActivity,
                        onItemClickListener)
                    activity_main_character_list.addItemDecoration(
                        DividerItemDecoration(
                            this@MainActivity,
                            DividerItemDecoration.VERTICAL)
                    )
                }
            }
        }
        service.listAllPeople().enqueue(wsCallback)
    }
}