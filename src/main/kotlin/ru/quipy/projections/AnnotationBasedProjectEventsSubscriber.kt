package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = ProjectAggregate::class, subscriberName = "demo-subs-stream"
)
class AnnotationBasedProjectEventsSubscriber { //todo remove

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun projectCreatedEventSubscriber(event: ProjectCreatedEvent) {
        logger.info("Project created with ID {}: ", event.projectId)
    }

    @SubscribeEvent
    fun statusCreatedEventSubscriber(event: StatusCreatedEvent) {
        logger.info("Status created with ID {}: ", event.statusId)
    }

    @SubscribeEvent
    fun participantAddedToProjectEventSubscriber(event: ParticipantAddedToProjectEvent) {
        logger.info("Participant {} added to project {}: ", event.userId, event.projectId)
    }

    @SubscribeEvent
    fun statusDeletedEventSubscriber(event: StatusDeletedEvent) {
        logger.info("Status deleted with ID {}: ", event.statusId)
    }
}