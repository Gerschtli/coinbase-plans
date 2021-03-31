package de.tobiashapp.coinbase.plans.runner

import com.coinbase.exchange.api.exchange.CoinbaseExchangeException
import de.tobiashapp.coinbase.plans.coinbase.ApiService
import de.tobiashapp.coinbase.plans.mail.MailService
import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import org.mockito.kotlin.whenever
import java.math.BigDecimal

class PlanRunnerTest {
    private val apiService: ApiService = mock()
    private val mailService: MailService = mock()

    private val planRunner = PlanRunner(apiService, mailService)

    @Test
    fun `run calls buyCrypto`() {
        planRunner.run(PlanExecution(BigDecimal(10), CryptoCurrency.BTC, FiatCurrency.EUR, "receiver"))

        verify(apiService).buyCrypto(CryptoCurrency.BTC, FiatCurrency.EUR, BigDecimal(10))
        verifyNoMoreInteractions(apiService)
    }

    @Test
    fun `run calls sendMessage with order id when buyCrypto succeeds`() {
        whenever(apiService.buyCrypto(any(), any(), any())).thenReturn("123-abc")

        planRunner.run(PlanExecution(BigDecimal(10), CryptoCurrency.BTC, FiatCurrency.EUR, "receiver"))

        verify(mailService).sendMessage(
            "receiver",
            "[Coinbase Plans] 10 EUR to BTC succeeded",
            "Hello there,\n\n" +
                    "Buy plan for 10 EUR to BTC succeeded!\n\n" +
                    "Order ID: 123-abc"
        )
        verifyNoMoreInteractions(mailService)
    }

    @Test
    fun `run calls sendMessage with error message when buyCrypto throws exception`() {
        whenever(apiService.buyCrypto(any(), any(), any())).thenThrow(CoinbaseExchangeException("message", null))

        planRunner.run(PlanExecution(BigDecimal(10), CryptoCurrency.BTC, FiatCurrency.EUR, "receiver"))

        verify(mailService).sendMessage(
            "receiver",
            "[Coinbase Plans] 10 EUR to BTC failed",
            "Hello there,\n\n" +
                    "Buy plan for 10 EUR to BTC failed!\n\n" +
                    "Error message: message"
        )
        verifyNoMoreInteractions(mailService)
    }
}
