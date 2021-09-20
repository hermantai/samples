package dev.mfazio.palettetester

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import dev.mfazio.palettetester.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //TODO: Update this as needed to change images.
        // No, it's not fancy, but it's just a sample app.
        val imageId = R.drawable.ic_abl_logo_with_text

        val imageDrawable = ContextCompat.getDrawable(this, imageId)
        val image = imageDrawable?.toBitmap()

        if(image != null) {
            val palette = Palette.from(image).generate()

            with(binding) {
                mainImage.setImageDrawable(imageDrawable)

                palette.dominantSwatch?.rgb?.let { dominantPalette.setBackgroundColor(it) }
                palette.lightVibrantSwatch?.rgb?.let { lightVibrantPalette.setBackgroundColor(it) }
                palette.vibrantSwatch?.rgb?.let { vibrantPalette.setBackgroundColor(it) }
                palette.darkVibrantSwatch?.rgb?.let { darkVibrantPalette.setBackgroundColor(it) }
                palette.lightMutedSwatch?.rgb?.let { lightMutedPalette.setBackgroundColor(it) }
                palette.mutedSwatch?.rgb?.let { mutedPalette.setBackgroundColor(it) }
                palette.darkMutedSwatch?.rgb?.let { darkMutedPalette.setBackgroundColor(it) }
            }
        }
    }
}