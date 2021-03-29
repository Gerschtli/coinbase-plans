package de.tobiashapp.coinbase.plans.coinbase

import com.coinbase.exchange.api.exchange.CoinbaseExchangeException
import com.coinbase.exchange.api.orders.OrderService
import com.coinbase.exchange.model.NewMarketOrderSingle
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ApiService(val orderService: OrderService) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(ApiService::class.java)
    }

    @Throws(CoinbaseExchangeException::class)
    fun buyCrypto(cryptoCurrency: CryptoCurrency, fiatCurrency: FiatCurrency, amount: BigDecimal): String {
        log.info("Create market order: buy {} for {} {}", cryptoCurrency, amount, fiatCurrency)

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
