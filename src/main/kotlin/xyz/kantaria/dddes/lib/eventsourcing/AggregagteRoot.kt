package xyz.kantaria.dddes.lib.eventsourcing

import com.fasterxml.jackson.annotation.JsonIgnore

abstract class AggregateRoot {
    abstract val id: Long
    var version = 0

    @JsonIgnore
    val uncommittedEvents: MutableList<DomainEvent> = mutableListOf()

    protected abstract fun applyEvent(event: DomainEvent)

    fun applyChange(event: DomainEvent, isNew: Boolean = true) {
//        val cls = this.javaClass
//        for (method in  cls.declaredMethods) {
//            method.
//            if (method.name == "applyEvent") {
//                try {
//                    method.invoke(this, event)
//                } catch (e: Throwable) {
//                    //
//                }
//            }
//        }
        applyEvent(event)
        if (isNew) {
            uncommittedEvents.add(event)
            version++
        }
    }

    fun loadFromHistory(events: List<DomainEvent>, lastVersion: Int) {
        events.forEach { applyChange(it, false) }
        version = lastVersion
    }
}

