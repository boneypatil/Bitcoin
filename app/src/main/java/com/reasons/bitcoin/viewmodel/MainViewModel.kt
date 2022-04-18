package com.reasons.bitcoin.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.reasons.bitcoin.data.CoinRate
import com.reasons.bitcoin.data.TargetToAchieve
import com.reasons.bitcoin.service.BitcoinRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: BitcoinRepository, application: Application) :
    AndroidViewModel(application) {


    private val targetRate = MutableLiveData<TargetToAchieve>()
    fun getTargetRate(): LiveData<TargetToAchieve> = targetRate


    fun insertTarget(target: TargetToAchieve) = viewModelScope.launch {
        repository.deleteTarget()
        repository.insertTarget(target)
    }

    val coinRate: LiveData<CoinRate> = repository.getCoinRate().asLiveData()

    fun readTarget() {
        viewModelScope.launch {
            targetRate.value = repository.getTarget()
        }
    }

}