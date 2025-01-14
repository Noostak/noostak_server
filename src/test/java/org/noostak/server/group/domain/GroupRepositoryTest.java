package org.noostak.server.group.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepositoryTest extends JpaRepository<Group, Long> {
}
