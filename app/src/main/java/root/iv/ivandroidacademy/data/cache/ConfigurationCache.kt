package root.iv.ivandroidacademy.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.model.dto.ImageConfigurationDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi

class ConfigurationCache(
    movieDBApi: MovieDBApi
): Cache<ImageConfigurationDTO.Config>({
    withContext(Dispatchers.IO) {
        movieDBApi.configuration().config
    }
})