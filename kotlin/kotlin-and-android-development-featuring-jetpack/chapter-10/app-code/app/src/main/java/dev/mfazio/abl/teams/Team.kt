package dev.mfazio.abl.teams

data class Team(
    val id: String,
    val city: String,
    val nickname: String,
    val division: Division,
    val wins: Int = 0,
    val losses: Int = 0,
    val leagueRank: Int = -1,
    val divisionRank: Int = -1
) {
    companion object {
        val Appleton = Team("APL", "Appleton", "Foxes", Division.East)
        val Baraboo = Team("BOO", "Baraboo", "Big Tops", Division.West)
        val EauClaire = Team("EC", "Eau Claire", "Blues", Division.West)
        val GreenBay = Team("GB", "Green Bay", "Skyline", Division.East)
        val LaCrosse = Team("LAX", "La Crosse", "Lovers", Division.West)
        val LakeDelton = Team("LD", "Lake Delton", "Breakers", Division.West)
        val Madison = Team("MSN", "Madison", "Snowflakes", Division.West)
        val Milwaukee = Team("MKE", "Milwaukee", "Sunrise", Division.East)
        val Pewaukee = Team("PKE", "Pewaukee", "Princesses", Division.East)
        val Shawano = Team("SHW", "Shawano", "Shorebirds", Division.East)
        val SpringGreen = Team("SG", "Spring Green", "Thespians", Division.West)
        val SturgeonBay = Team("SB", "Sturgeon Bay", "Elders", Division.East)
        val Waukesha = Team("WAU", "Waukesha", "Riffs", Division.East)
        val WisconsinRapids = Team("WR", "Wisconsin Rapids", "Cranberries", Division.West)
    }
}