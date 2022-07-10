package root.iv.ivandroidacademy.data.mapper

import root.iv.ivandroidacademy.data.database.entity.ActorEntity
import root.iv.ivandroidacademy.data.database.entity.GenreEntity
import root.iv.ivandroidacademy.data.database.entity.ImageConfigEntity
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
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

    fun movie(entity: MovieEntity) = Movie(
        entity.id.toInt(),
        entity.title,
        entity.tags,
        entity.duration,
        entity.ageLimit,
        entity.rating.toFloat(),
        entity.reviewsCount,
        entity.posterUrl,
        entity.backdropUrl,
        entity.isLike,
        entity.storyline
    )

    fun entity(movie: Movie) = MovieEntity(
        null,
        movie.id.toLong(),
        movie.title,
        movie.tags,
        movie.duration,
        movie.ageLimit,
        movie.rating.toDouble(),
        movie.reviewsCount,
        movie.poster,
        movie.poster2,
        movie.isLike,
        movie.storyline
    )

    fun entity(imageConfig: ImageConfigurationDTO.Config) = ImageConfigEntity(
        baseUrl = imageConfig.baseUrl,
        backdropSizes = imageConfig.backdropSizes,
        posterSizes = imageConfig.posterSizes,
        profileSizes = imageConfig.profileSizes
    )

    fun entity(actor: Actor, movieId: Long) = ActorEntity(
        actor.id.toLong(),
        actor.name,
        actor.photoUrl,
        movieId
    )

    fun entity(genre: Genre) = GenreEntity(
        genre.id.toLong(), genre.name
    )

    fun genre(entity: GenreEntity) = Genre(
        entity.id.toInt(), entity.name
    )

    fun actor(entity: ActorEntity) = Actor(
        entity.id.toInt(),
        entity.name,
        entity.photoUrl
    )

    fun genre(dto: GenreDTO) = Genre(
        dto.id,
        dto.name
    )

    fun imageConfig(entity: ImageConfigEntity) = ImageConfigurationDTO.Config(
        entity.baseUrl,
        entity.backdropSizes,
        entity.posterSizes,
        entity.profileSizes
    )
}