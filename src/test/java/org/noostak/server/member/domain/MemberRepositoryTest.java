package org.noostak.server.member.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public class MemberRepositoryTest implements MemberRepository {

    private final List<Member> members = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Member save(Member member) {
        try {
            if (member.getMemberId() == null) {
                var memberIdField = Member.class.getDeclaredField("memberId");
                memberIdField.setAccessible(true);
                memberIdField.set(member, idGenerator.getAndIncrement());
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[ERROR] 'memberId' 필드를 Member 클래스에서 찾을 수 없습니다. 필드 이름이 정확한지 확인하세요.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("[ERROR] Member 클래스의 'memberId' 필드에 접근할 수 없습니다. 접근 제한자가 적절한지 확인하세요.", e);
        }
        members.add(member);
        return member;
    }


    @Override
    public Optional<Member> findById(Long id) {
        return members.stream()
                .filter(member -> member.getMemberId().equals(id))
                .findFirst();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(members);
    }

    @Override
    public void deleteAll() {
        members.clear();
    }

    @Override
    public void deleteById(Long id) {
        members.removeIf(member -> member.getMemberId().equals(id));
    }

    @Override
    public void delete(Member entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Member> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return members.stream().anyMatch(member -> member.getMemberId().equals(id));
    }

    @Override
    public long count() {
        return members.size();
    }

    @Override
    public List<Member> findAllById(Iterable<Long> ids) {
        List<Member> result = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Override
    public <S extends Member> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> {
            Member savedMember = save(entity);
            result.add((S) savedMember);
        });
        return result;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Member> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Member> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Member> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Member getOne(Long aLong) {
        return null;
    }

    @Override
    public Member getById(Long aLong) {
        return null;
    }

    @Override
    public Member getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Member> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Member> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Member> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Member> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Member, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Member> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return null;
    }
}
