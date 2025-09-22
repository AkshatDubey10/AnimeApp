package com.example.animeapp.ui.list

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.animeapp.R
import com.example.animeapp.data.model.AnimeBrief

class AnimeAdapter(
  private val onClick: (AnimeBrief) -> Unit
) : ListAdapter<AnimeBrief, AnimeAdapter.AnimeViewHolder>(Diff) {

  object Diff : DiffUtil.ItemCallback<AnimeBrief>() {
    override fun areItemsTheSame(
      oldItem: AnimeBrief,
      newItem: AnimeBrief
    ) =
      oldItem.malId == newItem.malId

    override fun areContentsTheSame(
      oldItem: AnimeBrief,
      newItem: AnimeBrief
    ) =
      oldItem == newItem
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): AnimeViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_anime, parent, false)
    return AnimeViewHolder(view, onClick)
  }

  override fun onBindViewHolder(
    holder: AnimeViewHolder,
    position: Int
  ) {
    holder.bind(getItem(position))
  }

  class AnimeViewHolder(
    itemView: View,
    private val onClick: (AnimeBrief) -> Unit
  ) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.anime_title)
    private val episodes: TextView = itemView.findViewById(R.id.anime_episodes)
    private val score: TextView = itemView.findViewById(R.id.anime_score)
    private val image: ImageView = itemView.findViewById(R.id.anime_image)

    fun bind(anime: AnimeBrief) {
      title.text = anime.title
      episodes.text = "Episodes: ${anime.episodes ?: "?"}"
      score.text = "Score: ${anime.score ?: "-"}"

      val posterUrl = anime.images?.jpg?.imageUrl
        ?: anime.images?.jpg?.largeImageUrl
        ?: anime.images?.webp?.imageUrl
        ?: R.drawable.place_holder_with_error


      Log.i("AnimeViewHolder", "item image: ${anime.images}")

      Glide.with(itemView.context)
        .load(posterUrl)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.place_holder_with_error)
        .into(image)

      itemView.setOnClickListener { onClick(anime) }
    }
  }
}
