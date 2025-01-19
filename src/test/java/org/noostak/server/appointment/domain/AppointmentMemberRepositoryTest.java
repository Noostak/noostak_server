package org.noostak.server.appointment.domain;

import org.noostak.server.appointment.domain.repository.AppointmentMemberRepository;
import org.noostak.server.member.domain.Member;
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

public class AppointmentMemberRepositoryTest implements AppointmentMemberRepository {

    private final List<AppointmentMember> appointmentMembers = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Optional<AppointmentMember> findByAppointmentAndMember(Appointment appointment, Member member) {
        return appointmentMembers.stream()
                .filter(appointmentMember -> appointmentMember.getAppointment().equals(appointment)
                        && appointmentMember.getMember().equals(member))
                .findFirst();
    }

    @Override
    public <S extends AppointmentMember> S save(S entity) {
        if (entity.getAppointmentMemberId() == null) {
            setId(entity);
        }
        appointmentMembers.add(entity);
        return entity;
    }

    private void setId(AppointmentMember appointmentMember) {
        try {
            var appointmentMemberIdField = AppointmentMember.class.getDeclaredField("appointmentMemberId");
            appointmentMemberIdField.setAccessible(true);
            appointmentMemberIdField.set(appointmentMember, idGenerator.getAndIncrement());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[ERROR] 'appointmentMemberId' 필드를 AppointmentMember 클래스에서 찾을 수 없습니다.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("[ERROR] AppointmentMember 클래스의 'appointmentMemberId' 필드에 접근할 수 없습니다.", e);
        }
    }

    @Override
    public List<AppointmentMember> findAll() {
        return new ArrayList<>(appointmentMembers);
    }

    @Override
    public List<AppointmentMember> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {
        appointmentMembers.removeIf(appointmentMember -> appointmentMember.getAppointmentMemberId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return appointmentMembers.stream()
                .anyMatch(appointmentMember -> appointmentMember.getAppointmentMemberId().equals(id));
    }

    @Override
    public long count() {
        return appointmentMembers.size();
    }

    @Override
    public Optional<AppointmentMember> findById(Long id) {
        return appointmentMembers.stream()
                .filter(appointmentMember -> appointmentMember.getAppointmentMemberId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteAll() {
        appointmentMembers.clear();
    }

    @Override
    public <S extends AppointmentMember> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add((S) save(entity)));
        return result;
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void delete(AppointmentMember entity) {
        appointmentMembers.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends AppointmentMember> entities) {
        entities.forEach(appointmentMembers::remove);
    }

    // 아래는 기본적으로 필요하지 않는 메서드들로, 비워둔 채로 구현
    @Override
    public void flush() {}

    @Override
    public <S extends AppointmentMember> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<AppointmentMember> entities) {}

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {}

    @Override
    public void deleteAllInBatch() {}

    @Override
    public AppointmentMember getOne(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMember getById(Long aLong) {
        return null;
    }

    @Override
    public AppointmentMember getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends AppointmentMember> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMember> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends AppointmentMember> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppointmentMember> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppointmentMember> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends AppointmentMember, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<AppointmentMember> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<AppointmentMember> findAll(Pageable pageable) {
        return null;
    }
}
