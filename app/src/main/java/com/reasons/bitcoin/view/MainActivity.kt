package com.reasons.bitcoin.view


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
import com.reasons.bitcoin.R
import com.reasons.bitcoin.broadcasts.AlarmReceiver
import com.reasons.bitcoin.data.CoinRate
import com.reasons.bitcoin.data.TargetToAchieve
import com.reasons.bitcoin.databinding.ActivityMainBinding
import com.reasons.bitcoin.viewmodel.MainViewModel
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by inject()
    private var processed = false
    private var saveState = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initListener()
        initObservers()
        val alarm = AlarmReceiver()
        alarm.setAlarm(this)
        viewModel.readTarget()
        binding.activityBitcoinToggleButton.isEnabled = false


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
        if (checkIfNotEmpty())
            setSaveView()

    }
    private val rateChangeObserver = Observer<CoinRate> {
        if (it == null)
            return@Observer
        binding.coinRate = it


    }

    private fun initListener() {
        binding.activityBitcoinMinRateValueET.addTextChangedListener(minRateWatcher)
        binding.activityBitcoinMaxRateValueET.addTextChangedListener(maxRateWatcher)

        binding.activityBitcoinToggleButton.setOnClickListener {
            if (processed) {
                if (checkIfNotEmpty())
                if (saveState) {
                    saveTarget()
                    setSaveView()
                } else {
                    editView()
                }
            } else {
                disableToggleState()
            }
        }

    }

    private fun setSaveView() {
        binding.activityBitcoinToggleButton.isChecked = false
        binding.activityBitcoinMinRateValueET.isEnabled = false
        binding.activityBitcoinMaxRateValueET.isEnabled = false
        binding.activityBitcoinMinRateValueET.isClickable = false
        binding.activityBitcoinMaxRateValueET.isClickable = false
        saveState = false
    }

    private fun editView() {

        binding.activityBitcoinMinRateValueET.isClickable = true
        binding.activityBitcoinMaxRateValueET.isClickable = true
        binding.activityBitcoinMinRateValueET.isEnabled = true
        binding.activityBitcoinMaxRateValueET.isEnabled = true
        saveState = true
    }

    private fun saveTarget() {
        viewModel.insertTarget(
            TargetToAchieve(
                binding.activityBitcoinMinRateValueET.text.toString(),
                binding.activityBitcoinMaxRateValueET.text.toString()
            )
        )
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
            processedToSaveTarget()

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

    private fun processedToSaveTarget() {
        binding.activityBitcoinToggleButton.isChecked = true
        binding.activityBitcoinToggleButton.isEnabled = true
        processed = true
    }

    private fun disableToggleState() {
        binding.activityBitcoinToggleButton.textOff
        binding.activityBitcoinToggleButton.isEnabled = false
        processed = false
    }


    private val maxRateWatcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            checkIfNotEmpty()
        }
    }
}