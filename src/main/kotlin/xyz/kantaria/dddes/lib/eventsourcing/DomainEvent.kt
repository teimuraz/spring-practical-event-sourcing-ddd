package xyz.kantaria.dddes.lib.eventsourcing

interface DomainEvent {
    // Aggregate id
    val id: Long
    fun eventType(): String = this.javaClass.simpleName.decapitalize()
}