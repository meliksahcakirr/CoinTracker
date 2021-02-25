package com.meliksahcakir.cointracker.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.meliksahcakir.androidutils.EventObserver
import com.meliksahcakir.androidutils.afterTextChanged
import com.meliksahcakir.androidutils.hideKeyboard
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinInfo
import com.meliksahcakir.cointracker.databinding.MainFragmentBinding
import com.meliksahcakir.cointracker.ui.CoinAdapter
import com.meliksahcakir.cointracker.ui.CoinListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(), CoinListener {

    private var _binding: MainFragmentBinding? = null
    private val binding: MainFragmentBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()
    private val adapter = CoinAdapter(this)

    private var searchWindow: ListPopupWindow? = null

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

        viewModel.startTimer()
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
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (searchWindow?.isShowing == true) {
                searchWindow?.dismiss()
            } else {
                isEnabled = false
                activity?.onBackPressed()
            }
        }
        setupSortMenu()
        setupSearch()
    }

    private fun setupSortMenu() {
        val array = requireContext().resources.getStringArray(R.array.sort_methods)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                viewModel.changeOrder(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.sortImageView.setOnClickListener {
            binding.spinner.performClick()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun setupSearch() {
        searchWindow = ListPopupWindow(requireActivity(), null, R.attr.listPopupWindowStyle)
        searchWindow?.anchorView = binding.searchCardView
        searchWindow?.setForceIgnoreOutsideTouch(true)
        searchWindow?.height =
            (Resources.getSystem().displayMetrics.heightPixels * SEARCH_HEIGHT_RATIO).toInt()
        binding.searchEditText.afterTextChanged {
            viewModel.search(it)
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.search(binding.searchEditText.text.toString())
                binding.root.hideKeyboard()
            }
            true
        }
        viewModel.searchResults.observe(
            viewLifecycleOwner,
            Observer { coinList ->
                val list = coinList ?: emptyList()
                val adapter = ArrayAdapter(requireContext(), R.layout.search_item, list)
                searchWindow?.setAdapter(adapter)
                if (coinList.isNullOrEmpty()) {
                    searchWindow?.let {
                        if (it.isShowing) {
                            it.dismiss()
                        }
                    }
                } else {
                    searchWindow?.setOnItemClickListener { adapterView, _, pos, _ ->
                        val coinInfo = adapterView.adapter.getItem(pos) as? CoinInfo
                        searchWindow?.dismiss()
                        coinInfo?.let {
                            viewModel.onCoinSelected(coinInfo.id)
                        }
                    }
                    binding.root.post {
                        searchWindow?.show()
                    }
                }
            }
        )
        viewModel.navigateToDetailsPage.observe(
            viewLifecycleOwner,
            EventObserver {
                val action = MainFragmentDirections.actionMainFragmentToDetailsFragment(it)
                findNavController().navigate(action)
            }
        )
    }

    override fun onDestroyView() {
        viewModel.stopTimer()
        super.onDestroyView()
        _binding = null
    }

    override fun onClicked(coin: Coin) {
        if (searchWindow?.isShowing == true) {
            searchWindow?.dismiss()
        } else {
            viewModel.onCoinSelected(coin.id)
        }
    }
}
