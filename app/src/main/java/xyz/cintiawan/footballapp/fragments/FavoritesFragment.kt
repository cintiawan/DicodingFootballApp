package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_favorites.*

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.adapters.ViewPagerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class FavoritesFragment : Fragment() {
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(FavoriteMatchesFragment())
        adapter.addFragment(FavoriteTeamsFragment())
        viewpager.adapter = adapter

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab) { viewpager.currentItem = tab.position }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_favorites, container, false)
}
