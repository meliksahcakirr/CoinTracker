package com.meliksahcakir.cointracker.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meliksahcakir.cointracker.databinding.DetailsFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : Fragment() {

    private var _binding: DetailsFragmentBinding? = null
    private val binding: DetailsFragmentBinding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailsFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
