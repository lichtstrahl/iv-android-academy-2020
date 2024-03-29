package root.iv.ivandroidacademy.network.client

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import root.iv.ivandroidacademy.data.model.dto.*

interface MovieDBApi {

    @GET("movie/popular")
    suspend fun moviesPopular(@Query("page") page: Int): PageContainer<MovieDTO>

    @GET("search/movie")
    suspend fun movies(@Query("page") page: Int, @Query("query") query: String): PageContainer<MovieDTO>

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