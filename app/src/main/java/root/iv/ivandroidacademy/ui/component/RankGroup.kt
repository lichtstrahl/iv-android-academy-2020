package root.iv.ivandroidacademy.ui.component

import android.content.Context
import android.widget.ImageView
import root.iv.ivandroidacademy.R

class RankGroup(
    ctx: Context,
    vararg icon: ImageView
) {
    private val icons: List<ImageView> = icon.toList()
    private val disableColor = ctx.getColor(R.color.colorDisableStar)
    private val actionColor = ctx.getColor(R.color.colorAccent)

    /**
     * TODO Пока реализована только возможность отрисовывать целочисленный рейтинг
     */
    fun draw(rank: Int) {
        if (!(0..icons.size).contains(rank))
            throw IllegalStateException("Available rank 0..${icons.size}, but rank $rank")

        for (i in (0 until icons.size)) {
            val color = if (i < rank) actionColor else disableColor
            icons[i].setColorFilter(color)
        }
    }
}