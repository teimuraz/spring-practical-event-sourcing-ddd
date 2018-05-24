package xyz.kantaria.dddes.lib.eventsourcing

interface Repository<T : AggregateRoot> {
    fun save(aggregateRoot: AggregateRoot)
    fun getById(id: Long): T?
    val initialState: T
}