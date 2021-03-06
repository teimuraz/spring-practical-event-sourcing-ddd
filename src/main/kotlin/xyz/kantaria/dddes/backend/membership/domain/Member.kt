package xyz.kantaria.dddes.backend.membership.domain

import org.joda.time.DateTime
import xyz.kantaria.dddes.lib.error.ForbiddenException
import xyz.kantaria.dddes.lib.error.ValidationException
import xyz.kantaria.dddes.lib.eventsourcing.AggregateRoot
import xyz.kantaria.dddes.lib.eventsourcing.DomainEvent

class Member : AggregateRoot {
    override var id: Long = 0
        private set

    var name: String = ""
        private set

    var email: String = ""
        private set

    var role: MemberRole = MemberRole.STANDARD_MEMBER
        private set

    var organizationId: Long = 0
        private set

    var becameMemberAt: DateTime = DateTime.now()
        private set

    constructor()

    constructor(id: Long, name: String, email: String, role: MemberRole, organizationId: Long, becameMemberAt: DateTime) {
        applyChange(MemberCreated(id, name, email, role, organizationId, becameMemberAt))
    }

    fun changeName(newName: String) {
        applyChange(MemberNameChanged(id, newName))
    }

    fun changeEmail(newEmail: String) {
        applyChange(MemberEmailChanged(id, newEmail))
    }

    fun createNewMember(newMemberId: Long, name: String, email: String): Member {
        if (role != MemberRole.OWNER) {
            throw ForbiddenException("Only owner can create new members")
        }

        return Member(newMemberId, name, email, MemberRole.STANDARD_MEMBER, organizationId, DateTime.now())
    }

    fun becomeAnOwner(initiator: Member) {
        if (initiator.role != MemberRole.OWNER) {
            throw ForbiddenException("Only owner can make a member an owner (initiator id: $id), target member id: $id")
        }

        if (role == MemberRole.OWNER) {
            throw ValidationException("Member is already an owner (member id: $id)")
        }
        applyChange(MemberBecameAnOwner(id, MemberRole.OWNER))
    }

    fun becomeAStandardMember(initiator: Member) {
        if (initiator.role != MemberRole.OWNER) {
            throw ForbiddenException("Only owner can make another owner a standard member (initiator id: $id), target owner id: $id")
        }

        if (role == MemberRole.STANDARD_MEMBER) {
            throw ValidationException("Member is already a standard member (member id: $id)")
        }

        applyChange(MemberBecameAStandardMember(id, MemberRole.STANDARD_MEMBER))
    }

    fun disconnect(initiator: Member) {
        if (initiator.role != MemberRole.OWNER) {
            throw ForbiddenException("Only owner can disconnect another member")
        }

        if (initiator.id == id) {
            throw ForbiddenException("Member cannot disconnect itself")
        }

        if (role == MemberRole.FORMER_MEMBER) {
            throw ForbiddenException("Member is already disconnected")
        }

        applyChange(MemberDisconnected(id, MemberRole.FORMER_MEMBER))
    }

    override fun applyEvent(event: DomainEvent) {
        when (event) {
            is MemberCreated -> {
                id = event.id
                name = event.name
                email = event.email
                role = event.role
            }
            is MemberNameChanged -> {
                name = event.name
            }
            is MemberEmailChanged -> {
                email = event.email
            }
            is MemberBecameAnOwner -> {
                role = event.role
            }
            is MemberBecameAStandardMember -> {
                role = event.role
            }
        }
    }
}

data class MemberRole(val value: Int = 0) {
    companion object {
        val STANDARD_MEMBER = MemberRole(1)
        val FORMER_MEMBER = MemberRole(99)
        val OWNER = MemberRole(100)
    }
}

data class MemberCreated(override val id: Long, val name: String, val email: String, val role: MemberRole, val organizationId: Long, val becameMemberAt: DateTime) : DomainEvent
data class MemberNameChanged(override val id: Long, val name: String) : DomainEvent
data class MemberEmailChanged(override val id: Long, val email: String) : DomainEvent
data class MemberBecameAnOwner(override val id: Long, val role: MemberRole) : DomainEvent
data class MemberBecameAStandardMember(override val id: Long, val role: MemberRole) : DomainEvent
data class MemberDisconnected(override val id: Long, val role: MemberRole) : DomainEvent

