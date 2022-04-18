package com.reasons.bitcoin


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.reasons.bitcoin.broadcasts.AlarmReceiver
import com.reasons.bitcoin.data.CoinRate
import com.reasons.bitcoin.data.TargetToAchieve
import com.reasons.bitcoin.databinding.ActivityMainBinding
import com.reasons.bitcoin.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initListener()
        initObservers()
        val alarm = AlarmReceiver()
        alarm.setAlarm(this)
        viewModel.readTarget()
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(
            aLBReceiver,
            IntentFilter(getString(R.string.intent_filter_event))

        )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(aLBReceiver)

    }

    private val aLBReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val item =
                intent?.getParcelableExtra<CoinRate>(getString(R.string.extra_intent_localbroadcast_bitcoin))
                    ?: return
            binding.coinRate = item
        }
    }

    private fun initObservers() {
        viewModel.getTargetRate().observe(this, targetObserver)
        viewModel.coinRate.observe(this, rateChangeObserver)

    }

    private val targetObserver = Observer<TargetToAchieve> {
        if (it == null)
            return@Observer
        binding.target = it


    }
    private val rateChangeObserver = Observer<CoinRate> {
        if (it == null)
            return@Observer
        binding.coinRate = it


    }

    private fun initListener() {
        binding.activityBitcoinMinRateValueET.addTextChangedListener(minRateWatcher)
        binding.activityBitcoinMaxRateValueET.addTextChangedListener(maxRateWatcher)

//        binding.activityBitcoinToggleButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                // The toggle is enabled
//                if (checkIfNotEmpty())
//                    viewModel.insertTarget(
//                        TargetToAchieve(
//                            binding.activityBitcoinMinRateValueET.text.toString(),
//                            binding.activityBitcoinMaxRateValueET.text.toString()
//                        )
//                    )
//            } else {
//                disableToggleState()
//            }
//        }
        binding.activityBitcoinToggleButton.setOnClickListener {
            val isChecked = binding.activityBitcoinToggleButton.isChecked
            if (!isChecked) {
                if (checkIfNotEmpty())
                    viewModel.insertTarget(
                        TargetToAchieve(
                            binding.activityBitcoinMinRateValueET.text.toString(),
                            binding.activityBitcoinMaxRateValueET.text.toString()
                        )
                    )
            } else {
                disableToggleState()

            }
        }


    }

    private val minRateWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            checkIfNotEmpty()
        }
    }


    private fun checkIfNotEmpty(): Boolean {
        val minValue = binding.activityBitcoinMinRateValueET.text
        val maxValue = binding.activityBitcoinMaxRateValueET.text

        if (minValue.isNotEmpty() && maxValue.isNotEmpty()) {
            binding.activityBitcoinToggleButton.isChecked = true
            binding.activityBitcoinToggleButton.isEnabled = true

            return true
        } else if (minValue.isEmpty()) {
            disableToggleState()

            return false

        } else if (maxValue.isEmpty()) {

            disableToggleState()
            return false
        }
        return false

    }

    private fun disableToggleState() {
        binding.activityBitcoinToggleButton.textOff
        binding.activityBitcoinToggleButton.isEnabled = false
    }


    private val maxRateWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            checkIfNotEmpty()
        }
    }
}