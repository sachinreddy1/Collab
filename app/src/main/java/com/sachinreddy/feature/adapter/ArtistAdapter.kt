package com.sachinreddy.feature.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sachinreddy.feature.R
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.data.Artist
import de.hdodenhof.circleimageview.CircleImageView

class ArtistAdapter(val context: Context, artists_: MutableList<Artist>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var artists: MutableList<Artist> = ArrayList()
    var artistsFull: MutableList<Artist> = ArrayList()

    init {
        this.artists = artists_.toMutableList()
        this.artistsFull = this.artists.toMutableList()
    }

    val artistFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList: MutableList<Artist> = Authenticator.currentFriends.toMutableList()
            if (constraint != null && constraint.isNotEmpty()) {
                filteredList.clear()
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in artistsFull) {
                    item.artistName?.let {
                        if (it.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item)
                        }
                    }
                }
            }

            var results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            artists.clear()
            artists.addAll(results?.values as List<Artist>)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_artist, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int = artists.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ArtistViewHolder).setArtistDetails(artists[position])

    internal inner class ArtistViewHolder(artistView: View) : RecyclerView.ViewHolder(artistView) {
        private val artistName: TextView = artistView.findViewById(R.id.artistName)
        private val username: TextView = artistView.findViewById(R.id.username)
        private val score: TextView = artistView.findViewById(R.id.score)
        private val profilePicture: CircleImageView = artistView.findViewById(R.id.profilePicture)
        private val textureBackground: ImageView = artistView.findViewById(R.id.textureBackground)
        private val action_button: FloatingActionButton =
            artistView.findViewById(R.id.action_button)

        fun setArtistDetails(artist: Artist) {
            artistName.text = artist.artistName
            username.text = artist.username
            score.text = artist.score

            Authenticator.currentUser?.friends?.let {
                if (it.contains(artist.artistId)) {
                    action_button.apply {
                        setImageResource(R.drawable.ic_close)
                        setOnClickListener {
                            artist.artistId?.let { id ->
                                Authenticator.currentUser?.let { artist ->
                                    artist.friends?.remove(id)
                                }
                            }
                            Authenticator.apply {
                                currentUser?.let { artist ->
                                    mDatabaseReference.child(artist.artistId!!)
                                        .setValue(currentUser)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Removed a friend!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            setImageResource(R.drawable.ic_add)
                                            notifyDataSetChanged()
                                        }
                                }
                            }
                        }
                    }
                } else {
                    action_button.apply {
                        setImageResource(R.drawable.ic_add)
                        setOnClickListener {
                            artist.artistId?.let { id ->
                                Authenticator.currentUser?.let { artist ->
                                    artist.friends?.add(id)
                                }
                            }
                            Authenticator.apply {
                                currentUser?.let { artist ->
                                    mDatabaseReference.child(artist.artistId!!)
                                        .setValue(currentUser)
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context,
                                                "Added a friend!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            setImageResource(R.drawable.ic_close)
                                            notifyDataSetChanged()
                                        }
                                }
                            }
                        }
                    }
                }
            }

            Glide
                .with(context)
                .load(artist.profilePicture)
                .placeholder(R.drawable.doggi_target)
                .dontAnimate()
                .into(profilePicture)

            Glide
                .with(context)
                .load(artist.textureBackground)
                .placeholder(R.drawable.ic_pattern_background)
                .into(textureBackground)
        }
    }
}