package root.iv.ivandroidacademy.data.interactor

import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.repository.DataRepository

class MovieInteractor(
    private val dataRepository: DataRepository
) {

    suspend fun movie(movieId: Int): Movie? = dataRepository.movie(movieId)
}