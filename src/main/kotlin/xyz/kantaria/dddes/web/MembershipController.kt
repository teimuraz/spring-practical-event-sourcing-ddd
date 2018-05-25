package xyz.kantaria.dddes.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import xyz.kantaria.dddes.backend.membership.api.CreateNewMemberReq
import xyz.kantaria.dddes.backend.membership.api.MemberDto
import xyz.kantaria.dddes.backend.membership.api.MembershipService

@RestController
@RequestMapping("/membership")
class MembershipController @Autowired constructor(private val membershipService: MembershipService) {

    @GetMapping
    fun createNewMember(): MemberDto {
        val req = CreateNewMemberReq("temo", "teimuraz.kantaria@gmail.com")
        return membershipService.createNewMember(req)
    }
}