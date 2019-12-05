package awesome.account.app.accounts.logger

import mu.KotlinLogging

interface Logging {

    @Suppress("unused")
    val logger
        get() = KotlinLogging.logger(this.javaClass.name)
}