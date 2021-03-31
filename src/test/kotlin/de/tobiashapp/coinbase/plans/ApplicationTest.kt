package de.tobiashapp.coinbase.plans

import com.coinbase.exchange.api.orders.OrderService
import com.icegreen.greenmail.util.GreenMailUtil
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withinPercentage
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.TestPropertySource
import java.math.BigDecimal
import java.time.Duration

@TestPropertySource(properties = [
    "app.plans[0].amount=15",
    "app.plans[0].cron=*/5 * * * * *",
    "app.plans[0].crypto-currency=BTC",
    "app.plans[0].fiat-currency=USD",
    "app.plans[0].receiver-address=receiver@mail.com",
    "app.plans[0].time-zone=Europe/Berlin",
])
class ApplicationTest : IntegrationBaseTest() {
    @Autowired
    lateinit var orderService: OrderService

    @Test
    fun `buys crypto via API and sends success mail`() {
        await atMost Duration.ofSeconds(6) until {
            greenMail.receivedMessages.size == 1
        }

        val receivedMessages = greenMail.receivedMessages
        assertThat(receivedMessages).hasSize(1)

        val message = receivedMessages[0]

        assertAll(
            { assertThat(message.allRecipients).hasSize(1) },
            { assertThat(message.allRecipients[0].toString()).isEqualTo("receiver@mail.com") },
            { assertThat(message.from).hasSize(1) },
            { assertThat(message.from[0].toString()).isEqualTo("sender@mail.com") },
            { assertThat(message.subject).isEqualTo("[Coinbase Plans] 15 USD to BTC succeeded") },
            {
                val regex = Regex(
                    "^Hello there,\r\n\r\nBuy plan for 15 USD to BTC succeeded!\r\n\r\nOrder ID: (\\S+)$"
                )
                val matchResult = regex.matchEntire(GreenMailUtil.getBody(message))
                assertThat(matchResult).isNotNull

                val orderId = matchResult!!.groups[1]?.value
                assertThat(orderId).isNotNull

                val responseFilled = orderService.getFillByOrderId(orderId, 10)

                assertAll(
                    { assertThat(responseFilled).hasSize(1) },
                    { assertThat(responseFilled[0].product_id).isEqualTo("BTC-USD") },
                    { assertThat(responseFilled[0].settled).isEqualTo(true) },
                    { assertThat(responseFilled[0].side).isEqualTo("buy") },
                    {
                        val fiatValue = responseFilled[0].price * responseFilled[0].size + responseFilled[0].fee
                        assertThat(fiatValue).isCloseTo(BigDecimal(15), withinPercentage(0.005))
                    },
                )
            },
        )
    }
}
