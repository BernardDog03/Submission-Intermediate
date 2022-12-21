package com.example.submissionintermediate1.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionintermediate1.R
import com.example.submissionintermediate1.adapter.ListAdapter
import com.example.submissionintermediate1.adapter.LoadingAdapter
import com.example.submissionintermediate1.databinding.ActivityMainBinding
import com.example.submissionintermediate1.ui.ViewModelFactory
import com.example.submissionintermediate1.ui.add.AddStoriesActivity
import com.example.submissionintermediate1.ui.loginregist.LoginRegisterActivity
import com.example.submissionintermediate1.ui.maps.MapsActivity
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagingApi::class)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private lateinit var adapter: ListAdapter

    private val viewModel: MainViewModel by viewModels { factory }
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        token = intent.getStringExtra(EXTRA_TOKEN).toString()

        lifecycleScope.launchWhenStarted {
            launch {
                getStories()
            }
        }
        binding.apply {
            rvList.layoutManager = LinearLayoutManager(this@MainActivity)

            swipe.setOnRefreshListener {
                swipe.isRefreshing = false
                getStories()
            }
        }
        getStories()
    }

    private fun getStories() {
        val token = intent.getStringExtra(EXTRA_TOKEN).toString()
        adapter = ListAdapter()
        binding.rvList.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter {
                adapter.retry()
            }
        )
        viewModel.getStory(token).observe(this) {
            adapter.submitData(lifecycle, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.btn_logout -> {
                viewModel.deleteToken()
                val intent = Intent(this, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.btn_add_stories -> {
                val intentAdd = Intent(this, AddStoriesActivity::class.java)
                startActivity(intentAdd)
            }
            R.id.btn_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra(MapsActivity.EXTRA_TOKE, token)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_TOKEN = "extra_token"
    }

    override fun onStart() {
        super.onStart()
        getStories()
    }
}