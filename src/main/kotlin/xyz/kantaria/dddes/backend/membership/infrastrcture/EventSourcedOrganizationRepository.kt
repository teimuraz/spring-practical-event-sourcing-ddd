package xyz.kantaria.dddes.backend.membership.infrastrcture

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository
import xyz.kantaria.dddes.backend.common.AggregateTypeRegistry
import xyz.kantaria.dddes.backend.membership.domain.*
import xyz.kantaria.dddes.lib.error.InternalErrorException
import xyz.kantaria.dddes.lib.eventsourcing.DomainEvent
import xyz.kantaria.dddes.lib.eventsourcing.EventsJournalRepository
import xyz.kantaria.dddes.lib.eventsourcing.PgEventSourcedRepository

@Repository
class EventSourcedOrganizationRepository @Autowired constructor(
        override val eventsJournalRepository: EventsJournalRepository,
        override val eventPublisher: ApplicationEventPublisher,
        override val objectMapper: ObjectMapper)
    : OrganizationRepository,  PgEventSourcedRepository<Organization>() {

    override val aggregateRootClass = Organization::class

    override val aggregateRootType = AggregateTypeRegistry.MEMBERSHIP_ORGANIZATION

    override fun deserializeEvent(eventType: String, event: String): DomainEvent {
        return when (eventType) {
            "organizationCreated" -> objectMapper.readValue(event, OrganizationCreated::class.java)
            else -> throw InternalErrorException("Unsupported event type $eventType")
        }
    }

    override fun nextId(): Long {
        return System.currentTimeMillis()
    }
}