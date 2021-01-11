package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_matches.*
import org.jetbrains.anko.support.v4.startActivity

import xyz.cintiawan.footballapp.R
import xyz.cintiawan.footballapp.R.id.action_search
import xyz.cintiawan.footballapp.R.menu.action_bar
import xyz.cintiawan.footballapp.SearchMatchActivity
import xyz.cintiawan.footballapp.adapters.ViewPagerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MatchesFragment : Fragment() {
    private lateinit var adapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = ViewPagerAdapter(childFragmentManager)
        adapter.addFragment(NextMatchesFragment())
        adapter.addFragment(PrevMatchesFragment())
        viewpager.adapter = adapter

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) { }

            override fun onTabUnselected(tab: TabLayout.Tab?) { }

            override fun onTabSelected(tab: TabLayout.Tab) { viewpager.currentItem = tab.position }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(action_bar, menu)
        menu?.findItem(action_search)?.actionView = null

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            action_search -> {
                startActivity<SearchMatchActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_matches, container, false)
}
