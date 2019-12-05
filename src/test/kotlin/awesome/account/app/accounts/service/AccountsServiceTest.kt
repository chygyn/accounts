package awesome.account.app.accounts.service

import awesome.account.app.accounts.exception.ProcessException
import awesome.account.app.accounts.repository.Account
import awesome.account.app.accounts.repository.AccountRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccountsServiceTest {

    private lateinit var account1: Account
    private lateinit var account1Updated: Account
    private lateinit var account2: Account
    private lateinit var account2Updated: Account
    @Mock
    lateinit var accountRepositoryMock: AccountRepository
    private lateinit var accountsServiceMock: AccountServiceImpl

    @BeforeAll
    fun init() {
        MockitoAnnotations.initMocks(this)
        accountsServiceMock = AccountServiceImpl(
            accountRepository = accountRepositoryMock
        )

        generateMockData()
    }

    @Test
    fun `Add money to account`() {
        account1Updated.balance += 1000
        `when`(accountRepositoryMock.findOneByIdForUpdate(1)).thenReturn(Optional.of(account1))
        `when`(accountRepositoryMock.findById(1)).thenReturn(Optional.of(account1Updated))
        `when`(accountRepositoryMock.updateAccountAmount(account1.id, 1100L)).thenReturn(1)
        val result = accountsServiceMock.addMoney(account1.id, 1000L)
        assertThat(result.balance, Is(equalTo(account1Updated.balance)))
    }

    @Test
    fun `Exception assert when try to withdraw money and there is not enough funds`() {
        `when`(accountRepositoryMock.findOneByIdForUpdate(1)).thenReturn(Optional.of(account1))
        Assertions.assertThrows(ProcessException::class.java) {
            accountsServiceMock.addMoney(account1.id, -1000L)
        }
    }

    @Test
    fun `Send money from account1 to account2`() {
        `when`(accountRepositoryMock.findOneByIdForUpdate(1)).thenReturn(Optional.of(account1))
        `when`(accountRepositoryMock.findById(1)).thenReturn(Optional.of(account1Updated.also { it.balance -= 50 }))
        `when`(accountRepositoryMock.findOneByIdForUpdate(2)).thenReturn(Optional.of(account2))
        `when`(accountRepositoryMock.findById(2)).thenReturn(Optional.of(account2Updated.also { it.balance += 50 }))
        `when`(accountRepositoryMock.updateAccountAmount(account1.id, 50)).thenReturn(1)
        `when`(accountRepositoryMock.updateAccountAmount(account2.id, 250)).thenReturn(1)
        val result = accountsServiceMock.sendMoney(account1.id, account2.id, 50)
        assertThat(result.balance, Is(equalTo(account1Updated.balance)))
    }

    @Test
    fun `Exception assert when result sender balance less than 0`() {
        `when`(accountRepositoryMock.findById(1)).thenReturn(Optional.of(account1))
        `when`(accountRepositoryMock.findById(2)).thenReturn(Optional.of(account2))
        Assertions.assertThrows(ProcessException::class.java) {
            accountsServiceMock.sendMoney(account1.id, account2.id, 1000)
        }
    }

    @Test
    fun `Exception assert UpdateBalance cannot update account`() {
        `when`(accountRepositoryMock.findById(1)).thenReturn(Optional.of(account1))
        `when`(accountRepositoryMock.findById(2)).thenReturn(Optional.of(account2))
        Assertions.assertThrows(ProcessException::class.java) {
            accountsServiceMock.sendMoney(account1.id, account2.id, 1000)
        }
    }

    fun generateMockData() {
        account1 = Account(
            id = 1, name = "Vasya", balance = 100
        )
        account1Updated = account1.copy()
        account2 = Account(
            id = 2, name = "Iiiiigor", balance = 200
        )
        account2Updated = account2.copy()
    }
}