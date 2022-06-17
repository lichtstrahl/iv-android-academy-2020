package root.iv.ivandroidacademy.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.ActorDTO
import root.iv.ivandroidacademy.data.model.dto.GenreDTO
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import java.io.Closeable
import java.io.InputStream


@ExperimentalSerializationApi
class DataRepository(
    private val inputGenres: InputStream,
    private val inputMovies: InputStream,
    private val inputActors: InputStream
): Closeable {

    private val jsonParser = Json { ignoreUnknownKeys = true }
    private val mapper = Mapper()

    private val moviesLoader = object : Repository<Movie> {
        override suspend fun loadAll(): List<Movie> = withContext(Dispatchers.IO) {
            inputMovies.loadDto<MovieDTO>()
                .map { mapper.movie(it) }
        }

        override suspend fun load(id: Int): Movie? = withContext(Dispatchers.IO) {
            loadAll().find { it.id == id }
        }
    }

    private val actorsLoader = object : Repository<Actor> {
        override suspend fun loadAll(): List<Actor> = withContext(Dispatchers.IO) {
            inputActors.loadDto<ActorDTO>()
                .map { mapper.actor(it) }
        }

        override suspend fun load(id: Int): Actor? = withContext(Dispatchers.IO) {
            loadAll().find { it.id == id }
        }
    }

    private val genresLoader = object : Repository<Genre> {
        override suspend fun load(id: Int): Genre? = withContext(Dispatchers.IO) {
            loadAll().find { it.id == id }
        }

        override suspend fun loadAll(): List<Genre> = withContext(Dispatchers.IO) {
            inputGenres.loadDto<GenreDTO>()
                .map { mapper.genre(it) }
        }

    }

    constructor(context: Context): this(
        context.assets.open("genres.json"),
        context.assets.open("data.json"),
        context.assets.open("actors.json")
    )

    suspend fun movies() = moviesLoader.loadAll()

    suspend fun actor(ids: List<Int>) = actorsLoader.loadAll().filter { ids.contains(it.id) }

    suspend fun genres(ids: List<Int>) = genresLoader.loadAll().filter { ids.contains(it.id) }

    override fun close() {
        inputGenres.close()
        inputMovies.close()
        inputActors.close()
    }

    // ---
    // PRIVATE
    // ---

    private fun <T> InputStream.loadDto(): List<T> = this.bufferedReader().readText()
        .let { jsonParser.decodeFromString(it) }

    private fun <T> T?.orThrow(throwSupplier: () -> Throwable): T = this ?: throw throwSupplier.invoke()
}