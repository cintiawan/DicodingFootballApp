package xyz.cintiawan.footballapp.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_team_description.*

import xyz.cintiawan.footballapp.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TeamDescriptionFragment() : Fragment() {

    companion object {
        private const val DESCRIPTION = "description"
        fun newInstance(description: String?): TeamDescriptionFragment {
            val fragment = TeamDescriptionFragment()
            val args = Bundle()
            args.putString(DESCRIPTION, description)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        team_description.text = arguments?.getString(DESCRIPTION)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? = inflater.inflate(R.layout.fragment_team_description, container, false)


}
