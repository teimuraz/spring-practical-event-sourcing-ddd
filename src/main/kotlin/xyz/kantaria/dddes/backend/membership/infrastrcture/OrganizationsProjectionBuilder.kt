package xyz.kantaria.dddes.backend.membership.infrastrcture

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import xyz.kantaria.dddes.backend.membership.domain.MemberCreated
import xyz.kantaria.dddes.backend.membership.domain.OrganizationCreated

@Component
class OrganizationsProjectionBuilder @Autowired constructor(private val organizationProjectionRepository: OrganizationProjectionRepository) {

    @EventListener
    fun handle(e: OrganizationCreated) {
        val projection = OrganizationProjection(
                id = e.id,
                name = e.name,
                ownersCount = e.ownersCount
        )

        organizationProjectionRepository.save(projection)
    }
}