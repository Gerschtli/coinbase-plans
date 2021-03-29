package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.orders.OrderService
import de.tobiashapp.coinbase.plans.config.CoinbaseConfig
import de.tobiashapp.coinbase.plans.config.CoinbaseProperties
import de.tobiashapp.coinbase.plans.config.JacksonConfig
import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.ContextConfiguration
import java.math.BigDecimal

@JsonTest
@EnableConfigurationProperties(value = [CoinbaseProperties::class])
@ContextConfiguration(classes = [CoinbaseConfig::class, JacksonConfig::class, ApiService::class])
class ApiServiceTest {
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
