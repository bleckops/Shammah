package co.bleck.shammah.data.mapper

import co.bleck.shammah.data.dto.BannerDto
import co.bleck.shammah.domain.model.Banner
import java.util.Date

object BannerMapper {

    fun toDomain(id: String, dto: BannerDto): Banner = Banner(
        id = id,
        imageUrl = (dto.imageUrl as? String).orEmpty(),
        title = (dto.title as? String).orEmpty(),
        linkUrl = dto.linkUrl as? String,
        audioUrl = (dto.audioUrl as? String).orEmpty(),
        category = (dto.category as? String).orEmpty(),
        speaker = (dto.speaker as? String).orEmpty(),
        videoUrl = (dto.videoUrl as? String).orEmpty(),
        order = dto.order,
        isActive = dto.isActive,
        createdAt = dto.createdAt ?: Date(),
        updatedAt = dto.updatedAt ?: Date()
    )
}
