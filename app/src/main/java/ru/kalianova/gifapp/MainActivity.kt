package ru.kalianova.gifapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.GridLayout
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.kalianova.gifapp.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
const val BASE_URL = "https://api.giphy.com/v1/"
const val TAG = "MainActivity"
const val API_KEY = "mGEdtb3cKvKzsSctLek5oLOhxS8P9YlX"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val recyclerViewGifItems = binding.recyclerViewGifItems
        val gifs = mutableListOf<GifDataObject>()
        val adapter = GifDataAdapter(gifs, this)

        recyclerViewGifItems.adapter = adapter
        recyclerViewGifItems.setHasFixedSize(true)
        recyclerViewGifItems.layoutManager = LinearLayoutManager(this)


        //Retrofit
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()
        val retrofitService = retrofit.create(GetDataService::class.java)

        var startPosition = 0
        //Retrofit getGifs
        retrofitService.getGifs(API_KEY, startPosition).enqueue(object : Callback<GifDataResult?> {
            override fun onResponse(
                call: Call<GifDataResult?>, response: Response<GifDataResult?>
            ) {
                val body = response.body()
                if (body == null) {
                    Log.d(TAG, "onResponse: Body null")
                }
                gifs.addAll(body!!.data)
                adapter.notifyItemRangeInserted(startPosition, 25)

            }

            override fun onFailure(call: Call<GifDataResult?>, t: Throwable) {

            }
        })


        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                retrofitService.searchGifs(API_KEY, searchView.query.toString())
                    .enqueue(object : Callback<GifDataResult?> {
                        override fun onResponse(
                            call: Call<GifDataResult?>, response: Response<GifDataResult?>
                        ) {
                            val body = response.body()
                            if (body == null) {
                                Log.d(TAG, "onResponse: Body null")
                            }
                            gifs.clear()
                            gifs.addAll(body!!.data)
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<GifDataResult?>, t: Throwable) {

                        }
                    })
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

        }

        )
    }
}