package root.iv.ivandroidacademy.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.data.repository.DataRepository
import root.iv.ivandroidacademy.databinding.FragmentMoviesListBinding
import root.iv.ivandroidacademy.ui.component.MovieAdapter

@ExperimentalSerializationApi
class MoviesListFragment: Fragment() {

    private lateinit var moviesListView: RecyclerView
    private lateinit var moviesAdapter: MovieAdapter
    private lateinit var dataRepository: DataRepository
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataRepository = DataRepository(this.requireContext())
    }

    override fun onStart() {
        super.onStart()
        coroutineScope.launch { moviesAdapter.resetData(dataRepository.movies()) }
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.cancel()
    }

    override fun onDetach() {
        super.onDetach()
        dataRepository.close()
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