package xyz.kantaria.dddes.backend.membership.domain

import org.joda.time.DateTime
import xyz.kantaria.dddes.lib.eventsourcing.AggregateRoot
import xyz.kantaria.dddes.lib.eventsourcing.DomainEvent

class Organization : AggregateRoot {
    override var id: Long = 0
        private set

    var name: String = ""
        private set

    var ownersCount: Int = 0
        private set

    constructor()

    constructor(id: Long, name: String) {
        applyChange(OrganizationCreated(id, name, 0))
    }

    fun increaseOwnersCount() {
        applyChange(OwnersCountIncreased(id, ownersCount + 1))
    }

    fun decreaseOwnersCount() {
        applyChange(OwnersCountDecreased(id, ownersCount - 1))
    }

    override fun applyEvent(event: DomainEvent) {
        when (event) {
            is OrganizationCreated -> {
                id = event.id
                name = event.name
                ownersCount = event.ownersCount
            }

            is OwnersCountIncreased -> {
                ownersCount = event.totalOwnersCount
            }

            is OwnersCountDecreased -> {
                ownersCount = event.totalOwnersCount
            }
        }
    }
}

data class OrganizationCreated(override val id: Long, val name: String, val ownersCount: Int) : DomainEvent
data class OwnersCountIncreased(override val id: Long, val totalOwnersCount: Int) : DomainEvent
data class OwnersCountDecreased(override val id: Long, val totalOwnersCount: Int) : DomainEvent
