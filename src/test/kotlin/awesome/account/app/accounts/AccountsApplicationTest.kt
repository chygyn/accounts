package awesome.account.app.accounts

import awesome.account.app.accounts.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort

@SpringBootTest(classes = [AccountsApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountsApplicationTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate
    @LocalServerPort
    val port: Int = 0
    @Autowired
    lateinit var accountRepository: AccountRepository

}
