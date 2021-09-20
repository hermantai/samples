package dev.mfazio.abl.settings

data class ImageCredit(
    val id: Long,
    val imageResourceId: Int,
    val fileLabel: String,
    val url: String,
    val usageDescription: String
) {
    val anchorUrl = "<a>$url</a>"
}
