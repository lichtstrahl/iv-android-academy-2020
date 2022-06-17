package root.iv.ivandroidacademy.ui.fragment

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
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.repository.DataRepository
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import root.iv.ivandroidacademy.databinding.FragmentMovieDetailsBinding
import root.iv.ivandroidacademy.ui.component.ActorAdapter
import root.iv.ivandroidacademy.ui.component.RankGroup
import kotlin.math.roundToInt

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

        val movie = requireArguments().getInt(ARG_MOVIE_ID)
            .let { movieId -> DataRepository.movies.first { it.id == movieId } }
        val actors = DataRepository.actors
            .filter { actor -> movie.castIds.contains(actor.id) }
        actorAdapter = ActorAdapter(actors)
        actorsListView.adapter = actorAdapter
        actorsListView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)

        drawMovie(movie)

        return view
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

    private fun drawMovie(movie: MovieDTO) {
        Glide.with(this.requireContext())
            .load(movie.posterPath)
            .into(backgroundLogo)

        ageLimit.text = movie.pg
        title.text = movie.title
        tags.text = movie.genreIds
        rankGroup.draw(movie.rating.roundToInt())
        reviewCount.text = "${movie.votesCount} REVIEWS"
        story.text = movie.overview
    }

    private fun back(view: View) = this.requireActivity().onBackPressed()
}