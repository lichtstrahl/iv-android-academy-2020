package root.iv.ivandroidacademy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.databinding.FragmentMovieDetailsBinding
import root.iv.ivandroidacademy.ui.component.adapter.ActorAdapter
import root.iv.ivandroidacademy.ui.component.RankGroup
import root.iv.ivandroidacademy.viewmodel.MovieDetailsViewModel
import root.iv.ivandroidacademy.viewmodel.ViewModelFactory
import kotlin.math.roundToInt

@ExperimentalPagingApi
class MovieDetailsFragment: Fragment() {

    // Views
    private lateinit var actorsListView: RecyclerView
    private lateinit var backArrow: ImageView
    private lateinit var backLabel: TextView
    private lateinit var backgroundLogo: ImageView
    private lateinit var title: TextView
    private lateinit var tags: TextView
    private lateinit var rankGroup: RankGroup
    private lateinit var reviewCount: TextView
    private lateinit var story: TextView
    private lateinit var ageLimit: TextView
    private lateinit var actorsLoader: ProgressBar
    // Adapters
    private lateinit var actorAdapter: ActorAdapter

    // ViewModels
    private lateinit var movieDetailsViewModel: MovieDetailsViewModel

    // Args
    private var movieId: Int = -1

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
        backLabel.setOnClickListener(this::back)

        movieId = requireArguments().getInt(ARG_MOVIE_ID)

        actorAdapter = ActorAdapter()
        actorsListView.adapter = actorAdapter
        actorsListView.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        movieDetailsViewModel = ViewModelProvider(this, ViewModelFactory)[MovieDetailsViewModel::class.java]
        movieDetailsViewModel.loadDetails(this.viewLifecycleOwner, movieId)
        loadActors()
    }

    override fun onResume() {
        super.onResume()

        movieDetailsViewModel.movie.observe(this.viewLifecycleOwner, this::viewMovie)
        movieDetailsViewModel.actors.observe(this.viewLifecycleOwner, this::viewActors)
        movieDetailsViewModel.networkError.observe(this.viewLifecycleOwner, this::viewNetworkError)
    }

    // ---
    // PRIVATE
    // ---

    private fun binding(view: View) = FragmentMovieDetailsBinding.bind(view)
        .apply { this@MovieDetailsFragment.actorsListView = this.actorsListView }
        .apply { this@MovieDetailsFragment.backArrow = this.topMenuBackArrow }
        .apply { this@MovieDetailsFragment.backLabel = this.backLabelView }
        .apply { this@MovieDetailsFragment.backgroundLogo = this.backgroundLogo }
        .apply { this@MovieDetailsFragment.ageLimit = this.ageLimitView }
        .apply { this@MovieDetailsFragment.title = this.movieNameView }
        .apply { this@MovieDetailsFragment.tags = this.movieTagsView }
        .apply { this@MovieDetailsFragment.rankGroup = RankGroup(this@MovieDetailsFragment.requireContext(), this.star1, this.star2, this.star3, this.star4, this.star5) }
        .apply { this@MovieDetailsFragment.reviewCount = this.reviewCountView }
        .apply { this@MovieDetailsFragment.story = this.viewStoryline }
        .apply { this@MovieDetailsFragment.actorsLoader = this.actorsLoader }

    private fun viewMovie(movie: Movie) {
        backgroundLogo.load(movie.poster2)

        ageLimit.text = "${movie.ageLimit}+"
        title.text = movie.title
        tags.text = movie.tags
        rankGroup.draw(movie.rating.roundToInt())
        reviewCount.text = "${movie.reviewsCount} REVIEWS"
        story.text = movie.storyline
    }

    private fun viewActors(actors: List<Actor>) {
        actorAdapter.resetData(actors)
        actorsLoaded()
    }

    private fun viewNetworkError(errorMsg: String) {
        Snackbar.make(this.requireView(), errorMsg, Snackbar.LENGTH_SHORT).show()
    }

    private fun loadActors() {
        this.actorsLoader.visibility = View.VISIBLE
        this.actorsListView.visibility = View.INVISIBLE
    }

    private fun actorsLoaded() {
        this.actorsLoader.visibility = View.GONE
        this.actorsListView.visibility = View.VISIBLE
    }

    private fun back(view: View) = this.requireActivity().onBackPressed()
}