package awesome.account.app.accounts.controller

import awesome.account.app.accounts.AccountsApplicationTest
import awesome.account.app.accounts.repository.Account
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod

class AccountControllerTest : AccountsApplicationTest() {

    @Test
    fun `Verify replenish`() {
        val oldBalance = accountRepository.findById(1).get().balance
        val amount = 1000L
        testRestTemplate.exchange(
            "http://localhost:$port/accounts/replenishment/1/$amount",
            HttpMethod.PUT,
            null,
            Account::class.java
        )
        val updatedBalance = accountRepository.findById(1).get().balance
        assertThat(updatedBalance, Is(equalTo(oldBalance + amount)))
    }

    @Test
    fun `Verify transfer`() {
        val oldBalanceSender = accountRepository.findById(1).get().balance
        val oldBalanceReceiver = accountRepository.findById(2).get().balance
        val amount = 1000L
        testRestTemplate.exchange(
            "http://localhost:$port/accounts/transfer",
            HttpMethod.PUT,
            HttpEntity(
                TransferRequest(
                    senderId = 1, receiverId = 2, amount = amount
                )
            ),
            Account::class.java
        )
        val updatedBalanceSender = accountRepository.findById(1).get().balance
        val updatedBalanceReceiver = accountRepository.findById(2).get().balance
        assertThat(updatedBalanceSender, Is(equalTo(oldBalanceSender - amount)))
        assertThat(updatedBalanceReceiver, Is(equalTo(oldBalanceReceiver + amount)))
    }

    @Test
    fun `Verify withdrawal`() {
        val oldBalance = accountRepository.findById(1).get().balance
        val amount = 1000L
        testRestTemplate.exchange(
            "http://localhost:$port/accounts/withdrawal/1/$amount",
            HttpMethod.PUT,
            null,
            Account::class.java
        )
        val updatedBalance = accountRepository.findById(1).get().balance
        assertThat(updatedBalance, Is(equalTo(oldBalance - amount)))
    }

    @Test
    fun `Verify many requests`() {
        val oldBalance = accountRepository.findById(1).get().balance
        val amount = 1L
        val numberOfRequests = 1000
        for (i in 1..numberOfRequests) {
            testRestTemplate.exchange(
                "http://localhost:$port/accounts/withdrawal/1/$amount",
                HttpMethod.PUT,
                null,
                Account::class.java
            )
        }
        val updatedBalance = accountRepository.findById(1).get().balance
        assertThat(updatedBalance, Is(equalTo(oldBalance - amount * numberOfRequests)))
    }

    @Test
    fun `Verify many parallel requests`() {
        val oldBalance = accountRepository.findById(1).get().balance
        val amount = 1L
        val numberOfRequests = 1000
        var sum = emptyList<Account?>()
        val deferredPull = (1..1000).map {
            GlobalScope.async {
                delay(1000)
                testRestTemplate.exchange(
                    "http://localhost:$port/accounts/withdrawal/1/$amount",
                    HttpMethod.PUT,
                    null,
                    Account::class.java
                )
            }
        }
        runBlocking {
            sum = deferredPull.map { it.await().body }
        }
        val updatedBalance = accountRepository.findById(1).get().balance
        assertThat(sum.size, Is(equalTo(1000)))
        assertThat(updatedBalance, Is(equalTo(oldBalance - amount * numberOfRequests)))
    }
}