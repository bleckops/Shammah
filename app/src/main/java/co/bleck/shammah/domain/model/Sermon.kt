package co.bleck.shammah.domain.model

import java.util.Date

data class Sermon(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: Date = Date(),
    val notes: String = ""
)
