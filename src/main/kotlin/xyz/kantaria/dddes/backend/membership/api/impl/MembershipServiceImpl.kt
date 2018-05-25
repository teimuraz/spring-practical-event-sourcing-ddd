package xyz.kantaria.dddes.backend.membership.api.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import xyz.kantaria.dddes.backend.membership.api.CreateNewMemberReq
import xyz.kantaria.dddes.backend.membership.api.MemberDto
import xyz.kantaria.dddes.backend.membership.api.MembershipService
import xyz.kantaria.dddes.backend.membership.domain.Member
import xyz.kantaria.dddes.backend.membership.domain.MemberRepository
import javax.xml.bind.ValidationException

@Component
class MembershipServiceImpl @Autowired constructor(
        private val memberRepository: MemberRepository
) : MembershipService {

    @Transactional
    override fun createNewMember(req: CreateNewMemberReq): MemberDto {
        val currentMemberId: Long = 1

        val initiator = memberRepository.findById(currentMemberId)
                ?: throw ValidationException("Non existing initiator cannot create a member")

        val newMemberId = memberRepository.nextId()

        val newMember = initiator.createNewMember(newMemberId, req.name, req.email)

        memberRepository.save(newMember)

        return domainToDto(newMember)
    }

    private fun domainToDto(domain: Member): MemberDto {
        return MemberDto(
                domain.id,
                domain.name,
                domain.email,
                domain.role,
                domain.organizationId,
                domain.becameMemberAt
        )
    }
}