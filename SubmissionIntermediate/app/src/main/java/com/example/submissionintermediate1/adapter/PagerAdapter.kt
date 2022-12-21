package com.example.submissionintermediate1.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.paging.ExperimentalPagingApi
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.submissionintermediate1.ui.loginregist.login.LoginFragment
import com.example.submissionintermediate1.ui.loginregist.register.RegisterFragment

@ExperimentalPagingApi
class PagerAdapter (activity: AppCompatActivity): FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = LoginFragment()
            1 -> fragment = RegisterFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}