package xyz.kantaria.dddes.lib.eventsourcing

import kotlin.reflect.KClass

interface Repository<T : AggregateRoot> {
    fun save(aggregateRoot: AggregateRoot)
    fun findById(id: Long): T?
    fun nextId(): Long
    val aggregateRootClass: KClass<T>
}