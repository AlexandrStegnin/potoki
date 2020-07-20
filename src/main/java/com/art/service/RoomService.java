package com.art.service;

import com.art.model.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private InvestorsFlowsService flowsService;

    @Autowired
    private InvestorsCashService cashService;

    public List<Room> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsRoot.fetch(Room_.underFacility, JoinType.LEFT)
                .fetch(UnderFacility_.facility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);//.distinct(true);
        roomsCriteriaQuery.orderBy(cb.asc(roomsRoot.get(Room_.underFacility).get(UnderFacility_.facility).get(Facility_.id)));

        return em.createQuery(roomsCriteriaQuery).getResultList();
    }

    public Room findById(BigInteger id) {
        return this.em.find(Room.class, id);
    }

    public Room findByIdWithUnderFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsRoot.fetch(Room_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Room_.id), id));
        return em.createQuery(roomsCriteriaQuery).getSingleResult();
    }

    public Room findByRoom(String room) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> roomsCriteriaQuery = cb.createQuery(Room.class);
        Root<Room> roomsRoot = roomsCriteriaQuery.from(Room.class);
        roomsRoot.fetch(Room_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Room_.name), room));
        return em.createQuery(roomsCriteriaQuery).getSingleResult();
    }

    public void deleteById(Long id) {
        List<InvestorsFlows> flows = flowsService.findByRoomId(id);
        flows.forEach(f -> f.setRoom(null));
        flowsService.saveList(flows);
        List<InvestorsCash> cashes = cashService.findByRoomId(id);
        cashes.forEach(c -> c.setRoom(null));
        cashService.saveAll(cashes);

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<Room> delete = cb.createCriteriaDelete(Room.class);
        Root<Room> roomsRoot = delete.from(Room.class);
        delete.where(cb.equal(roomsRoot.get(Room_.id), id));
        this.em.createQuery(delete).executeUpdate();
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
        this.em.persist(room);
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
