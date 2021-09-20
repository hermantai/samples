package dev.mfazio.abl.scoreboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import dev.mfazio.abl.databinding.FragmentScoreboardBinding

class ScoreboardFragment : Fragment() {

    private val scoreboardViewModel by activityViewModels<ScoreboardViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentScoreboardBinding.inflate(inflater, container, false)
            .apply {
                vm = scoreboardViewModel

                scoreboardChangeYesterday.setOnClickListener { scoreboardViewModel.goToDate(-1) }
                scoreboardChangeYesterday.setOnLongClickListener {
                    scoreboardViewModel.goToDate(monthsToMove = -1)
                    true
                }

                scoreboardChangeTomorrow.setOnClickListener { scoreboardViewModel.goToDate(1) }
                scoreboardChangeTomorrow.setOnLongClickListener {
                    scoreboardViewModel.goToDate(monthsToMove = 1)
                    true
                }

                lifecycleOwner = viewLifecycleOwner
            }

        scoreboardViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if(!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
}