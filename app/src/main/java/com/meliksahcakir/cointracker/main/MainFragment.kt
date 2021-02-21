package com.meliksahcakir.cointracker.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.meliksahcakir.cointracker.databinding.MainFragmentBinding
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
