package root.iv.ivandroidacademy.data.mapper

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

    fun movie(dto: MovieDTO) = Movie(
        dto.id,
        dto.title,
        dto.genreIds,
        dto.duration,
        if (dto.adult) 16 else 13,
        dto.rating,
        dto.votesCount,
        dto.posterPath,
        dto.posterBackdropPath,
        false,
        dto.overview,
        dto.actorIds
    )

    fun genre(dto: GenreDTO) = Genre(
        dto.id,
        dto.name
    )
}