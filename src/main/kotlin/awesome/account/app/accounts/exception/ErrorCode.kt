package awesome.account.app.accounts.exception

enum class ErrorCode(val error: String) {

    ACCOUNT_NOT_FOUND("Account not found"),
    ACCOUNT_NOT_ENOUGH_MONEY("Not enough funds in the account"),
    TRANSFER_SAME_ACCOUNT("Cannot transfer funds from the same account"),
    TRANSFER_AMOUNT_LESS_THAN_ZERO("Transfer amount must be greater than zero");

    override fun toString() = error
}