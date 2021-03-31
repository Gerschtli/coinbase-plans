package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.exchange.CoinbaseExchangeException
import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.model.NewMarketOrderSingle
import de.tobiashapp.coinbase.plans.models.CryptoCurrency
import de.tobiashapp.coinbase.plans.models.FiatCurrency
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.math.BigDecimal

private val LOG = KotlinLogging.logger {}

@Service
class ApiService(private val orderService: OrderService) {
    @Throws(CoinbaseExchangeException::class)
    fun buyCrypto(cryptoCurrency: CryptoCurrency, fiatCurrency: FiatCurrency, amount: BigDecimal): String {
        LOG.info("Create market order: buy {} for {} {}", cryptoCurrency, amount, fiatCurrency)

        val newMarketOrderSingle = NewMarketOrderSingle()
        newMarketOrderSingle.side = "buy"
        newMarketOrderSingle.product_id = buildProductId(cryptoCurrency, fiatCurrency)
        newMarketOrderSingle.funds = amount.toString()

        val response = orderService.createOrder(newMarketOrderSingle)

        return response.id
    }

    private fun buildProductId(cryptoCurrency: CryptoCurrency, fiatCurrency: FiatCurrency) =
        "${cryptoCurrency.name}-${fiatCurrency.name}"
}
