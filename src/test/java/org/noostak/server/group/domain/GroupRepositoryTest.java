package org.noostak.server.group.domain;

import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@Repository
@Primary
public class GroupRepositoryTest implements GroupRepository {

    private final List<Group> groups = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Group save(Group group) {
        if (group.getGroupId() == null) {
            setId(group);
        }
        groups.add(group);
        return group;
    }

    private void setId(Group group) {
        try {
            var groupIdField = Group.class.getDeclaredField("groupId");
            groupIdField.setAccessible(true);
            groupIdField.set(group, idGenerator.getAndIncrement());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[ERROR] 'groupId' 필드를 Group 클래스에서 찾을 수 없습니다. 필드 이름이 정확한지 확인하세요.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("[ERROR] Group 클래스의 'groupId' 필드에 접근할 수 없습니다. 접근 제한자가 적절한지 확인하세요.", e);
        }
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groups.stream()
                .filter(group -> group.getGroupId().equals(id))
                .findFirst();
    }

    @Override
    public <S extends Group> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Group> findAll() {
        return new ArrayList<>(groups);
    }

    @Override
    public List<Group> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public void deleteAll() {
        groups.clear();
    }

    @Override
    public void deleteById(Long id) {
        groups.removeIf(group -> group.getGroupId().equals(id));
    }

    @Override
    public void delete(Group entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Group> entities) {

    }

    @Override
    public boolean existsById(Long id) {
        return groups.stream()
                .anyMatch(group -> group.getGroupId().equals(id));
    }

    @Override
    public long count() {
        return groups.size();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Group> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Group> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Group> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Group getOne(Long aLong) {
        return null;
    }

    @Override
    public Group getById(Long aLong) {
        return null;
    }

    @Override
    public Group getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Group> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Group> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Group> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Group> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Group> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Group> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Group, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Group> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return null;
    }

}
