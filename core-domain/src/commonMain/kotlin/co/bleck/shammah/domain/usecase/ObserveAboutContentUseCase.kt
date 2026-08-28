package co.bleck.shammah.domain.usecase

import co.bleck.shammah.domain.model.AboutContent
import co.bleck.shammah.domain.model.ResourceType
import co.bleck.shammah.domain.repository.ResourceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Projects the `resources` collection into the three sections the About screen needs.
 *
 * The first active document of each type wins. If a slot has no matching document, the
 * corresponding field on [AboutContent] is `null` so the UI can substitute its built-in
 * fallback copy without losing the rest of the screen.
 */
class ObserveAboutContentUseCase(private val repository: ResourceRepository) {
    operator fun invoke(): Flow<AboutContent> =
        repository.getResources().map { resources ->
            AboutContent(
                mission = resources.firstOrNull { it.type == ResourceType.mission },
                vision = resources.firstOrNull { it.type == ResourceType.vision },
                aboutUs = resources.firstOrNull { it.type == ResourceType.aboutus },
            )
        }
}
