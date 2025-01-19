package org.noostak.server.member.domain.vo;

import org.noostak.server.member.domain.MemberGroup;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class MemberGroupRepositoryTest implements MemberGroupRepository {

    private final List<MemberGroup> memberGroups = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public boolean existsByGroupGroupIdAndMemberMemberId(Long groupId, Long memberId) {
        return memberGroups.stream()
                .anyMatch(memberGroup -> memberGroup.getGroup().getGroupId().equals(groupId)
                        && memberGroup.getMember().getMemberId().equals(memberId));
    }

    @Override
    public MemberGroup save(MemberGroup memberGroup) {
        try {
            if (memberGroup.getMemberGroupId() == null) {
                var idField = MemberGroup.class.getDeclaredField("memberGroupId");
                idField.setAccessible(true);
                idField.set(memberGroup, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[ERROR] 'memberGroupId' 필드를 MemberGroup 클래스에서 찾을 수 없습니다. 필드 이름이 정확한지 확인하세요.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("[ERROR] MemberGroup 클래스의 'memberGroupId' 필드에 접근할 수 없습니다. 접근 제한자가 적절한지 확인하세요.", e);
        }
        memberGroups.add(memberGroup);
        return memberGroup;
    }


    @Override
    public Optional<MemberGroup> findById(Long id) {
        return memberGroups.stream()
                .filter(memberGroup -> memberGroup.getMemberGroupId().equals(id))
                .findFirst();
    }

    @Override
    public List<MemberGroup> findAll() {
        return new ArrayList<>(memberGroups);
    }

    @Override
    public void deleteAll() {
        memberGroups.clear();
    }

    @Override
    public void deleteById(Long id) {
        memberGroups.removeIf(memberGroup -> memberGroup.getMemberGroupId().equals(id));
    }

    @Override
    public void delete(MemberGroup entity) {
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends MemberGroup> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return memberGroups.stream().anyMatch(memberGroup -> memberGroup.getMemberGroupId().equals(id));
    }

    @Override
    public long count() {
        return memberGroups.size();
    }

    @Override
    public List<MemberGroup> findAllById(Iterable<Long> ids) {
        List<MemberGroup> result = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Override
    public <S extends MemberGroup> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> {
            MemberGroup savedMemberGroup = save(entity);
            result.add((S) savedMemberGroup);
        });
        return result;
    }

    @Override
    public void flush() {}

    @Override
    public <S extends MemberGroup> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends MemberGroup> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<MemberGroup> entities) {}

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {}

    @Override
    public void deleteAllInBatch() {}

    @Override
    public MemberGroup getOne(Long id) {
        return null;
    }

    @Override
    public MemberGroup getById(Long id) {
        return null;
    }

    @Override
    public MemberGroup getReferenceById(Long id) {
        return null;
    }

    @Override
    public <S extends MemberGroup> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends MemberGroup> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends MemberGroup> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends MemberGroup> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends MemberGroup> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends MemberGroup> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends MemberGroup, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<MemberGroup> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<MemberGroup> findAll(Pageable pageable) {
        return null;
    }
}
