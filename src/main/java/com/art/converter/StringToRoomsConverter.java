package com.art.converter;

import com.art.model.Room;
import com.art.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Component
public class StringToRoomsConverter implements Converter<String, Room> {
    @Resource(name = "roomService")
    private RoomService roomService;

    @Autowired
    public StringToRoomsConverter(RoomService roomService) {
        this.roomService = roomService;
    }

    public Room convert(String id) {
        Room room;
        try {
            BigInteger IntId = new BigInteger(id);
            room = roomService.findById(IntId);
        } catch (Exception ex) {
            room = roomService.findByRoom(id);
        }

        return room;
    }
}
