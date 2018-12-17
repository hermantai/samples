package com.gmail.htaihm.playdatabinding;

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gmail.htaihm.playdatabinding.databinding.ItemHeroBinding

class HeroesAdapter(
    private val heroes: List<Hero>,
    private val callback: OnHeroClickListener
) : RecyclerView.Adapter<HeroesAdapter.HeroViewHolder>() {

    interface OnHeroClickListener {
        fun onHeroClick(hero: Hero)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            ItemHeroBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return heroes.size
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroes[position], callback)
    }

    class HeroViewHolder(
        private val binding: ItemHeroBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Hero, callback: OnHeroClickListener) {
            binding.apply {
                hero = item
                listener = callback
            }
        }
    }
}