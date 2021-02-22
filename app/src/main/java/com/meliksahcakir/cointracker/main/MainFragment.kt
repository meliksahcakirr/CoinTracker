package com.meliksahcakir.cointracker.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.meliksahcakir.androidutils.EventObserver
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.databinding.MainFragmentBinding
import com.meliksahcakir.cointracker.ui.CoinAdapter
import com.meliksahcakir.cointracker.ui.CoinListener
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MainFragment : Fragment(), CoinListener {

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!

    private val viewModel: MainViewModel by sharedViewModel()
    private val adapter = CoinAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.busy.observe(
            viewLifecycleOwner,
            Observer {
                binding.progressBar.isVisible = it == true
            }
        )
        viewModel.warningEvent.observe(
            viewLifecycleOwner,
            EventObserver {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        )
        viewModel.coins.observe(
            viewLifecycleOwner,
            Observer {
                if (it.isNullOrEmpty()) {
                    binding.recyclerView.isVisible = false
                    binding.emptyGroup.isVisible = true
                    adapter.submitList(emptyList())
                } else {
                    binding.recyclerView.isVisible = true
                    binding.emptyGroup.isVisible = false
                    adapter.submitList(it)
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClicked(coin: Coin) {
    }
}
