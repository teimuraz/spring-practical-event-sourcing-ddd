package xyz.kantaria.dddes.lib.eventsourcing

data class EventLogDescriptor(val aggregateType: String, val event: DomainEvent)

//abstract class EventSourcedRepository<T : AggregateRoot> : Repository<T> {
//
//    private val eventLog = mutableListOf<EventLogDescriptor>()
//
//    override fun save(aggregateRoot: AggregateRoot, expectedVersion: Int) {
//        aggregateRoot.uncommittedEvents.forEach {
//            val descriptor = EventLogDescriptor(aggregateRoot.aggregateType, it)
//            eventLog.add(descriptor)
//        }
//
//        println(eventLog)
//    }
//
//    override fun getById(id: Long): T? {
//        val aggregate = initialState
//
//        val events = eventLog
//                .filter { it.aggregateType == aggregate.aggregateType && it.event.id == id}
//                .map { it.event }
//
//
//        return if (events.isEmpty()) {
//            null
//        } else {
//            aggregate.loadFromHistory(events)
//            aggregate
//        }
//    }
//
//}