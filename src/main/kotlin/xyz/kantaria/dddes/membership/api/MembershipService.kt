package xyz.kantaria.dddes.membership.api

interface MembershipService {
    fun createNewMember(req: CreateNewMemberReq)
}

data class CreateNewMemberReq(val name: String, val email: String)
