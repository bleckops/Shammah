package co.bleck.shammah.domain.model

/**
 * Snapshot of the three resource-driven sections of the About screen.
 *
 * Each [Resource] is nullable because Firestore may not yet have published the document
 * (or the client lacks permission to read it) — the UI layer falls back to a built-in
 * default copy in those cases.
 */
data class AboutContent(
    val mission: Resource? = null,
    val vision: Resource? = null,
    val aboutUs: Resource? = null,
)
