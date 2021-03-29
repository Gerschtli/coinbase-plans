package de.tobiashapp.coinbase.plans.runner

import com.coinbase.exchange.api.exchange.CoinbaseExchangeException
import de.tobiashapp.coinbase.plans.coinbase.ApiService
import de.tobiashapp.coinbase.plans.mail.MailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlanRunner(
    private val apiService: ApiService,
    private val mailService: MailService,
) {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(PlanRunner::class.java)
    }

    fun run(planExecution: PlanExecution) {
        log.info("Execute buy plan for {}-{}", planExecution.cryptoCurrency, planExecution.fiatCurrency)
        try {
            val orderId = apiService.buyCrypto(
                planExecution.cryptoCurrency, planExecution.fiatCurrency, planExecution.amount
            )

            sendMail(planExecution, true, "Order ID: $orderId")
        } catch (exception: CoinbaseExchangeException) {
            log.error(
                "Execute buy plan for {}-{} failed: {}",
                planExecution.cryptoCurrency,
                planExecution.fiatCurrency,
                exception.message,
                exception
            )

            sendMail(planExecution, false, "Error message: ${exception.message}")
        }
    }

    private fun sendMail(
        planExecution: PlanExecution,
        success: Boolean,
        additionalInfo: String,
    ) {
        val statusText = if (success) "succeeded" else "failed"

        mailService.sendMessage(
            planExecution.receiverAddress,
            "[Coinbase Plans] ${planExecution.amount} ${planExecution.fiatCurrency} to " +
                    "${planExecution.cryptoCurrency} $statusText",
            "Hello there,\n\n" +
                    "${planExecution.amount} ${planExecution.fiatCurrency} to ${planExecution.cryptoCurrency} " +
                    "$statusText!\n\n$additionalInfo",
        )
    }
}
