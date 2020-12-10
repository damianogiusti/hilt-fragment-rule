package com.damianogiusti.hilt.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.damianogiusti.hilt.sample.utils.factory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Damiano Giusti on 10/12/2020.
 */
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    @Inject lateinit var provider: Provider<DefaultHomeViewModel>

    private val viewModel by viewModels<HomeViewModel>(factoryProducer = factory { provider })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.message.observe(viewLifecycleOwner) { message ->
            view.findViewById<TextView>(R.id.home_text_view).text = message
        }
    }
}