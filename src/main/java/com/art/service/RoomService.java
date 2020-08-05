package com.art.service;

import com.art.model.*;
import com.art.model.supporting.enums.OwnerType;
import com.art.repository.RoomRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {

    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    private final InvestorsFlowsService flowsService;

    private final InvestorCashService cashService;

    private final AccountService accountService;

    private final RoomRepository roomRepository;

    public RoomService(InvestorsFlowsService flowsService, InvestorCashService cashService,
                       AccountService accountService, RoomRepository roomRepository) {
        this.flowsService = flowsService;
        this.cashService = cashService;
        this.accountService = accountService;
        this.roomRepository = roomRepository;
    }

    public List<Room> findAll() {
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> {
            Hibernate.initialize(room.getUnderFacility());
            Hibernate.initialize(room.getUnderFacility().getFacility());
        });
        return rooms;
    }

    public Room findById(Long id) {
        return roomRepository.findOne(id);
    }

    public Room findByIdWithUnderFacilities(Long id) {
        Room room = roomRepository.findOne(id);
        Hibernate.initialize(room.getUnderFacility());
        return room;
    }

    public Room findByRoom(String name) {
        return roomRepository.findByName(name);
    }

    public void deleteById(Long id) {
        List<InvestorsFlows> flows = flowsService.findByRoomId(id);
        flows.forEach(f -> f.setRoom(null));
        flowsService.saveList(flows);
        List<InvestorCash> cashes = cashService.findByRoomId(id);
        cashes.forEach(c -> c.setRoom(null));
        cashService.saveAll(cashes);

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<Room> delete = cb.createCriteriaDelete(Room.class);
        Root<Room> roomsRoot = delete.from(Room.class);
        delete.where(cb.equal(roomsRoot.get(Room_.id), id));
        this.em.createQuery(delete).executeUpdate();
        accountService.deleteByOwnerId(id, OwnerType.UNDER_FACILITY);
    }

    public void update(Room room) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<Room> update = criteriaBuilder.createCriteriaUpdate(Room.class);
        Root<Room> roomsRoot = update.from(Room.class);
        update.set(Room_.name, room.getName());
        update.set(Room_.cost, room.getCost());
        update.set(Room_.roomSize, room.getRoomSize());
        update.set(Room_.underFacility, room.getUnderFacility());
        update.set(Room_.sold, room.isSold());
        update.set(Room_.dateSale, room.getDateSale());
        update.set(Room_.salePrice, room.getSalePrice());
        update.set(Room_.dateBuy, room.getDateBuy());
        update.set(Room_.totalYearProfit, room.getTotalYearProfit());
        update.where(criteriaBuilder.equal(roomsRoot.get(Room_.id), room.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void create(Room room) {
        roomRepository.saveAndFlush(room);
        UnderFacility underFacility = room.getUnderFacility();
        Account account = accountService.findByOwnerId(underFacility.getId(), OwnerType.UNDER_FACILITY);
        int countRooms = roomRepository.countByUnderFacilityId(underFacility.getId());
        accountService.createAccount(room, account, countRooms);
    }

    public List<Room> init() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsCriteriaQuery.select(roomsRoot);
        Room room = new Room();
        room.setId(0L);
        room.setName("Выберите помещение");
        List<Room> roomList = new ArrayList<>(0);
        roomList.add(room);
        roomList.addAll(em.createQuery(roomsCriteriaQuery).getResultList());
        return roomList;
    }

    public List<Room> findByUnderFacility(UnderFacility underFacility) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsRoot.fetch(Room_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Room_.underFacility), underFacility));
        return em.createQuery(roomsCriteriaQuery).getResultList();
    }

    public List<Room> findByFacilitiesId(List<BigInteger> facilitiesId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsRoot.fetch(Room_.underFacility, JoinType.LEFT)
                .fetch(UnderFacility_.facility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot).distinct(true);
        roomsCriteriaQuery.where(roomsRoot.get(Room_.underFacility).get(UnderFacility_.facility).get(Facility_.id).in(facilitiesId));
        return em.createQuery(roomsCriteriaQuery).getResultList();
    }
}
