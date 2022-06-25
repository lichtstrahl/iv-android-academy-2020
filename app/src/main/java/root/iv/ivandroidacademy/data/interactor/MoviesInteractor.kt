package root.iv.ivandroidacademy.data.interactor

import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.repository.DataRepository
import java.io.Closeable

class MoviesInteractor(
    private val dataRepository: DataRepository
) {

    suspend fun movies(): List<Movie> = dataRepository.movies()
}