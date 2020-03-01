package com.vesna.roundup.domain.usecase

import com.vesna.roundup.data.network.Api
import com.vesna.roundup.domain.model.Account
import com.vesna.roundup.domain.model.Period
import com.vesna.roundup.domain.model.Transaction
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.reactivex.Single
import org.joda.time.DateTime
import org.junit.Before
import org.junit.Test

class CalculateRoundUpTest {

    @MockK
    lateinit var api: Api
    @MockK
    lateinit var getAccount: GetAccount

    @InjectMockKs
    lateinit var sut: CalculateRoundUp

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    @Test
    fun testOne() {
        val transaction = Transaction(3.5, Transaction.Direction.OUT)
        val account = mockk<Account>()
        every { api.getTransactions(any(), any(), any()) } returns Single.just(listOf(transaction))
        every { getAccount.execute() } returns Single.just(account)
        sut.execute(Period(DateTime(), DateTime())).test().assertValue(0.5)
    }

    @Test
    fun testTwo() {
        val transaction1 = Transaction(3.5, Transaction.Direction.OUT)
        val transaction2 = Transaction(0.7, Transaction.Direction.OUT)
        val account = mockk<Account>()
        every { api.getTransactions(any(), any(), any()) } returns Single.just(listOf(transaction1, transaction2))
        every { getAccount.execute() } returns Single.just(account)
        sut.execute(Period(DateTime(), DateTime())).test().assertValue(0.8)
    }
}