package com.sachinreddy.feature.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.ArtistAdapter
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.data.Artist
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_friends.*


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FriendsFragment : Fragment() {
    lateinit var adapter: ArtistAdapter

    private val mTotalValueEventListener = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            var artists: MutableList<Artist> = mutableListOf()
            for (i in snapshot.children) {
                i.getValue(Artist::class.java)?.let {
                    if (it.artistId != Authenticator.currentUser?.artistId)
                        artists.add(it)
                }
            }

            adapter.artistsFull = artists
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()
        adapter = ArtistAdapter(context!!, Authenticator.currentFriends)
        friends_recycler_view.adapter = adapter

        swipe_refresh.setOnRefreshListener {
            adapter.artists = Authenticator.currentFriends.toMutableList()
            adapter.artistsFull = Authenticator.currentFriends.toMutableList()
            adapter.notifyDataSetChanged()
            swipe_refresh.isRefreshing = false
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_friends, menu)

        val manager = requireContext().getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchArtist = menu?.findItem(R.id.search_friends)
        val searchView = searchArtist?.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = "Search Artists"

        searchView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            // Search was closed
            override fun onViewDetachedFromWindow(v: View?) {
                // Display current friends
            }

            // Search was opened
            override fun onViewAttachedToWindow(v: View?) {
                // Get entire list of artist
                Authenticator.mDatabaseReference.addListenerForSingleValueEvent(
                    mTotalValueEventListener
                )
                // Clear the list of items
                adapter.notifyDataSetChanged()
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // Enter button is pressed
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            // Filter artists based on query
            override fun onQueryTextChange(newText: String): Boolean {
                adapter.artistFilter.filter(newText)
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_FriendsFragment_to_HomeFragment)
        }
        return true
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.artists)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.FriendsFragment
}
