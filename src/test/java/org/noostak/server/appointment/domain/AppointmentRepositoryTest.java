package org.noostak.server.appointment.domain;

import org.noostak.server.appointment.domain.repository.AppointmentRepository;
import org.noostak.server.appointment.domain.vo.AppointmentStatus;
import org.noostak.server.group.domain.Group;
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

public class AppointmentRepositoryTest implements AppointmentRepository {

    private final List<Appointment> appointments = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Appointment save(Appointment appointment) {
        if (appointment.getId() == null) {
            setId(appointment);
        }
        appointments.add(appointment);
        return appointment;
    }

    private void setId(Appointment appointment) {
        try {
            var appointmentIdField = Appointment.class.getDeclaredField("id");
            appointmentIdField.setAccessible(true);
            appointmentIdField.set(appointment, idGenerator.getAndIncrement());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("[ERROR] 'id' 필드를 Appointment 클래스에서 찾을 수 없습니다. 필드 이름이 정확한지 확인하세요.", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("[ERROR] Appointment 클래스의 'id' 필드에 접근할 수 없습니다. 접근 제한자가 적절한지 확인하세요.", e);
        }
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        return appointments.stream()
                .filter(appointment -> appointment.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Appointment> findAll() {
        return new ArrayList<>(appointments);
    }

    @Override
    public void deleteAll() {
        appointments.clear();
    }

    @Override
    public void deleteById(Long id) {
        appointments.removeIf(appointment -> appointment.getId().equals(id));
    }

    @Override
    public boolean existsById(Long id) {
        return appointments.stream()
                .anyMatch(appointment -> appointment.getId().equals(id));
    }

    @Override
    public long count() {
        return appointments.size();
    }

    @Override
    public List<Appointment> findAllById(Iterable<Long> ids) {
        List<Appointment> result = new ArrayList<>();
        ids.forEach(id -> findById(id).ifPresent(result::add));
        return result;
    }

    @Override
    public <S extends Appointment> List<S> saveAll(Iterable<S> entities) {
        List<S> result = new ArrayList<>();
        entities.forEach(entity -> result.add((S) save(entity)));
        return result;
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> ids) {
        ids.forEach(this::deleteById);
    }

    @Override
    public void delete(Appointment entity) {
        appointments.remove(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends Appointment> entities) {
        entities.forEach(appointments::remove);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends Appointment> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Appointment> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Appointment> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Appointment getOne(Long aLong) {
        return null;
    }

    @Override
    public Appointment getById(Long aLong) {
        return null;
    }

    @Override
    public Appointment getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Appointment> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Appointment> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Appointment> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Appointment> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Appointment> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Appointment> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Appointment, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Appointment> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Appointment> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Appointment> findByGroupAndAppointmentStatus(Group group, AppointmentStatus status) {
        return appointments.stream()
                .filter(appointment -> appointment.getGroup().equals(group) && appointment.getAppointmentStatus().equals(status))
                .toList();
    }
}
