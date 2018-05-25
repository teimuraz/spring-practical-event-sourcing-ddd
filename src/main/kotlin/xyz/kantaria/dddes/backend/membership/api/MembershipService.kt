package xyz.kantaria.dddes.backend.membership.api

import org.joda.time.DateTime
import xyz.kantaria.dddes.backend.membership.domain.MemberRole

interface MembershipService {
    fun createNewMember(req: CreateNewMemberReq): MemberDto
}

data class MemberDto(
        val id: Long,
        val name: String,
        val email: String,
        val role: MemberRole,
        val organizationId: Long,
        val becameMemberAt: DateTime
)

data class CreateNewMemberReq(val name: String, val email: String)
