package de.tobiashapp.coinbase.plans.runner

import com.coinbase.exchange.api.exchange.CoinbaseExchangeException
import de.tobiashapp.coinbase.plans.coinbase.ApiService
import de.tobiashapp.coinbase.plans.mail.MailService
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val LOG = KotlinLogging.logger {}

@Service
class PlanRunner(
    private val apiService: ApiService,
    private val mailService: MailService,
) {
    fun run(planExecution: PlanExecution) {
        LOG.info("Execute buy plan for {}-{}", planExecution.cryptoCurrency, planExecution.fiatCurrency)
        try {
            val orderId = apiService.buyCrypto(
                planExecution.cryptoCurrency, planExecution.fiatCurrency, planExecution.amount
            )

            sendMail(planExecution, true, "Order ID: $orderId")
        } catch (exception: CoinbaseExchangeException) {
            LOG.error(
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
                    "Buy plan for ${planExecution.amount} ${planExecution.fiatCurrency} to " +
                    "${planExecution.cryptoCurrency} $statusText!\n\n$additionalInfo",
        )
    }
}
