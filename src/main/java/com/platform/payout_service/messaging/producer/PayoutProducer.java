package com.platform.payout_service.messaging.producer;

import com.platform.payout_service.configs.rabbit.exchanges.ExchangeAbstractConfig;
import com.platform.payout_service.dtos.payout.PayoutMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.stereotype.Component;
/**
 * Producer component for publishing payout events to RabbitMQ.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PayoutProducer {
    /** RabbitTemplate for sending messages to RabbitMQ. */
    private final RabbitTemplate rabbitTemplate;
    /** Configuration for the payout exchange. */
    private final ExchangeAbstractConfig payoutRequestStatusExchangeConfig;

    /**
     * Publishes a payout event to the RabbitMQ exchange.
     *
     * @param messageDto the payout message DTO
     *                               containing payout details.
     */
    public void publishPayoutEvent(PayoutMessageDto messageDto) {
        try {
            log.debug("Entering publishPayoutEvent with payout : {}", messageDto);
            rabbitTemplate.convertAndSend(
                    payoutRequestStatusExchangeConfig.getExchangeName(), "", messageDto);
            log.debug("Payout event published successfully");
        } catch (AmqpConnectException e) {
            log.error("❌ Failed to connect to RabbitMQ. The broker might be down.", e);
        } catch (MessageConversionException e) {
            log.error("❌ Error serializing campaign message: {}", messageDto, e);
        } catch (AmqpException e) {
            log.error("❌ General error publishing message to RabbitMQ.", e);
        }
    }
}
