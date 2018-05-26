package xyz.kantaria.dddes.lib.eventsourcing

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaModule
import org.joda.time.DateTime
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.CrudRepository
import javax.persistence.*
import kotlin.reflect.full.createInstance

abstract class PgEventSourcedRepository<T : AggregateRoot> : Repository<T> {

    protected abstract val objectMapper: ObjectMapper
    protected abstract val eventPublisher: ApplicationEventPublisher
    protected abstract val eventsJournalRepository: EventsJournalRepository
    protected abstract val aggregateRootType: Int
    protected abstract fun deserializeEvent(eventType: String, event: String): DomainEvent

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

    override fun findById(id: Long): T? {
        val aggregate = aggregateRootClass.createInstance()
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