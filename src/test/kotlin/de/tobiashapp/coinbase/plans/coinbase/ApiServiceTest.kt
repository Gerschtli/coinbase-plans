package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.orders.OrderService
import de.tobiashapp.coinbase.plans.IntegrationBaseTest
import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal

class ApiServiceTest : IntegrationBaseTest() {
    @Autowired
    lateinit var apiService: ApiService

    @Autowired
    lateinit var orderService: OrderService

    @Test
    fun `buyCrypto creates a market order`() {
        val orderId = apiService.buyCrypto(CryptoCurrency.BTC, FiatCurrency.USD, BigDecimal(10))

        val responseFilled = orderService.getFillByOrderId(orderId, 10)

        assertAll(
            { assertThat(responseFilled).hasSize(1) },
            { assertThat(responseFilled[0].product_id).isEqualTo("BTC-USD") },
            { assertThat(responseFilled[0].settled).isEqualTo(true) },
            { assertThat(responseFilled[0].side).isEqualTo("buy") },
        )
    }
}
