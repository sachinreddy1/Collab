package com.sachinreddy.feature.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.SongAdapter
import com.sachinreddy.feature.auth.Authenticator
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val appViewModel by activityViewModels<AppViewModel> { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        appComponent!!.inject(this)
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_homeFragment_to_profileFragment)
            R.id.action_start_collab ->
                validNavController?.navigate(R.id.action_homeFragment_to_friendsFragment)
            R.id.action_solo_session ->
                Snackbar.make(requireView(), "Starting solo session...", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupActionBar()

        val adapter = SongAdapter(requireContext())
        songs_recycler_view.adapter = adapter

        // Songs go here
        Authenticator.currentUser?.let {
            adapter.songs = it.songs
            adapter.notifyDataSetChanged()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupActionBar() {
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(app_action_bar)
            supportActionBar?.apply {
                title = getString(R.string.app_name)
                setDisplayHomeAsUpEnabled(true)
                setHomeAsUpIndicator(R.drawable.ic_account_circle_dark)
                setHomeActionContentDescription(getString(R.string.open_profile_card))
            }
        }
    }

    private val validNavController get() = findNavController().takeIf { it.valid }
    private val NavController.valid get() = currentDestination?.id == R.id.HomeFragment
}
