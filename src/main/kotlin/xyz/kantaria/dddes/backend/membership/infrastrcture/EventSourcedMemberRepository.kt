package xyz.kantaria.dddes.backend.membership.infrastrcture

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Repository
import xyz.kantaria.dddes.backend.common.AggregateTypeRegistry
import xyz.kantaria.dddes.lib.error.InternalErrorException
import xyz.kantaria.dddes.lib.eventsourcing.DomainEvent
import xyz.kantaria.dddes.lib.eventsourcing.EventsJournalRepository
import xyz.kantaria.dddes.lib.eventsourcing.PgEventSourcedRepository
import xyz.kantaria.dddes.backend.membership.domain.Member
import xyz.kantaria.dddes.backend.membership.domain.MemberCreated
import xyz.kantaria.dddes.backend.membership.domain.MemberRepository

@Repository
class EventSourcedMemberRepository @Autowired constructor(
        override val eventsJournalRepository: EventsJournalRepository,
        override val eventPublisher: ApplicationEventPublisher,
        override val objectMapper: ObjectMapper)
    : MemberRepository,  PgEventSourcedRepository<Member>() {

    override val aggregateClass = Member::class

    override val aggregateRootType = AggregateTypeRegistry.MEMBERSHIP_MEMBER

    override fun deserializeEvent(eventType: String, event: String): DomainEvent {
        return when (eventType) {
            "memberCreated" -> objectMapper.readValue(event, MemberCreated::class.java)
            else -> throw InternalErrorException("Unsupported event type $eventType")
        }
    }

    override fun nextId(): Long {
        return System.currentTimeMillis()
    }
}