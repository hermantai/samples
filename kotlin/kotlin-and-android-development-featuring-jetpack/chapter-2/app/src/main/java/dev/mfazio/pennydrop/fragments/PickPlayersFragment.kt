package dev.mfazio.pennydrop.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import dev.mfazio.pennydrop.databinding.FragmentPickPlayersBinding

class PickPlayersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentPickPlayersBinding.inflate(inflater, container, false)

        return binding.root
    }
}
