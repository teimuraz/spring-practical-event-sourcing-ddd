package xyz.kantaria.dddes.backend.membership.infrastrcture

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import xyz.kantaria.dddes.backend.membership.domain.MemberCreated

@Component
class MembersProjectionBuilder @Autowired constructor(private val memberProjectionRepository: MemberProjectionRepository){

    @EventListener
    fun handle(e: MemberCreated) {
        val projection = MemberProjection(
                id = e.id,
                name = e.name,
                email = e.email,
                role = e.role.value,
                organizationId = e.organizationId,
                becameMemberAt = e.becameMemberAt
        )

        memberProjectionRepository.save(projection)
    }
}