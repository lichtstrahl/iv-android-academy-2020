package root.iv.ivandroidacademy.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.ui.fragment.MovieDetailsFragment
import root.iv.ivandroidacademy.ui.fragment.MoviesListFragment
import root.iv.ivandroidacademy.ui.fragment.openDetails

@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            handleIntent(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    // ---
    // PRIVATE
    // ---

    private fun handleIntent(intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                val movieId = intent.data?.lastPathSegment?.toInt()

                if (movieId != null)
                    openMovieDetails(movieId)
                else
                    openMoviesList()
            }
            else -> openMoviesList()
        }
    }

    private fun openMoviesList() = supportFragmentManager
        .beginTransaction()
        .replace(R.id.mainFrame, MoviesListFragment())
        .commit()

    private fun openMovieDetails(movieId: Int) {
        openMoviesList()
        supportFragmentManager.openDetails(movieId)
    }
}