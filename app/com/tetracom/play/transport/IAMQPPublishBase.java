package com.tetracom.play.transport;

import java.util.Optional;

import com.tetracom.play.transport.config.IAMQPExchange;
import com.tetracom.play.transport.config.IAMQPQueue;

public interface IAMQPPublishBase {
	Optional<IAMQPQueue> queue();
	IAMQPExchange exchange();
}
