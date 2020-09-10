package com.art.service;

import com.art.model.*;
import com.art.model.supporting.enums.OwnerType;
import com.art.repository.RoomRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoomService {

    private final InvestorsFlowsService flowsService;

    private final MoneyService cashService;

    private final AccountService accountService;

    private final RoomRepository roomRepository;

    public RoomService(InvestorsFlowsService flowsService, MoneyService cashService,
                       AccountService accountService, RoomRepository roomRepository) {
        this.flowsService = flowsService;
        this.cashService = cashService;
        this.accountService = accountService;
        this.roomRepository = roomRepository;
    }

//    @Cacheable(Constant.ROOMS_CACHE_KEY)
    public List<Room> findAll() {
        List<Room> rooms = roomRepository.findAll();
        rooms.forEach(room -> {
            Hibernate.initialize(room.getUnderFacility());
            Hibernate.initialize(room.getUnderFacility().getFacility());
        });
        return rooms;
    }

//    @Cacheable(Constant.ROOMS_CACHE_KEY)
    public Room findById(Long id) {
        return roomRepository.findOne(id);
    }

    public Room findByIdWithUnderFacility(Long id) {
        Room room = roomRepository.findOne(id);
        Hibernate.initialize(room.getUnderFacility());
        return room;
    }

//    @Cacheable(Constant.ROOMS_CACHE_KEY)
    public Room findByRoom(String name) {
        return roomRepository.findByName(name);
    }

//    @CacheEvict(value = Constant.ROOMS_CACHE_KEY)
    public void deleteById(Long id) {
        List<InvestorsFlows> flows = flowsService.findByRoomId(id);
        flows.forEach(f -> f.setRoom(null));
        flowsService.saveList(flows);
        List<Money> cashes = cashService.findByRoomId(id);
        cashes.forEach(c -> {
            c.setRoom(null);
            cashService.update(c);
        });
        roomRepository.delete(id);
        accountService.deleteByOwnerId(id, OwnerType.UNDER_FACILITY);
    }

//    @CachePut(value = Constant.ROOMS_CACHE_KEY, key = "#room.id")
    public void update(Room room) {
        roomRepository.saveAndFlush(room);
    }

//    @CachePut(Constant.ROOMS_CACHE_KEY)
    public void create(Room room) {
        roomRepository.saveAndFlush(room);
        UnderFacility underFacility = room.getUnderFacility();
        Account account = accountService.findByOwnerId(underFacility.getId(), OwnerType.UNDER_FACILITY);
        int countRooms = roomRepository.countByUnderFacilityId(underFacility.getId());
        accountService.createAccount(room, account, countRooms);
    }

    public List<Room> init() {
        Room room = new Room();
        room.setId(0L);
        room.setName("Выберите помещение");
        List<Room> roomList = new ArrayList<>(0);
        roomList.add(room);
        roomList.addAll(findAll());
        return roomList;
    }

}
