package dev.mfazio.pennydrop.game

data class AI(val name: String) {

    override fun toString() = name

    companion object {
        @JvmStatic
        val basicAI = listOf(
            AI("TwoFace"),
            AI("No Go Noah"),
            AI("Bail Out Beulah"),
            AI("Fearful Fred"),
            AI("Even Steven"),
            AI("Riverboat Ron"),
            AI("Sammy Sixes"),
            AI("Random Rachael")
        )
    }
}