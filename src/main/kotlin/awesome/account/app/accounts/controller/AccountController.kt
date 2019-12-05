package awesome.account.app.accounts.controller

import awesome.account.app.accounts.service.AccountService
import org.springframework.web.bind.annotation.*
import kotlin.math.abs

@RestController
@RequestMapping("accounts")
class AccountController(
    private val accountService: AccountService
) {

    @PutMapping("/transfer")
    fun transfer(@RequestBody request: TransferRequest) = accountService.sendMoney(
        senderId = request.senderId, receiverId = request.receiverId, amount = abs(request.amount)
    )

    @PutMapping("/replenishment/{accountId}/{amount}")
    fun replenishment(@PathVariable("accountId") accountId: Long, @PathVariable("amount") amount: Long) =
        accountService.addMoney(
            accountId = accountId, amount = abs(amount)
        )

    @PutMapping("/withdrawal/{accountId}/{amount}")
    fun withdrawal(@PathVariable("accountId") accountId: Long, @PathVariable("amount") amount: Long) =
        accountService.addMoney(
            accountId = accountId, amount = -abs(amount)
        )
}