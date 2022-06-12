package root.iv.ivandroidacademy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import root.iv.ivandroidacademy.R

class MovieDetailsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)

        view.findViewById<View>(R.id.topMenuBackArrow).setOnClickListener {
            this.requireActivity().onBackPressed()
        }

        return view
    }
}