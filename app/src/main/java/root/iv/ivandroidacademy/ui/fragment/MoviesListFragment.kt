package root.iv.ivandroidacademy.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.repository.DataRepository
import root.iv.ivandroidacademy.data.model.dto.MovieDTO
import root.iv.ivandroidacademy.databinding.FragmentMoviesListBinding
import root.iv.ivandroidacademy.ui.component.MovieAdapter

class MoviesListFragment: Fragment() {

    private lateinit var moviesListView: RecyclerView
    private lateinit var moviesAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
            .apply { binding(this) }

        moviesAdapter = MovieAdapter(DataRepository.movies, this::onMovieClick)
        moviesListView.adapter = moviesAdapter
        moviesListView.layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)

        return view
    }

    // ---
    // PRIVATE
    // ---

    private fun binding(view: View) = FragmentMoviesListBinding.bind(view)
        .apply { moviesListView = moviesList }

    private fun onMovieClick(movie: MovieDTO) {

        requireActivity().supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .add(R.id.mainFrame, MovieDetailsFragment.getInstance(movie.id))
            .commit()
    }
}