package root.iv.ivandroidacademy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.app.App
import root.iv.ivandroidacademy.data.interactor.MoviesInteractor
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.databinding.FragmentMoviesListBinding
import root.iv.ivandroidacademy.presenter.MoviesPresenter
import root.iv.ivandroidacademy.ui.component.adapter.MovieAdapter

class MoviesListFragment: Fragment(), MoviesPresenter.View {

    private lateinit var moviesListView: RecyclerView
    private lateinit var moviesAdapter: MovieAdapter
    private val presenter: MoviesPresenter = MoviesPresenter(
        MoviesInteractor(App.dataRepository)
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

    override fun viewMoviesList(movies: List<Movie>) {
        moviesAdapter.resetData(movies)
    }

    // ---
    // PRIVATE
    // ---

    private fun binding(view: View) = FragmentMoviesListBinding.bind(view)
        .apply { moviesListView = moviesList }

    private fun onMovieClick(movie: Movie) {

        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(R.id.mainFrame, MovieDetailsFragment.getInstance(movie.id))
            .commit()
    }
}