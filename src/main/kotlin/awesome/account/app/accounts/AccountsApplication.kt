package awesome.account.app.accounts

import awesome.account.app.accounts.repository.Account
import awesome.account.app.accounts.repository.AccountRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class AccountsApplication {

    @Bean
    fun data(accountRepository: AccountRepository) = ApplicationRunner {
        accountRepository.saveAll(
            listOf(
                Account(
                    name = "Vasya", balance = 10000
                ), Account(
                    name = "Iiiigor", balance = 5000
                )
            )
        )
    }
}

fun main(args: Array<String>) {
    runApplication<AccountsApplication>(*args)
}
