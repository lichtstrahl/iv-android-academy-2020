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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.databinding.FragmentMoviesListBinding
import root.iv.ivandroidacademy.ui.component.adapter.MovieAdapter
import root.iv.ivandroidacademy.viewmodel.MoviesListViewModel
import root.iv.ivandroidacademy.viewmodel.ViewModelFactory

class MoviesListFragment: Fragment() {

    // Views
    private lateinit var moviesListView: RecyclerView
    private lateinit var searchLineView: TextInputLayout

    private lateinit var moviesAdapter: MovieAdapter

    // ViewModels
    private lateinit var moviesViewModel: MoviesListViewModel

    companion object {
        private const val RECREATE_INPUT_SEARCH_LINE = "recreate:input-search-line"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies_list, container, false)
            .apply { binding(this) }

        moviesAdapter = MovieAdapter(this::onMovieClick)
        moviesListView.adapter = moviesAdapter
        moviesListView.layoutManager = GridLayoutManager(this.requireContext(), 2, GridLayoutManager.VERTICAL, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        moviesViewModel = ViewModelProvider(this, ViewModelFactory)[MoviesListViewModel::class.java]
        moviesViewModel.loadMovies(searchLineView.editText?.text?.toString())
        loadMovies()
    }

    override fun onResume() {
        super.onResume()
        moviesViewModel.movies.observe(this.viewLifecycleOwner, this::viewMoviesList)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(RECREATE_INPUT_SEARCH_LINE, searchLineView.editText?.text?.toString())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        savedInstanceState?.let { bundle ->
            val searchLine = bundle.getString(RECREATE_INPUT_SEARCH_LINE) ?: ""
            searchLineView.editText?.setText(searchLine)
        }
    }

    // ---
    // View Interface
    // ---

    fun viewMoviesList(movies: PagingData<Movie>) {
        this.viewLifecycleOwner.lifecycleScope.launch {
            moviesAdapter.submitData(movies)
            searchLineView.endIconDrawable = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_search)
            searchLineView.setEndIconOnClickListener { loadMovies() }
        }
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

    private fun loadMovies() {
        moviesViewModel.loadMovies(searchLineView.editText?.text?.toString())
        viewLoadingMovies()
    }

    private fun viewLoadingMovies() {
        searchLineView.endIconDrawable = this.requireContext().getProgressBarDrawable()
        (searchLineView.endIconDrawable as Animatable).start()
        searchLineView.setEndIconOnClickListener(null)
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