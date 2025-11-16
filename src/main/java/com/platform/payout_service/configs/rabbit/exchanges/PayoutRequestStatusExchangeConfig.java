package com.platform.payout_service.configs.rabbit.exchanges;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
/**
 * Configuration class for the Payout Request Status Exchange.
 */
@Configuration
public class PayoutRequestStatusExchangeConfig extends ExchangeAbstractConfig {
    /**
     * Constructs an ExchangeAbstractConfig with the specified exchange name.
     *
     * @param exchangeNameParam the name of the exchange
     */
    public PayoutRequestStatusExchangeConfig(@Value("${queue.payout-status.exchange}") String exchangeNameParam) {
        super(exchangeNameParam);
    }
}
