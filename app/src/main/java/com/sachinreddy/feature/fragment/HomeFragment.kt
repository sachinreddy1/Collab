package com.sachinreddy.feature.fragment

import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.emrekose.recordbutton.OnRecordListener
import com.evrencoskun.tableview.TableView
import com.sachinreddy.feature.R
import com.sachinreddy.feature.adapter.EditCellAdapter
import com.sachinreddy.feature.adapter.EditCellListener
import com.sachinreddy.feature.data.table.Cell
import com.sachinreddy.feature.injection.appComponent
import com.sachinreddy.feature.viewModel.AppViewModel
import kotlinx.android.synthetic.main.activity_app.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.File
import java.lang.Exception
import javax.inject.Inject


const val REQUEST_PERMISSION_CODE = 200
val PERMISSIONS = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECORD_AUDIO)

class HomeFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val appViewModel by activityViewModels<AppViewModel> { viewModelFactory }

    var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appComponent!!.inject(this)
        setupActionBar()

        // Setting up tableView and adapter
        val tableView = TableView(requireContext())
        val adapter = EditCellAdapter(requireContext(), appViewModel, appViewModel.mTrackList)
        tableView.adapter = adapter
        adapter.setTracks(appViewModel.mTrackList)
        content_container.adapter = adapter
        content_container.tableViewListener = EditCellListener(requireContext())

        ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, REQUEST_PERMISSION_CODE)

        recordBtn.setRecordListener(object : OnRecordListener {
            override fun onRecord() {
                (adapter.selectedCell as Cell).hasData = true
                adapter.notifyDataSetChanged()
                startRecording()
            }

            override fun onRecordCancel() {
                stopRecording()
            }

            override fun onRecordFinish() {
                stopRecording()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun startRecording() {
        if (isRecording) return

        setupMediaRecorder()
        try {
            mediaRecorder?.prepare();
            mediaRecorder?.start();
            isRecording = true
            Toast.makeText(requireContext(), "Recording...", Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception){
            e.printStackTrace();
        }
    }

    private fun stopRecording() {
        if (!isRecording) return

        mediaRecorder?.let {
            it.stop()
            it.release()
            isRecording = false
        }
        Toast.makeText(requireContext(), "Stopped recording...", Toast.LENGTH_SHORT).show()
    }

    private fun setupMediaRecorder() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, "/test.3gp")

        mediaRecorder?.setOutputFile(file);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home ->
                validNavController?.navigate(R.id.action_HomeFragment_to_ProfileFragment)
        }
        return true
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
