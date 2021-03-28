package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.model.NewMarketOrderSingle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.time.Instant

@SpringBootTest
class ApiServiceTest {
    @Autowired
    lateinit var apiService: ApiService

    @Autowired
    lateinit var orderService: OrderService

    @Disabled
    fun `buyCrypto creates a market order`() {
        val orderId = apiService.buyCrypto(CryptoCurrency.BTC, FiatCurrency.EUR, BigDecimal(10))

        val responseFilled = orderService.getFillByOrderId(orderId, 10)

        println(responseFilled[0])
        assertAll(
            { assertThat(responseFilled).hasSize(1) },
            { assertThat(responseFilled[0].price).isEqualTo(10) },
            { assertThat(responseFilled[0].product_id).isEqualTo("BTC-EUR") },
            { assertThat(responseFilled[0].settled).isEqualTo(true) },
            { assertThat(responseFilled[0].side).isEqualTo("buy") },
        )
    }

    @Disabled
    fun `getFills returns filled orders`() {
        val newMarketOrderSingle = NewMarketOrderSingle()
        newMarketOrderSingle.side = "buy"
        newMarketOrderSingle.product_id = "BTC-EUR"
        newMarketOrderSingle.funds = "10"
        orderService.createOrder(newMarketOrderSingle)

        val fills = apiService.getFills(CryptoCurrency.BTC, FiatCurrency.EUR, 20)

        println(fills[0])
        assertAll(
            { assertThat(fills).hasSize(1) },
            { assertThat(fills[0].createdAt).isEqualTo(Instant.now()) },
            { assertThat(fills[0].cryptoCurrency).isEqualTo(CryptoCurrency.BTC) },
            { assertThat(fills[0].fee).isEqualTo(BigDecimal(0.05)) },
            { assertThat(fills[0].fiatCurrency).isEqualTo(FiatCurrency.EUR) },
            { assertThat(fills[0].orderId).isEqualTo("10") },
            { assertThat(fills[0].price).isEqualTo(BigDecimal(10)) },
            { assertThat(fills[0].settled).isEqualTo(true) },
            { assertThat(fills[0].size).isEqualTo(BigDecimal(10)) },
        )
    }
}
