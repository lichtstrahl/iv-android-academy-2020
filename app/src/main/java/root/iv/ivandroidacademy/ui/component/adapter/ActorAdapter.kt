package root.iv.ivandroidacademy.ui.component.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import root.iv.ivandroidacademy.R
import root.iv.ivandroidacademy.data.model.Actor
import root.iv.ivandroidacademy.databinding.ActorCardBinding

class ActorAdapter(
    actors: List<Actor> = listOf()
): DiffUtilAdapter<Actor, ActorAdapter.ActorViewHolder>(actors, {}) {

    class ActorViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val avatar: ImageView
        private val name: TextView

        init {
            ActorCardBinding.bind(itemView)
                .apply { this@ActorViewHolder.avatar = this.avatar }
                .apply { this@ActorViewHolder.name = this.name }
        }

        fun bind(actor: Actor) {
            name.text = actor.name

            avatar.load(actor.photoUrl) {
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder = LayoutInflater
        .from(parent.context)
        .inflate(R.layout.actor_card, parent, false)
        .let { ActorViewHolder(it) }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) = holder.bind(content[position])

    override fun getItemCount(): Int = content.size
}