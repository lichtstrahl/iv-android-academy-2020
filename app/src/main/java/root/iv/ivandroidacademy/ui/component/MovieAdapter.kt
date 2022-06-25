package root.iv.ivandroidacademy.ui.component

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Movie
import root.iv.ivandroidacademy.databinding.FilmCardBinding
import root.iv.ivandroidacademy.ui.component.adapter.DiffUtilAdapter
import kotlin.math.roundToInt

class MovieAdapter(
    movies: List<Movie> = listOf(),
    listener: (Movie) -> Unit
): DiffUtilAdapter<Movie, MovieAdapter.MovieViewHolder>(movies, listener) {

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val ageLimit: TextView
        private val like: ImageView
        private val logo: ImageView
        private val tag: TextView
        private val rankGroup: RankGroup
        private val reviewCount: TextView
        private val title: TextView
        private val time: TextView

        init {
            FilmCardBinding.bind(itemView)
                .apply { this@MovieViewHolder.ageLimit = this.ageLimit }
                .apply { this@MovieViewHolder.like = this.like }
                .apply { this@MovieViewHolder.logo = this.logo }
                .apply { this@MovieViewHolder.tag = this.tag }
                .apply { this@MovieViewHolder.rankGroup = RankGroup(this@MovieViewHolder.itemView.context, this.star1, this.star2, this.star3, this.star4, this.star5) }
                .apply { this@MovieViewHolder.reviewCount = this.viewReviewCount }
                .apply { this@MovieViewHolder.title = this.title }
                .apply { this@MovieViewHolder.time = this.time }
        }

        fun bind(movie: Movie) {
            ageLimit.text = "${movie.ageLimit}+"
            tag.text = movie.tags

            val backgroundLogoTarget = object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    logo.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    logo.background = null
                }

            }
            Glide.with(this.itemView.context)
                .load(movie.poster)
                .into(backgroundLogoTarget)
            rankGroup.draw(movie.rating.roundToInt())
            reviewCount.text = "${movie.reviewsCount} REVIEWS"
            title.text = movie.title
            time.text = movie.duration?.let { "$it MIN" } ?: "-"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.film_card, parent, false)
        .let { MovieViewHolder(it) }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) = holder
        .bind(content[position])
        .apply { holder.itemView.setOnClickListener { listener.invoke(content[position]) } }


    override fun getItemCount(): Int = content.size
}