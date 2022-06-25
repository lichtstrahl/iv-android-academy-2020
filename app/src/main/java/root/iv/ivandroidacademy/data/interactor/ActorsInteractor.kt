package root.iv.ivandroidacademy.data.interactor

import root.iv.ivandroidacademy.data.repository.DataRepository

class ActorsInteractor(
    private val dataRepository: DataRepository
) {

    suspend fun actors(id: Int) = dataRepository.actors(id)
}