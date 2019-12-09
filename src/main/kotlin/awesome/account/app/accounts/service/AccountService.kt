package awesome.account.app.accounts.service

import awesome.account.app.accounts.exception.ErrorCode
import awesome.account.app.accounts.exception.ProcessException
import awesome.account.app.accounts.logger.Logging
import awesome.account.app.accounts.repository.Account
import awesome.account.app.accounts.repository.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AccountService {
    fun addMoney(accountId: Long, amount: Long): Account
    fun sendMoney(senderId: Long, receiverId: Long, amount: Long): Account
}

@Service
class AccountServiceImpl(
    val accountRepository: AccountRepository
) : AccountService, Logging {

    @Transactional
    override fun addMoney(accountId: Long, amount: Long): Account {
        logger.info { "Add amount $amount to the account with ID $accountId" }
        val account = findAccountWithLock(accountId)
        updateBalance(amount, account)
        return findAccount(accountId)
    }

    @Transactional
    override fun sendMoney(senderId: Long, receiverId: Long, amount: Long): Account {
        if (Objects.equals(receiverId,senderId)) {
            logger.error { "Cannot transfer funds from the same account" }
            throw ProcessException(fault = ErrorCode.TRANSFER_SAME_ACCOUNT.error)
        }
        logger.info { "Send amount $amount from account $senderId to $receiverId" }
        checkAmount(amount)
        var sender = findAccountWithLock(senderId)
        var receiver = findAccountWithLock(receiverId)
        sender = updateBalance(-amount, sender)
        updateBalance(amount, receiver)
        return sender
    }

    private fun findAccount(accountId: Long): Account {
        val accountOptional = accountRepository.findById(accountId)
        return if (!accountOptional.isPresent) {
            logger.error { "Account with ID $accountId does not exists" }
            throw ProcessException(fault = ErrorCode.ACCOUNT_NOT_FOUND.error)
        } else (accountOptional.get())
    }

    private fun findAccountWithLock(accountId: Long): Account {
        val accountOptional = accountRepository.findOneByIdForUpdate(accountId)
        return if (!accountOptional.isPresent) {
            logger.error { "Account with ID $accountId does not exists" }
            throw ProcessException(fault = ErrorCode.ACCOUNT_NOT_FOUND.error)
        } else (accountOptional.get())
    }

    private fun updateBalance(amount: Long, account: Account): Account {
        val newBalance = account.balance + amount
        if (newBalance < 0) {
            logger.error { "New balance for account ${account.id} less than 0" }
            throw ProcessException(
                fault = ErrorCode.ACCOUNT_NOT_ENOUGH_MONEY.error
            )
        } else {
            val updatedAccountRows = accountRepository.updateAccountAmount(account.id, newBalance)
            if (updatedAccountRows < 1) {
                logger.error { "Cannot update because account with ID ${account.id} does not exists" }
                throw ProcessException(fault = ErrorCode.ACCOUNT_NOT_FOUND.error)
            }
            return findAccount(account.id)
        }
    }

    private fun checkAmount(amount: Long) {
        if (amount <= 0) {
            logger.error { "Transfer amount less than 0" }
            throw ProcessException(fault = ErrorCode.TRANSFER_AMOUNT_LESS_THAN_ZERO.error)
        }
    }
}