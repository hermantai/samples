package com.gmail.htaihm.playdatabinding

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

/**
 * Reference: http://imakeanapp.com/android-jetpack-data-binding/
 *
 * Demo: data binding, binding adapters, observables
 */
class MainActivity : AppCompatActivity(), HeroesAdapter.OnHeroClickListener {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.heroes_list)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val repository = HeroesRepository.getInstance(applicationContext)
        val heroes = repository.fetchHeroes()

        val adapter = HeroesAdapter(heroes, this)
        recyclerView.adapter = adapter
    }

    override fun onHeroClick(hero: Hero) {
        Log.d("MainActivity", "Clicked Hero: ${hero.name}")
        startActivity(HeroActivity.createIntent(this, hero.id))
    }
}

