package com.art.service;

import com.art.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
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
@Repository
public class RoomsService {
    @PersistenceContext(name = "persistanceUnit")
    private EntityManager em;

    @Autowired
    private InvestorsFlowsService flowsService;

    @Autowired
    private InvestorsCashService cashService;

    public List<Rooms> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsRoot.fetch(Rooms_.underFacility, JoinType.LEFT)
                .fetch(UnderFacilities_.facility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);//.distinct(true);
        roomsCriteriaQuery.orderBy(cb.asc(roomsRoot.get(Rooms_.underFacility).get(UnderFacilities_.facility).get(Facilities_.id)));

        return em.createQuery(roomsCriteriaQuery).getResultList();
    }

    public Rooms findById(BigInteger id) {
        return this.em.find(Rooms.class, id);
    }

    public Rooms findByIdWithUnderFacilities(BigInteger id) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsRoot.fetch(Rooms_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Rooms_.id), id));
        return em.createQuery(roomsCriteriaQuery).getSingleResult();
    }

    public Rooms findByRoom(String room) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsRoot.fetch(Rooms_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Rooms_.room), room));
        return em.createQuery(roomsCriteriaQuery).getSingleResult();
    }

    public void deleteById(BigInteger id) {
        List<InvestorsFlows> flows = flowsService.findByRoomId(id);
        flows.forEach(f -> f.setRoom(null));
        flowsService.saveList(flows);
        List<InvestorsCash> cashes = cashService.findByRoomId(id);
        cashes.forEach(c -> c.setRoom(null));
        cashService.saveAll(cashes);

        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaDelete<Rooms> delete = cb.createCriteriaDelete(Rooms.class);
        Root<Rooms> roomsRoot = delete.from(Rooms.class);
        delete.where(cb.equal(roomsRoot.get(Rooms_.id), id));
        this.em.createQuery(delete).executeUpdate();
    }

    public void update(Rooms room) {
        CriteriaBuilder criteriaBuilder = this.em.getCriteriaBuilder();
        CriteriaUpdate<Rooms> update = criteriaBuilder.createCriteriaUpdate(Rooms.class);
        Root<Rooms> roomsRoot = update.from(Rooms.class);
        update.set(Rooms_.room, room.getRoom());
        update.set(Rooms_.coast, room.getCoast());
        update.set(Rooms_.roomSize, room.getRoomSize());
        update.set(Rooms_.underFacility, room.getUnderFacility());
        update.set(Rooms_.sold, room.isSold());
        update.set(Rooms_.dateOfSale, room.getDateOfSale());
        update.where(criteriaBuilder.equal(roomsRoot.get(Rooms_.id), room.getId()));
        this.em.createQuery(update).executeUpdate();
    }

    public void create(Rooms room) {
        this.em.persist(room);
    }

    public List<Rooms> init() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsCriteriaQuery.select(roomsRoot);
        Rooms room = new Rooms();
        room.setId(new BigInteger("0"));
        room.setRoom("Выберите помещение");
        List<Rooms> roomsList = new ArrayList<>(0);
        roomsList.add(room);
        roomsList.addAll(em.createQuery(roomsCriteriaQuery).getResultList());
        return roomsList;
    }

    public List<Rooms> findByUnderFacility(UnderFacilities underFacility) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsRoot.fetch(Rooms_.underFacility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot);
        roomsCriteriaQuery.where(cb.equal(roomsRoot.get(Rooms_.underFacility), underFacility));
        return em.createQuery(roomsCriteriaQuery).getResultList();
    }

    public List<Rooms> findByFacilitiesId(List<BigInteger> facilitiesId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Rooms> roomsCriteriaQuery = cb.createQuery(Rooms.class);
        Root<Rooms> roomsRoot = roomsCriteriaQuery.from(Rooms.class);
        roomsRoot.fetch(Rooms_.underFacility, JoinType.LEFT)
                .fetch(UnderFacilities_.facility, JoinType.LEFT);
        roomsCriteriaQuery.select(roomsRoot).distinct(true);
        roomsCriteriaQuery.where(roomsRoot.get(Rooms_.underFacility).get(UnderFacilities_.facility).get(Facilities_.id).in(facilitiesId));
        return em.createQuery(roomsCriteriaQuery).getResultList();
    }
}
