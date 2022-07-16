package root.iv.ivandroidacademy.ui.notify

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.paging.ExperimentalPagingApi
import coil.Coil
import coil.request.ImageRequest
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.database.entity.MovieEntity
import root.iv.ivandroidacademy.ui.activity.MainActivity

@ExperimentalPagingApi
class NotifyPublisher(
    ctx: Context
) {

    companion object {
        private const val POPULAR_MOVIE_CHANNEL_ID = "channel:popular-movie"
        private const val POPULAR_MOVIE_REQUEST_ID = 5
    }

    private val manager = NotificationManagerCompat.from(ctx)
    init {
        NotificationChannelCompat.Builder(POPULAR_MOVIE_CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
            .setName(ctx.getString(R.string.popular_movies_channel))
            .setDescription(ctx.getString(R.string.popular_movies_channel_desc))
            .build()
            .apply { manager.createNotificationChannel(this) }
    }

    suspend fun notifyPopularMovie(ctx: Context, movie: MovieEntity) {
        val icon = ResourcesCompat.getDrawable(ctx.resources, R.drawable.ic_popular_movie, ctx.theme)!!.toBitmap()
        val poster = movie.posterUrl?.let { ctx.loadIcon(it) } ?: icon

        NotificationCompat.Builder(ctx, POPULAR_MOVIE_CHANNEL_ID)
            .setContentTitle(movie.title)
            .setContentText(ctx.getString(R.string.popular_movies_notification_content))
            .setContentIntent(movie.pendingIntent(ctx))
            .setSmallIcon(IconCompat.createWithBitmap(icon))
            .setLargeIcon(poster)
            .setAutoCancel(true)
            .build()
            .apply { manager.notify(1, this) }
    }

    // ---
    // PRIVATE
    // ---

    private fun MovieEntity.pendingIntent(ctx: Context) = Intent(Intent.ACTION_VIEW, Uri.parse("iv://movies.app/movie/${this.id}"), ctx, MainActivity::class.java)
        .let {
            PendingIntent.getActivity(
                ctx, POPULAR_MOVIE_REQUEST_ID,
                it, PendingIntent.FLAG_IMMUTABLE
            )
        }

    private suspend fun Context.loadIcon(url: String): Bitmap? {
        val request = ImageRequest.Builder(this)
            .data(url)
            .build()
        return Coil.imageLoader(this).execute(request).drawable?.toBitmap()
    }
}