package xyz.cintiawan.footballapp.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager?) : FragmentPagerAdapter(fragmentManager) {
    private var fragmentList: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    fun addFragment(f: Fragment) = fragmentList.add(f)
}