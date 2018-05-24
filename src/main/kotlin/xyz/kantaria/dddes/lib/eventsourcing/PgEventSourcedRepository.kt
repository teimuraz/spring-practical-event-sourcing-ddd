package xyz.kantaria.dddes.lib.eventsourcing

import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.time.DateTime
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.CrudRepository
import javax.persistence.*
import kotlin.reflect.full.createInstance

abstract class PgEventSourcedRepository<T : AggregateRoot> : Repository<T> {

    abstract protected val objectMapper: ObjectMapper
    abstract protected val eventPublisher: ApplicationEventPublisher
    abstract protected val eventsJournalRepository: EventsJournalRepository
    abstract protected val aggregateRootType: Int
    abstract protected fun deserializeEvent(eventType: String, event: String): DomainEvent

    override fun save(aggregateRoot: AggregateRoot) {
        aggregateRoot.uncommittedEvents.forEach {
            val model = EventJournalModel(
                    aggregateRootType = aggregateRootType,
                    aggregateRootId = aggregateRoot.id,
                    eventType = it.eventType(),
                    event = objectMapper.writeValueAsString(it),
                    aggregateVersion = aggregateRoot.version
            )
            eventsJournalRepository.save(model)
            eventPublisher.publishEvent(it)
        }
        aggregateRoot.uncommittedEvents.clear()
    }

    override fun getById(id: Long): T? {
        val aggregate = aggregateClass.createInstance()
        val eventModels = eventsJournalRepository.findByAggregateRootTypeAndAggregateRootIdOrderByEventOffset(aggregateRootType, id)
        val events = eventModels.map { model -> deserializeEvent(model.eventType, model.event) }

        if (events.isEmpty()) {
            return null
        }

        aggregate.loadFromHistory(events, eventModels.last().aggregateVersion)

        return aggregate
    }
}

@Entity(name = "events_journal")
data class EventJournalModel(

        @Id
        @Column(name = "event_offset", columnDefinition = "serial")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val eventOffset: Long = 0,

        @Column(name = "aggregate_root_type")
        val aggregateRootType: Int = 0,

        @Column(name = "aggregate_root_id")
        val aggregateRootId: Long = 0,

        @Column(name = "event_type")
        val eventType: String = "",

        val event: String = "",

        @Column(name = "aggregate_version")
        val aggregateVersion: Int = 0,

        @Column(name = "created_at")
        val createdAt: DateTime = DateTime.now()
)

@org.springframework.stereotype.Repository
interface EventsJournalRepository : CrudRepository<EventJournalModel, Long> {
    fun findByAggregateRootTypeAndAggregateRootIdOrderByEventOffset(type: Int, id: Long): List<EventJournalModel>
}