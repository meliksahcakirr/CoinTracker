package com.meliksahcakir.cointracker.favorite

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
import com.meliksahcakir.cointracker.databinding.FavoriteFragmentBinding
import com.meliksahcakir.cointracker.ui.FavoriteCoinAdapter
import com.meliksahcakir.cointracker.ui.FavoriteCoinListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), FavoriteCoinListener {

    private var _binding: FavoriteFragmentBinding? = null
    private val binding: FavoriteFragmentBinding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModel()
    private val adapter = FavoriteCoinAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)

        viewModel.busy.observe(viewLifecycleOwner,
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

    override fun onFavoriteClicked(coin: Coin) {
        viewModel.onFavoriteClicked(coin)
    }
}
