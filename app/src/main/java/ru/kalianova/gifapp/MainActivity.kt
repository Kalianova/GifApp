package ru.kalianova.gifapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.kalianova.gifapp.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding
const val API_KEY = "mGEdtb3cKvKzsSctLek5oLOhxS8P9YlX"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //RecyclerView init
        val recyclerViewGifItems = binding.recyclerViewGifItems
        val adapter = GifPagerAdapter()
        recyclerViewGifItems.adapter = adapter
        recyclerViewGifItems.setHasFixedSize(true)
        recyclerViewGifItems.layoutManager = LinearLayoutManager(this)

        //Retrofit init
        val retrofit = GetDataService.invoke()
        initViewModel(retrofit, this@MainActivity, "", adapter)

        //SearchView implementation
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                initViewModel(retrofit, this@MainActivity, searchView.query.toString(), adapter)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }

            val callback = onBackPressedDispatcher.addCallback(
                this@MainActivity,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        initViewModel(retrofit, this@MainActivity, "", adapter)
                    }
                })
        }
        )
    }
}

fun initViewModel(
    retrofit: GetDataService,
    context: AppCompatActivity,
    q: String,
    adapter: GifPagerAdapter
) {
    val gifRepository = GifsDataSource(retrofit, q)
    val viewModel = GifLiveDataModel(gifRepository)
    viewModel.errorManager.observe(context) {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
    context.lifecycleScope.launch {
        viewModel.getGifsList().observe(context) {
            it?.let {
                adapter.submitData(context.lifecycle, it)
            }
        }
    }
}
