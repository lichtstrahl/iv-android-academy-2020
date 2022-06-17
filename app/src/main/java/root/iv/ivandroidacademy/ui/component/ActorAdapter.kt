package root.iv.ivandroidacademy.ui.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.dto.ActorDTO
import root.iv.ivandroidacademy.databinding.ActorCardBinding

class ActorAdapter(
    private val actors: List<ActorDTO> = listOf()
): RecyclerView.Adapter<ActorAdapter.ActorViewHolder>() {

    class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView
        private val name: TextView

        init {
            ActorCardBinding.bind(itemView)
                .apply { this@ActorViewHolder.avatar = this.avatar }
                .apply { this@ActorViewHolder.name = this.name }
        }

        fun bind(actor: ActorDTO) {
            name.text = actor.name
            Glide.with(itemView.context)
                .load(actor.photoUrl)
                .into(avatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.actor_card, parent, false)
        .let { ActorViewHolder(it) }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) = holder.bind(actors[position])

    override fun getItemCount(): Int = actors.size

}