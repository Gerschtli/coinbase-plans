package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.model.NewMarketOrderSingle
import de.tobiashapp.coinbase.plans.IntegrationBaseTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withinPercentage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class CoinbaseApiSystemTest : IntegrationBaseTest() {
    @Autowired
    lateinit var orderService: OrderService

    @Test
    fun `gdax lib creates market orders`() {
        // create market order
        val newMarketOrderSingle = NewMarketOrderSingle()
        newMarketOrderSingle.side = "buy"
        newMarketOrderSingle.product_id = "BTC-USD"
        newMarketOrderSingle.funds = "10"
        val responsePostOrders = orderService.createOrder(newMarketOrderSingle)

        assertAll(
            { assertThat(responsePostOrders.type).isEqualTo("market") },
            { assertThat(responsePostOrders.side).isEqualTo("buy") },
            { assertThat(responsePostOrders.product_id).isEqualTo("BTC-USD") },
        )

        // test filled order
        val responseFilled = orderService.getFillByOrderId(responsePostOrders.id, 10)
        assertAll(
            { assertThat(responseFilled).hasSize(1) },
            { assertThat(responseFilled[0].product_id).isEqualTo("BTC-USD") },
            { assertThat(responseFilled[0].settled).isEqualTo(true) },
            { assertThat(responseFilled[0].side).isEqualTo("buy") },
            {
                val fiatValue = responseFilled[0].price * responseFilled[0].size + responseFilled[0].fee
                assertThat(fiatValue).isCloseTo(BigDecimal(10), withinPercentage(0.005))
            },
        )

        // get all open orders
        val responseGetOrders3 = orderService.openOrders
        assertThat(responseGetOrders3).hasSize(0)
    }
}
