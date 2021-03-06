package xyz.kantaria.dddes.backend.membership.infrastrcture

import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import xyz.kantaria.dddes.backend.membership.domain.*
import java.util.logging.Logger

@Component
class Seeder @Autowired constructor(
        private val memberRepository: MemberRepository,
        private val organizationRepository: OrganizationRepository
) {

    private val log = LoggerFactory.getLogger(Seeder::class.java)

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun doSomethingAfterStartup() {
        val existingMember = memberRepository.findById(1)
        if (existingMember == null) {
            val member = Member(1, "teimuraz", "teimuraz.kantaria@gmail.com", MemberRole.OWNER, 1, DateTime.now())
            log.info("Seeding member $member")
            memberRepository.save(member)
        }

        val existingOrganization = organizationRepository.findById(1)
        if (existingOrganization == null) {
            val organization = Organization(1, "Acme")
            log.info("Seeding organization $organization")
            organizationRepository.save(organization)
        }
    }
}