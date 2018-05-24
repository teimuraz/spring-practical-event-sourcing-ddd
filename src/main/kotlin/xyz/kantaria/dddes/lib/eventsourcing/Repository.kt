package xyz.kantaria.dddes.lib.eventsourcing

import kotlin.reflect.KClass

interface Repository<T : AggregateRoot> {
    fun save(aggregateRoot: AggregateRoot)
    fun getById(id: Long): T?
    val aggregateClass: KClass<T>
}