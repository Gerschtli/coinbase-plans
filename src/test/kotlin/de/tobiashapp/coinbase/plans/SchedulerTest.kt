package de.tobiashapp.coinbase.plans

import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import de.tobiashapp.coinbase.plans.runner.PlanExecution
import de.tobiashapp.coinbase.plans.runner.PlanRunner
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.untilAsserted
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.TestPropertySource
import java.math.BigDecimal
import java.time.Duration

@TestPropertySource(properties = [
    "app.plans[0].amount=20.5",
    "app.plans[0].cron=*/5 * * * * *",
    "app.plans[0].crypto-currency=BTC",
    "app.plans[0].fiat-currency=EUR",
    "app.plans[0].receiver-address=receiver",
])
class SchedulerTest : IntegrationBaseTest() {
    @MockBean
    lateinit var planRunner: PlanRunner

    @Test
    fun `on Scheduler PostConstruct calls PlanRunner run`() {
        val planExecution = PlanExecution(
            BigDecimal(20.5),
            CryptoCurrency.BTC,
            FiatCurrency.EUR,
            "receiver"
        )

        await atMost Duration.ofSeconds(6) untilAsserted {
            verify(planRunner).run(planExecution)
        }
    }
}
