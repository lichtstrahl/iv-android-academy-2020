package root.iv.ivandroidacademy.ui.fragment

import androidx.fragment.app.FragmentManager
import androidx.paging.ExperimentalPagingApi
import root.iv.ivandroidacademy.R

@ExperimentalPagingApi
fun FragmentManager.openDetails(movieId: Int) = this
    .beginTransaction()
    .addToBackStack(null)
    .setCustomAnimations(R.anim.open_movie_details, 0, 0, R.anim.close_movie_details)
    .add(R.id.mainFrame, MovieDetailsFragment.getInstance(movieId))
    .commit()