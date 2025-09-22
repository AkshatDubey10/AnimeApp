package com.example.animeapp.ui.detail

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.example.animeapp.R
import com.example.animeapp.data.model.AnimeDetail
import com.example.animeapp.databinding.FragmentAnimeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeDetailFragment : Fragment() {

  companion object {
    private const val ARG_ID = "anime_id"
    fun newInstance(id: Int) =
      AnimeDetailFragment().apply { arguments = Bundle().apply { putInt(ARG_ID, id) } }
  }

  private val vm: AnimeDetailViewModel by viewModels()
  private var _binding: FragmentAnimeDetailBinding? = null
  private val binding get() = _binding!!
  private var player: ExoPlayer? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAnimeDetailBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    val id = arguments?.getInt(ARG_ID) ?: return
    vm.load(id)

    lifecycleScope.launch {
      vm.uiState.collectLatest { state ->
        when (state) {
          is AnimeDetailUiState.Loading -> {
            binding.loader.visibility = View.VISIBLE
            binding.errorPlaceholder.visibility = View.GONE
            binding.poster.visibility = View.GONE
            binding.playerView.visibility = View.GONE
            binding.youTubeWebView.visibility = View.GONE
          }

          is AnimeDetailUiState.Success -> {
            binding.loader.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.GONE
            bindDetail(state.detail)
          }

          is AnimeDetailUiState.Error -> {
            binding.loader.visibility = View.GONE
            binding.errorPlaceholder.visibility = View.VISIBLE
            binding.poster.visibility = View.GONE
            binding.playerView.visibility = View.GONE
            binding.youTubeWebView.visibility = View.GONE
          }
        }
      }
    }

  }

  private fun isDirectPlayable(url: String?): Boolean {
    if (url.isNullOrBlank()) return false
    val low = url.lowercase()
    return low.endsWith(".mp4") || low.endsWith(".m3u8") || low.endsWith(".webm") || low.endsWith(".mpd")
  }

  private fun bindDetail(d: AnimeDetail) {
    binding.title.text = d.title
    binding.synopsis.text = d.synopsis ?: "No synopsis available"
    binding.genres.text = "Genres: ${d.genres.joinToString { it.name }}"
    binding.episodes.text = "Episodes: ${d.episodes ?: "?"}"
    binding.rating.text = "Score: ${d.score ?: "N/A"}"

    val posterUrl = d.images?.jpg?.imageUrl
    Glide.with(this)
      .load(posterUrl ?: R.drawable.placeholder) // fallback image
      .placeholder(R.drawable.placeholder)
      .error(R.drawable.place_holder_with_error)
      .into(binding.poster)

    val trailerUrl = d.trailer?.url
    val youTubeId = d.trailer?.youtubeId

    when {
      isDirectPlayable(trailerUrl) -> {
        binding.playerView.visibility = View.VISIBLE
        binding.poster.visibility = View.GONE
        binding.youTubeWebView.visibility = View.GONE
        initializePlayer(trailerUrl)
      }

      !youTubeId.isNullOrBlank() -> {
        binding.playerView.visibility = View.GONE
        binding.poster.visibility = View.GONE
        binding.youTubeWebView.visibility = View.VISIBLE
        binding.youTubeWebView.settings.javaScriptEnabled = true
        binding.youTubeWebView.webViewClient = WebViewClient()
        val html = """
                    <html><body style="margin:0;padding:0">
                    <iframe width="100%" height="100%" 
                    src="https://www.youtube.com/embed/$youTubeId?autoplay=0&rel=0" 
                    frameborder="0" allowfullscreen></iframe>
                    </body></html>
                """.trimIndent()
        binding.youTubeWebView.loadData(html, "text/html", "utf-8")
      }

      else -> {
        // Show poster as fallback
        binding.playerView.visibility = View.GONE
        binding.youTubeWebView.visibility = View.GONE
        binding.poster.visibility = View.VISIBLE
      }
    }
  }

  private fun initializePlayer(url: String?) {
    if (url.isNullOrBlank()) return
    player = ExoPlayer.Builder(requireContext()).build().also { exo ->
      val mediaItem = MediaItem.fromUri(Uri.parse(url))
      exo.setMediaItem(mediaItem)
      exo.prepare()
      binding.playerView.player = exo
      exo.playWhenReady = false
    }
  }

  override fun onPause() {
    super.onPause()
    player?.pause()
  }

  override fun onStop() {
    super.onStop()
    player?.pause()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    player?.release()
    player = null
    _binding = null
  }
}
