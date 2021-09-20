package dev.mfazio.abl.players

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dev.mfazio.abl.databinding.FragmentSinglePlayerBinding

class SinglePlayerFragment: Fragment() {

    private val args: SinglePlayerFragmentArgs by navArgs()
    private val singlePlayerViewModel by activityViewModels<SinglePlayerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSinglePlayerBinding
            .inflate(inflater, container, false)
            .apply {
                vm = singlePlayerViewModel.apply {
                    this.setPlayerId(args.playerId)
                }
                lifecycleOwner = viewLifecycleOwner
            }

        singlePlayerViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }
}