package dev.mfazio.abl.players

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mfazio.abl.databinding.FragmentPlayersBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalPagingApi
class PlayersFragment : Fragment() {

    private val playerListArgs: PlayersFragmentArgs by navArgs()
    private val playersListViewModel by activityViewModels<PlayerListViewModel>()

    private lateinit var playersAdapter: PlayersAdapter
    private var currentJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPlayersBinding.inflate(inflater)

        this.playersAdapter = PlayersAdapter()

        with(binding.playersList) {
            adapter = playersAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }

        with(binding.playerSearchBoxText) {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    searchPlayers(text.toString().trim())
                    true
                } else {
                    false
                }
            }

            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    searchPlayers(text.toString().trim())
                    true
                } else {
                    false
                }
            }
        }

        binding.playerSearchBox.setEndIconOnClickListener {
            binding.playerSearchBoxText.text?.let { text ->
                text.clear()
                searchPlayers()
            }
        }

        searchPlayers()

        return binding.root
    }

    private fun searchPlayers(nameQuery: String? = null) {
        currentJob?.cancel()

        currentJob = lifecycleScope.launch {
            playersListViewModel
                .getPlayerListItems(playerListArgs.teamId, nameQuery)
                .collectLatest { playerListItems ->
                    if (::playersAdapter.isInitialized) {
                        playersAdapter.submitData(playerListItems)
                    }
                }
        }
    }
}