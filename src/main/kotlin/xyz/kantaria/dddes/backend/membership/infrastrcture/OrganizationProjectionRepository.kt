package xyz.kantaria.dddes.backend.membership.infrastrcture

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface OrganizationProjectionRepository : CrudRepository<OrganizationProjection, Long>

@Entity(name = "membership_organizations")
class OrganizationProjection(
        @Id
        val id: Long = 0,

        val name: String = "",

        @Column(name = "owners_count")
        val ownersCount: Int = 0
)
