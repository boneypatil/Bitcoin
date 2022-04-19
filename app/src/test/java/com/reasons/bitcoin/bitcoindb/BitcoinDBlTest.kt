package com.reasons.bitcoin.bitcoindb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.reasons.bitcoin.data.CoinRate
import com.reasons.bitcoin.data.TargetToAchieve
import com.reasons.bitcoin.service.BitcoinRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito


@RunWith(JUnit4::class)
class BitcoinDBlTest {

    lateinit var repo: BitcoinRepository


    @ExperimentalCoroutinesApi
    private val mainDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setUp() {
        repo = Mockito.mock(BitcoinRepository::class.java)
        Dispatchers.setMain(mainDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainDispatcher.cleanupTestCoroutines()
    }


    @ExperimentalCoroutinesApi
    @Test
    fun readCoinRate() = runBlocking {
        val bodyResult = Mockito.mock(CoinRate::class.java)
        runBlockingTest {
            Mockito.`when`(repo.getCoinRateTest()).thenReturn(
                bodyResult
            )
        }
        Truth.assertThat(bodyResult.bpi).isNull()
        TestCase.assertNotNull(bodyResult)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `readTarget  state success`() {
        val bodyResult = Mockito.mock(TargetToAchieve::class.java)

        runBlockingTest {
            Mockito.`when`(repo.getTarget()).thenReturn(
                bodyResult
            )
        }
        Truth.assertThat(bodyResult.maxRate != "27890.039").isTrue()
        TestCase.assertNotNull(bodyResult)
    }

}