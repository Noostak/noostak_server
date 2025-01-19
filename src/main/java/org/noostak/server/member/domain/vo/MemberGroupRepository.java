package org.noostak.server.member.domain.vo;

import org.noostak.server.member.domain.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    boolean existsByGroupGroupIdAndMemberMemberId(Long groupId, Long memberId);
}
