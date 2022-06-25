package root.iv.ivandroidacademy.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.serializer
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Genre
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.model.dto.ActorDTO
import root.iv.ivandroidacademy.data.model.dto.GenreDTO
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import java.io.Closeable
import java.io.InputStream


class DataRepository(
    private val inputGenres: InputStream,
    private val inputMovies: InputStream,
    private val inputActors: InputStream
): Closeable {

    private val jsonParser = Json { ignoreUnknownKeys = true }
    private val mapper = Mapper()

    private val moviesLoader = object : Repository<Movie> {
        override suspend fun loadAll(): List<Movie> = withContext(Dispatchers.IO) {
            val genresMap = genresLoader.loadAll().associateBy(Genre::id)

            inputMovies.loadDto<MovieDTO>()
                .map { movieDto -> mapper.movie(movieDto, genresMap.filter { movieDto.genreIds.contains(it.key) }.map { it.value }) }
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
        context.assets.open("people.json")
    )

    suspend fun movies() = moviesLoader.loadAll()

    suspend fun movie(id: Int) = moviesLoader.load(id)

    suspend fun actors(ids: List<Int>) = actorsLoader.loadAll().filter { ids.contains(it.id) }

    override fun close() {
        inputGenres.reset()
        inputMovies.reset()
        inputActors.reset()
    }

    // ---
    // PRIVATE
    // ---

    private inline fun <reified T: Any> InputStream.loadDto(serializer: KSerializer<T> = serializer()): List<T> =
        this.bufferedReader()
            .readText()
            .let { jsonParser.decodeFromString(JsonArray.serializer(), it) }
            .map { jsonParser.decodeFromJsonElement(serializer, it) }
}