package com.example.animeapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeapp.R
import com.example.animeapp.databinding.FragmentAnimeListBinding
import com.example.animeapp.ui.detail.AnimeDetailFragment
import com.example.jikananime.data.SyncPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class AnimeListFragment : Fragment() {

  private var _binding: FragmentAnimeListBinding? = null
  private val binding get() = _binding!!

  private val vm: AnimeListViewModel by viewModels()
  private lateinit var adapter: AnimeAdapter

  @Inject lateinit var prefs: SyncPrefs

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAnimeListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)

    adapter = AnimeAdapter { anime ->
      parentFragmentManager.beginTransaction()
        .replace(R.id.container, AnimeDetailFragment.newInstance(anime.malId))
        .addToBackStack(null)
        .commit()
    }

    binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
    binding.recyclerView.adapter = adapter

    binding.swipeRefresh.setOnRefreshListener {
      vm.loadAnime {
        binding.swipeRefresh.isRefreshing = false
        val ts = prefs.getLastSync()
        binding.lastSync.text =
          if (ts <= 0) "Last sync: -" else "Last sync: " +
            SimpleDateFormat.getDateTimeInstance().format(Date(ts))
      }
    }

    viewLifecycleOwner.lifecycleScope.launch {
      vm.anime.collectLatest { state ->
        adapter.submitList(state?.data)
        binding.staleIndicator.visibility =
          if (state?.isStale == true && state.data.isNotEmpty()) View.VISIBLE else View.GONE
        binding.swipeRefresh.isRefreshing = false
      }
    }

    // show last sync time
    viewLifecycleOwner.lifecycleScope.launch {
      val ts = prefs.getLastSync()
      binding.lastSync.text =
        if (ts <= 0) "Last sync: -" else "Last sync: " +
          SimpleDateFormat.getDateTimeInstance().format(Date(ts))
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}