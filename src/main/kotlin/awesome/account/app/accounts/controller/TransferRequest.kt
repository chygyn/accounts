package awesome.account.app.accounts.controller

data class TransferRequest(
    val senderId: Long,
    val receiverId: Long,
    val amount: Long
)