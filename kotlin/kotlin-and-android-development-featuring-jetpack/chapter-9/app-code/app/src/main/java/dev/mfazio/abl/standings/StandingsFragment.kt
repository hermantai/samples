package dev.mfazio.abl.standings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import dev.mfazio.abl.R

class StandingsFragment : Fragment() {

    private val standingsViewModel by activityViewModels<StandingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_standings, container, false)

        val standingsAdapter = StandingsAdapter()

        if (view is RecyclerView) {
            view.adapter = standingsAdapter
        }

        standingsViewModel.standings.observe(viewLifecycleOwner) { standings ->
            standingsAdapter.addHeadersAndBuildStandings(standings)
        }

        return view
    }
}