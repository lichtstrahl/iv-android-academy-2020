package root.iv.ivandroidacademy.data.mapper

import root.iv.ivandroidacademy.data.model.*
import root.iv.ivandroidacademy.data.model.dto.ActorDTO
import root.iv.ivandroidacademy.data.model.dto.GenreDTO
import root.iv.ivandroidacademy.data.model.dto.ImageConfigurationDTO
import root.iv.ivandroidacademy.data.model.dto.MovieDTO

object Mapper {

    fun actor(dto: ActorDTO, config: ImageConfigurationDTO.Config) = Actor(
        dto.id,
        dto.name,
        dto.photoUrl?.toImageUrl(config.baseUrl, config.profileSizes.middle())
    )

    fun movie(dto: MovieDTO, genres: List<Genre>, config: ImageConfigurationDTO.Config) = Movie(
        dto.id,
        dto.title,
        genres.joinToString(separator = ", ") { it.name },
        dto.duration,
        if (dto.adult) 16 else 13,
        dto.rating/2,
        dto.votesCount,
        dto.posterPath?.toImageUrl(config.baseUrl, config.posterSizes.middle()),
        dto.posterBackdropPath?.toImageUrl(config.baseUrl, config.backdropSizes.extra()),
        false,
        dto.overview
    )

    fun genre(dto: GenreDTO) = Genre(
        dto.id,
        dto.name
    )
}