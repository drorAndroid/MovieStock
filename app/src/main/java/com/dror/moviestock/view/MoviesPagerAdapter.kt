package com.dror.moviestock.view

import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlin.reflect.KClass

class MoviesPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments = listOf(MainFragment.newInstance(), DetailsFragment.newInstance())

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.count()
    }

    fun <T: Any> getFragmentPosition(fragmentClass: KClass<T>): Int {
        return fragments.indexOfFirst {
            it::class == fragmentClass
        }

    }
}