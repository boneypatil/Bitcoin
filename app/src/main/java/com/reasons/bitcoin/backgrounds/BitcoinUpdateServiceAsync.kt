package com.reasons.bitcoin.backgrounds

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.reasons.bitcoin.R
import com.reasons.bitcoin.data.*
import com.reasons.bitcoin.service.BitconService
import com.reasons.bitcoin.utils.getBitcoinService
import com.reasons.bitcoin.utils.notificationForNewVersionDialog
import com.reasons.bitcoin.utils.notificationForOldVersionDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException


class BitcoinUpdateServiceAsync : Service() {
    private lateinit var bitcoinService: BitconService
    private lateinit var coinRateDao: CoinRateDao
    private lateinit var targetDao: TargetDao

    /**
     * This method run only one time. At the first time of service created and running
     */
    override fun onCreate() {
        bitcoinService = getBitcoinService()
        launchForegroundNotification()
        coinRateDao = BitCoinDb.getDatabase(applicationContext).coinRate()
        targetDao = BitCoinDb.getDatabase(applicationContext).target()
        Log.d("BitcoinUpdateService", "onCreate()  created")
    }

    private fun launchForegroundNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.notificationForNewVersionDialog(false)
        } else {
            this.notificationForOldVersionDialog(false)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("BitcoinUpdateService", "onStartCommand() After service created")
        try {
            CoroutineScope(Dispatchers.IO).launch {
                // make network call
                val response = bitcoinService.fetchExploreDetails().await()
                handleResponse(response)
            }
        } catch (e: HttpException) {
            logIt("HTTPException code: ${e.code()}\nmessage: ${e.message()}\netc: $e")
            e.printStackTrace()
        } catch (e: Exception) {
            logIt("Exception occurred while trying to save ${e.printStackTrace()}")
            e.printStackTrace()
        }
        return START_STICKY
    }

    private suspend fun handleResponse(response: CoinRate) {
        coinRateDao.deleteAll()
        coinRateDao.insert(response)
        broadcastMessage(response)
        checkIfTargetReached(response, targetDao.getTarget() )
    }

    private fun checkIfTargetReached(response: CoinRate, target: TargetToAchieve?) {
        if(target==null)
            return
        val usaRate = response.bpi?.uSD?.rate_float
        val minRate = target.minRate.toDouble()
        val maxRate = target.maxRate.toDouble()

        if (usaRate != null) {
            if (usaRate in minRate..maxRate) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationForNewVersionDialog(true)
                } else {
                    notificationForOldVersionDialog(true)
                }
            }
        }
    }


    private fun broadcastMessage(response: CoinRate) {
        val intent = Intent(getString(R.string.intent_filter_event))
        intent.putExtra(getString(R.string.extra_intent_localbroadcast_bitcoin), response)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }


    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding
        return null
    }

    private fun logIt(s: String) {
        Log.d("BitcoinUpdateService", s)
    }
}