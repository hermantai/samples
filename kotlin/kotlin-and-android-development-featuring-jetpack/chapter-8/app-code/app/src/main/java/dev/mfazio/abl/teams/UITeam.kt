package dev.mfazio.abl.teams

import dev.mfazio.abl.R

data class UITeam(
    val team: Team,
    val logoId: Int,
    val primaryColorId: Int,
    val secondaryColorId: Int,
    val tertiaryColorId: Int
) {
    val teamId = team.id
    val location = team.city
    val nickname = team.nickname
    val teamName = "$location $nickname"
    val division = team.division

    companion object {
        val allTeams = listOf(
            UITeam(Team.Appleton, R.drawable.fi_ic_fox, R.color.appletonPrimary, R.color.appletonSecondary, R.color.appletonTertiary),
            UITeam(Team.Baraboo, R.drawable.fi_ic_circus, R.color.barabooPrimary, R.color.barabooSecondary, R.color.barabooTertiary),
            UITeam(Team.EauClaire, R.drawable.fi_ic_lake, R.color.eauClairePrimary, R.color.eauClaireSecondary, R.color.eauClaireTertiary),
            UITeam(Team.GreenBay, R.drawable.ic_skyline_logo, R.color.greenBayPrimary, R.color.greenBaySecondary, R.color.greenBayTertiary),
            UITeam(Team.LaCrosse, R.drawable.fi_ic_heart, R.color.laCrossePrimary, R.color.laCrosseSecondary, R.color.laCrosseTertiary),
            UITeam(Team.LakeDelton, R.drawable.fi_ic_sea, R.color.lakeDeltonPrimary, R.color.lakeDeltonSecondary, R.color.lakeDeltonTertiary),
            UITeam(Team.Madison, R.drawable.fi_ic_snow_sharp, R.color.madisonPrimary, R.color.madisonSecondary, R.color.madisonTertiary),
            UITeam(Team.Milwaukee, R.drawable.mke_flag, R.color.milwaukeePrimary, R.color.milwaukeeSecondary, R.color.milwaukeeTertiary),
            UITeam(Team.Pewaukee, R.drawable.fi_ic_crown, R.color.pewaukeePrimary, R.color.pewaukeeSecondary, R.color.pewaukeeTertiary),
            UITeam(Team.Shawano, R.drawable.fi_ic_swallow, R.color.shawanoPrimary, R.color.shawanoSecondary, R.color.shawanoTertiary),
            UITeam(Team.SpringGreen, R.drawable.fi_ic_theater, R.color.springGreenPrimary, R.color.springGreenSecondary, R.color.springGreenTertiary),
            UITeam(Team.SturgeonBay, R.drawable.fi_ic_walking_stick, R.color.sturgeonBayPrimary, R.color.sturgeonBaySecondary, R.color.sturgeonBayTertiary),
            UITeam(Team.Waukesha, R.drawable.fi_ic_electric_guitar, R.color.waukeshaPrimary, R.color.waukeshaSecondary, R.color.waukeshaTertiary),
            UITeam(Team.WisconsinRapids, R.drawable.fi_ic_cranberry, R.color.wiRapidsPrimary, R.color.wiRapidsSecondary, R.color.wiRapidsTertiary)
        )
    }
}