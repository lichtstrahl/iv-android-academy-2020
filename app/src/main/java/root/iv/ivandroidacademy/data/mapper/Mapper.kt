package root.iv.ivandroidacademy.data.mapper

import root.iv.ivandroidacademy.BuildConfig
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.ActorDTO
import root.iv.ivandroidacademy.data.model.dto.GenreDTO
import root.iv.ivandroidacademy.data.model.dto.MovieDTO

class Mapper {

    fun actor(dto: ActorDTO) = Actor(
        dto.id,
        dto.name,
        dto.photoUrl
    )

    fun movie(dto: MovieDTO, genres: List<Genre>) = Movie(
        dto.id,
        dto.title,
        genres.joinToString(separator = ", ") { it.name },
        dto.duration,
        if (dto.adult) 16 else 13,
        dto.rating/2,
        dto.votesCount,
        BuildConfig.MOVIE_BASE_URL+dto.posterPath,
        BuildConfig.MOVIE_BASE_URL+dto.posterBackdropPath,
        false,
        dto.overview
    )

    fun genre(dto: GenreDTO) = Genre(
        dto.id,
        dto.name
    )
}