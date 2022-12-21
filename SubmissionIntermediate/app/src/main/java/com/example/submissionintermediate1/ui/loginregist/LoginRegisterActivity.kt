package com.example.submissionintermediate1.ui.loginregist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.example.submissionintermediate1.R
import com.example.submissionintermediate1.adapter.PagerAdapter
import com.example.submissionintermediate1.databinding.ActivityLoginRegisterBinding
import com.example.submissionintermediate1.ui.ViewModelFactory
import com.example.submissionintermediate1.ui.loginregist.login.LoginViewModel
import com.example.submissionintermediate1.ui.main.MainActivity
import com.example.submissionintermediate1.ui.main.MainActivity.Companion.EXTRA_TOKEN
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginRegisterBinding
    private val viewModel: LoginViewModel by viewModels { factory }
    private lateinit var factory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        factory = ViewModelFactory.getInstance(this)

        val pagerAdapter = PagerAdapter(this)
        binding.apply {
            viewPager.adapter = pagerAdapter
            TabLayoutMediator(tabsDetail, viewPager){tab, position ->
                tab.text = resources.getString(TAB_TITTLES[position])
            }.attach()
            supportActionBar?.elevation = 0f
        }
        lifecycleScope.launchWhenResumed {
            launch {
                viewModel.getToken().collect {
                    if (it.isNullOrEmpty()) {
                        toastMsg(getString(R.string.error))
                    } else {
                        val intent = Intent(this@LoginRegisterActivity, MainActivity::class.java)
                        intent.putExtra(EXTRA_TOKEN, it)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun toastMsg(msg: String){
        Toast.makeText(this@LoginRegisterActivity, msg, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private val TAB_TITTLES = intArrayOf(
            R.string.login,
            R.string.signup
        )
    }
}