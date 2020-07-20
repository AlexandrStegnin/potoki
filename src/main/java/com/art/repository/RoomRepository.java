package com.art.repository;

import com.art.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    Room findByName(String name);

}
