package root.iv.ivandroidacademy.network.client

import retrofit2.http.GET
import retrofit2.http.Path
import root.iv.ivandroidacademy.data.model.dto.*

interface MovieDBApi {

    @GET("movie/popular")
    suspend fun movies(): PageContainer<MovieDTO>

    @GET("movie/{id}")
    suspend fun movie(@Path("id") movieId: Int): MovieDTO

    @GET("movie/{id}/credits")
    suspend fun actors(@Path("id") movieId: Int): MovieCreditsDTO

    @GET("person/{id}")
    suspend fun actor(@Path("id") actorId: Int): ActorDTO

    @GET("genre/movie/list")
    suspend fun genres(): GenresContainer

    @GET("configuration")
    suspend fun configuration(): ImageConfigurationDTO
}