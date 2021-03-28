package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.model.NewMarketOrderSingle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CoinbaseApiSystemTest {
    @Autowired
    lateinit var orderService: OrderService

    @Test
    fun `gdax lib creates market orders`() {
        // create market order
        val newMarketOrderSingle = NewMarketOrderSingle()
        newMarketOrderSingle.side = "buy"
        newMarketOrderSingle.product_id = "BTC-EUR"
        newMarketOrderSingle.funds = "10"
        val responsePostOrders = orderService.createOrder(newMarketOrderSingle)

        assertAll(
            { assertThat(responsePostOrders.type).isEqualTo("market") },
            { assertThat(responsePostOrders.side).isEqualTo("buy") },
            { assertThat(responsePostOrders.product_id).isEqualTo("BTC-EUR") },
        )

        // test filled order
        val responseFilled = orderService.getFillByOrderId(responsePostOrders.id, 10)
        assertThat(responseFilled).hasSize(1)

        // get all open orders
        val responseGetOrders3 = orderService.openOrders
        assertThat(responseGetOrders3).hasSize(0)
    }
}
