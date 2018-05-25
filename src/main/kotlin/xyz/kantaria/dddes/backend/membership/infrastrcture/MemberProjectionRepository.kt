package xyz.kantaria.dddes.backend.membership.infrastrcture

import org.joda.time.DateTime
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface MemberProjectionRepository : CrudRepository<MemberProjection, Long>

@Entity(name = "membership_members")
class MemberProjection(
        @Id
        val id: Long = 0,

        val name: String = "",
        val email: String = "",
        val role: Int = 0,

        @Column(name = "organization_id")
        val organizationId: Long = 0,

        @Column(name = "became_member_at")
        val becameMemberAt: DateTime = DateTime.now()
)
