package ru.quipy.projections

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.quipy.api.*
import ru.quipy.streams.annotation.AggregateSubscriber
import ru.quipy.streams.annotation.SubscribeEvent

@Service
@AggregateSubscriber(
    aggregateClass = TaskAggregate::class, subscriberName = "task-demo-subs-stream"
)
class AnnotationBasedTaskEventsSubscriber {

    val logger: Logger = LoggerFactory.getLogger(AnnotationBasedProjectEventsSubscriber::class.java)

    @SubscribeEvent
    fun taskCreatedEventSubscriber(event: TaskCreatedEvent) {
        logger.info("Task created with ID {}: ", event.taskId)
    }

    @SubscribeEvent
    fun taskRenamedEventSubscriber(event: TaskRenamedEvent) {
        logger.info("Task {} changed title to {}: ", event.taskId, event.newTitle)
    }

    @SubscribeEvent
    fun taskAssignedToUserEventSubscriber(event: TaskAssignedToUserEvent) {
        logger.info("Task {} assigned to user {}: ", event.taskId, event.userId)
    }

    @SubscribeEvent
    fun statusAssignedToTaskEventSubscriber(event: StatusAssignedToTaskEvent) {
        logger.info("Status {} assigned to task {}: ", event.statusId, event.taskId)
    }

    @SubscribeEvent
    fun taskStatusChangedEventSubscriber(event: TaskStatusChangedEvent) {
        logger.info("Task {} changed status to {}: ", event.taskId, event.newStatusId)
    }
}
