package com.gmail.htaihm.playdatabinding

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ObservableInt
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gmail.htaihm.playdatabinding.databinding.ActivityHeroBinding

class HeroActivity : AppCompatActivity() {

    companion object {
        const val ARG_HERO_ID = "hero_id"

        fun createIntent(context: Context, heroId: String) : Intent {
            // HeroActivity.javaClass is actually a static method, which gets the class of "this" object, which is
            // HeroActivity.companion in this class, which is wrong.

            val intent = Intent(context, HeroActivity::class.java)
            intent.putExtra(ARG_HERO_ID, heroId)
            return intent
        }
    }

    private lateinit var heroVO: HeroVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityHeroBinding = DataBindingUtil.setContentView(this, R.layout.activity_hero)

        val bundle = requireNotNull(intent.extras)
        val heroId = bundle.getString(ARG_HERO_ID, "")

        val repository = HeroesRepository.getInstance(applicationContext)
        repository.getHero(heroId)?.let {
                hero ->
            heroVO = HeroVO(hero.id, hero.name, ObservableInt(hero.matches), hero.abilities)
            binding.heroVO = heroVO
            binding.increaseMatches.setOnClickListener {
                heroVO.matches.set(heroVO.matches.get() + 1)
            }
        }
    }
}
