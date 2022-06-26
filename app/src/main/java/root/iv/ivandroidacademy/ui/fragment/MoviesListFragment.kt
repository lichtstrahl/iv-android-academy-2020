package root.iv.ivandroidacademy.ui.fragment

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.getDrawableOrThrow
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.cache.ConfigurationCache
import root.iv.ivandroidacademy.data.cache.GenresCache
import root.iv.ivandroidacademy.data.interactor.MovieInteractor
import root.iv.ivandroidacademy.data.mapper.Mapper
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.databinding.FragmentMoviesListBinding
import root.iv.ivandroidacademy.presenter.MoviesPresenter
import root.iv.ivandroidacademy.ui.component.adapter.MovieAdapter

class MoviesListFragment: Fragment(), MoviesPresenter.View {

    // Views
    private lateinit var moviesListView: RecyclerView
    private lateinit var searchLineView: TextInputLayout

    private lateinit var moviesAdapter: MovieAdapter
    private val presenter: MoviesPresenter = MoviesPresenter(
        MovieInteractor(
            GenresCache(App.movieDBApi, Mapper),
            ConfigurationCache(App.movieDBApi),
            Mapper,
            App.movieDBApi
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
            .apply { binding(this) }

        moviesAdapter = MovieAdapter(listener = this::onMovieClick)
        moviesListView.adapter = moviesAdapter
        moviesListView.layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.cancel()
    }

    // ---
    // View Interface
    // ---

    override fun viewMoviesList(movies: List<Movie>) {
        moviesAdapter.resetData(movies)
        searchLineView.endIconDrawable = AppCompatResources.getDrawable(this.requireContext(), R.drawable.ic_search)
        searchLineView.setEndIconOnClickListener(this::onSearchIconClick)
    }

    override fun viewLoadingMovies() {
        searchLineView.endIconDrawable = this.requireContext().getProgressBarDrawable()
        (searchLineView.endIconDrawable as Animatable).start()
        searchLineView.setEndIconOnClickListener(null)
    }

    // ---
    // PRIVATE
    // ---

    private fun binding(view: View) = FragmentMoviesListBinding.bind(view)
        .apply { this@MoviesListFragment.moviesListView = moviesListView }
        .apply { this@MoviesListFragment.searchLineView = searchLineView }

    private fun onMovieClick(movie: Movie) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(R.id.mainFrame, MovieDetailsFragment.getInstance(movie.id))
            .commit()
    }

    private fun onSearchIconClick(view: View) {
        presenter.loadMovies(searchLineView.editText?.text?.toString())
    }

    private fun Context.getProgressBarDrawable(): Drawable {
        val value = TypedValue()
        theme.resolveAttribute(android.R.attr.progressBarStyleSmall, value, false)
        val progressBarStyle = value.data
        val attributes = intArrayOf(android.R.attr.indeterminateDrawable)
        val array = obtainStyledAttributes(progressBarStyle, attributes)
        val drawable = array.getDrawableOrThrow(0)
        array.recycle()
        return drawable
    }
}