package root.iv.ivandroidacademy.data.cache

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import root.iv.ivandroidacademy.data.database.dao.ImageConfigDao
import root.iv.ivandroidacademy.data.database.entity.ImageConfigEntity
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.dto.ImageConfigurationDTO
import root.iv.ivandroidacademy.network.client.MovieDBApi

class ConfigurationCache(
    private val movieDBApi: MovieDBApi,
    private val imageConfigDao: ImageConfigDao,
    private val mapper: Mapper
): DatabaseCache<ImageConfigurationDTO.Config> {

    override suspend fun get(): ImageConfigurationDTO.Config {
        val config = imageConfigDao.selectAll()
            .firstOrNull()
            ?.let { mapper.imageConfig(it) }


        return config ?: refresh()
    }

    override suspend fun refresh(): ImageConfigurationDTO.Config {
        val config = withContext(Dispatchers.IO) {
            movieDBApi.configuration().config
        }

        imageConfigDao.insert(mapper.entity(config))

        return config
    }

}