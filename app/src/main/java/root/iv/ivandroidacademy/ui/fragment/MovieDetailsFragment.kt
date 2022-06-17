package root.iv.ivandroidacademy.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.repository.DataRepository
import root.iv.ivandroidacademy.databinding.FragmentMovieDetailsBinding
import root.iv.ivandroidacademy.ui.component.ActorAdapter
import root.iv.ivandroidacademy.ui.component.RankGroup
import kotlin.math.roundToInt

@ExperimentalSerializationApi
class MovieDetailsFragment: Fragment() {

    private lateinit var actorsListView: RecyclerView
    private lateinit var backArrow: ImageView
    private lateinit var backgroundLogo: ImageView
    private lateinit var title: TextView
    private lateinit var tags: TextView
    private lateinit var rankGroup: RankGroup
    private lateinit var reviewCount: TextView
    private lateinit var story: TextView
    private lateinit var ageLimit: TextView
    private lateinit var actorAdapter: ActorAdapter
    private var movieId: Int = -1
    private lateinit var dataRepository: DataRepository
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        private const val ARG_MOVIE_ID = "arg:movie-id"

        fun getInstance(movieId: Int) = MovieDetailsFragment().apply {
            this.arguments = Bundle().apply { this.putInt(ARG_MOVIE_ID, movieId) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
            .apply { binding(this) }

        backArrow.setOnClickListener(this::back)

        movieId = requireArguments().getInt(ARG_MOVIE_ID)

        actorAdapter = ActorAdapter()
        actorsListView.adapter = actorAdapter
        actorsListView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(this.requireContext())
    }

    override fun onStart() {
        super.onStart()

        coroutineScope.launch {
            dataRepository.movie(movieId)?.let {
                actorAdapter.resetData(dataRepository.actors(it.actorIds))
                drawMovie(it)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        dataRepository.close()
    }

    // ---
    // PRIVATE
    // ---

    private fun binding(view: View) = FragmentMovieDetailsBinding.bind(view)
        .apply { this@MovieDetailsFragment.actorsListView = this.actorsListView }
        .apply { this@MovieDetailsFragment.backArrow = this.topMenuBackArrow }
        .apply { this@MovieDetailsFragment.backgroundLogo = this.backgroundLogo }
        .apply { this@MovieDetailsFragment.ageLimit = this.ageLimitView }
        .apply { this@MovieDetailsFragment.title = this.movieNameView }
        .apply { this@MovieDetailsFragment.tags = this.movieTagsView }
        .apply { this@MovieDetailsFragment.rankGroup = RankGroup(this@MovieDetailsFragment.requireContext(), this.star1, this.star2, this.star3, this.star4, this.star5) }
        .apply { this@MovieDetailsFragment.reviewCount = this.reviewCountView }
        .apply { this@MovieDetailsFragment.story = this.viewStoryline }

    private suspend fun drawMovie(movie: Movie) = withContext(Dispatchers.Main) {
        Glide.with(this@MovieDetailsFragment.requireContext())
            .load(movie.poster2)
            .into(backgroundLogo)

        ageLimit.text = "${movie.ageLimit}+"
        title.text = movie.title
        tags.text = movie.tags
        rankGroup.draw(movie.rating.roundToInt())
        reviewCount.text = "${movie.reviewsCount} REVIEWS"
        story.text = movie.storyline
    }

    private fun back(view: View) = this.requireActivity().onBackPressed()
}