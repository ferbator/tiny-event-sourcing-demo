package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = UserAggregate::class, subscriberName = "user-demo-subs-stream"
)
class AnnotationBasedUserEventsSubscriber {  //todo remove

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun userRegisteredEventSubscriber(event: UserRegisteredEvent) {
        logger.info("User registered with ID {}: ", event.userId)
    }
}
