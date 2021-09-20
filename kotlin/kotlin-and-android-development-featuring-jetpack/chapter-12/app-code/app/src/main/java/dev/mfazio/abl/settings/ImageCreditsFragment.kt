package dev.mfazio.abl.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.mfazio.abl.R
import dev.mfazio.abl.databinding.FragmentImageCreditsBinding

class ImageCreditsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentImageCreditsBinding.inflate(inflater)

        val adapter = ImageCreditsAdapter().apply {
            submitList(credits)
        }

        with(binding.imageCreditsList) {
            this.adapter = adapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        return binding.root
    }

    companion object {
        private val credits = listOf(
            ImageCredit(1, R.drawable.fi_ic_fox, "Appleton Logo", "https://www.flaticon.com/free-icon/fox_672640", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(2, R.drawable.fi_ic_circus, "Baraboo Logo", "https://www.flaticon.com/free-icon/circus_1838489", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(3, R.drawable.fi_ic_lake, "Eau Claire Logo", "https://www.flaticon.com/free-icon/river_119573", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(4, R.drawable.ic_skyline_logo, "Green Bay Logo", "https://www.skylinetechnologies.com/", "All rights to the Skyline logo belongs to Skyline Technologies, Inc"),
            ImageCredit(5, R.drawable.fi_ic_heart, "La Crosse Logo", "https://www.flaticon.com/free-icon/hearts_1405110", "Icons made by <a href=\"https://www.flaticon.com/authors/smalllikeart\" title=\"smalllikeart\">smalllikeart</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(6, R.drawable.fi_ic_sea, "Lake Delton Logo", "https://www.flaticon.com/free-icon/wave_1821219", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(7, R.drawable.fi_ic_snow_sharp, "Madison Logo", "https://www.flaticon.com/free-icon/snowflake_2834554", "Icons made by <a href=\"https://www.flaticon.com/free-icon/snowflake_2834554\" title=\"Good Ware\">Good Ware</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(8, R.drawable.mke_flag, "Milwaukee Logo", "https://milwaukeeflag.com", "The Milwaukee logo is from the People's Flag of Milwaukee"),
            ImageCredit(9, R.drawable.fi_ic_crown, "Pewaukee Logo", "https://www.flaticon.com/free-icon/crown_3141842", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(10, R.drawable.fi_ic_swallow, "Shawano Logo", "https://www.flaticon.com/free-icon/swallow_2484514", "Icons made by <a href=\"https://smashicons.com/\" title=\"Smashicons\">Smashicons</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(11, R.drawable.fi_ic_theater, "Spring Green Logo", "https://www.flaticon.com/free-icon/theater_1138022", "Icons made by <a href=\"https://www.flaticon.com/authors/smashicons\" title=\"Smashicons\">Smashicons</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(12, R.drawable.fi_ic_walking_stick, "Sturgeon Bay Logo", "https://www.flaticon.com/free-icon/walking-stick_2852351", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(13, R.drawable.fi_ic_electric_guitar, "Waukesha Logo", "https://www.flaticon.com/free-icon/electric-guitar_836903", "Icons made by <a href=\"https://www.flaticon.com/authors/freepik\" title=\"Freepik\">Freepik</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(14, R.drawable.fi_ic_cranberry, "Wisconsin Rapids Logo", "https://www.flaticon.com/free-icon/cranberry_2044963", "Icons made by <a href=\"https://www.flaticon.com/authors/vitaly-gorbachev\" title=\"Vitaly Gorbachev\">Vitaly Gorbachev</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(15, R.drawable.ic_abl_logo_with_text, "ABL Logo Background", "https://www.flaticon.com/free-icon/home-plate_2288316", "Icons made by <a href=\"https://www.flaticon.com/free-icon/home-plate_2288316\" title=\"smalllikeart\">smalllikeart</a> from <a href=\"https://www.flaticon.com/\" title=\"Flaticon\">www.flaticon.com</a>"),
            ImageCredit(16, R.drawable.mdi_scoreboard_outline, "Scoreboard Icon", "https://materialdesignicons.com/icon/scoreboard-outline", "Icon created by Yaroslav Bandura and found on MaterialDesignIcons.com")
        )
    }
}