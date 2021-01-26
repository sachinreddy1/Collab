package com.sachinreddy.feature.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sachinreddy.feature.R
import com.sachinreddy.feature.databinding.ActivityAppBinding
import com.sachinreddy.feature.injection.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

class AppActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_app)
    }

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}
