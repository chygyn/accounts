package awesome.account.app.accounts.exception

class ProcessException(
    var fault: String? = null,
    override val cause: Throwable = IllegalArgumentException()
) : RuntimeException()