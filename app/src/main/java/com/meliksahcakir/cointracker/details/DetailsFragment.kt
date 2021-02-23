package com.meliksahcakir.cointracker.details

import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.meliksahcakir.androidutils.EventObserver
import com.meliksahcakir.cointracker.R
import com.meliksahcakir.cointracker.data.Coin
import com.meliksahcakir.cointracker.data.CoinDetails
import com.meliksahcakir.cointracker.databinding.DetailsFragmentBinding
import com.meliksahcakir.cointracker.utils.setImageUrl
import com.meliksahcakir.cointracker.utils.toSpanned
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.NumberFormat
import java.util.Locale

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupIntervalMenu()
        arguments?.let {
            val args = DetailsFragmentArgs.fromBundle(it)
            viewModel.fetchInitialData(args.id)
        }

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

        viewModel.coin.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                bindCoin(it)
            }
        })

        viewModel.details.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                bindCoinDetails(it)
            }
        })

        viewModel.favorite.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_on)
            } else {
                binding.favoriteImageView.setImageResource(R.drawable.ic_favorite_off)
            }
        })

        binding.favoriteImageView.setOnClickListener {
            viewModel.onFavoriteButtonClicked()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bindCoin(coin: Coin) {
        val coinText = "${coin.symbol.toUpperCase(Locale.getDefault())} / USD"
        binding.coinTextView.text = coinText
        val volumeText = "Vol ${NumberFormat.getInstance().format(coin.totalVolume)}"
        binding.volumeTextView.text = volumeText
        val lastPriceText = "$${NumberFormat.getInstance().format(coin.currentPrice)}"
        binding.lastPriceTextView.text = lastPriceText
        val highPriceText = "$${NumberFormat.getInstance().format(coin.high24h)}"
        binding.highTextView.text = highPriceText
        val lowPriceText = "$${NumberFormat.getInstance().format(coin.low24h)}"
        binding.lowTextView.text = lowPriceText
        with(binding.percentageTextView) {
            val percentage = "${"%.2f".format(coin.priceChangePercentage24h)}%"
            text = percentage
            val colorResource = when {
                coin.priceChangePercentage24h > 0f -> R.color.green
                coin.priceChangePercentage24h < 0f -> R.color.redDark
                else -> R.color.grayLight
            }
            val color = ContextCompat.getColor(requireContext(), colorResource)
            setTextColor(color)
        }
    }

    private fun bindCoinDetails(details: CoinDetails) {
        with(binding.infoSheet) {
            coinImageView.setImageUrl(details.large)
            val coinText = "${details.name} (${details.symbol.toUpperCase(Locale.getDefault())})"
            coinTextView.text = coinText
            hashingTextView.text = details.hashingAlgorithm
            descriptionTextView.text = details.description.toSpanned()
            Linkify.addLinks(descriptionTextView, Linkify.WEB_URLS)
        }
    }

    private fun setupIntervalMenu() {
        val strArray = requireContext().resources.getStringArray(R.array.update_intervals_str)
        val intArray = requireContext().resources.getIntArray(R.array.update_intervals_int)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, strArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinner.adapter = adapter
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, position: Int, id: Long) {
                viewModel.updateTimerInterval(intArray[position].toLong())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.settingsImageView.setOnClickListener {
            binding.spinner.performClick()
        }
    }
}
