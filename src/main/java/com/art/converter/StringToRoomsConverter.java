package com.art.converter;

import com.art.model.Rooms;
import com.art.service.RoomsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigInteger;

@Component
public class StringToRoomsConverter implements Converter<String, Rooms> {
    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Autowired
    public StringToRoomsConverter(RoomsService roomsService) {
        this.roomsService = roomsService;
    }

    public Rooms convert(String id) {
        Rooms rooms = null;
        try{
            BigInteger IntId = new BigInteger(id);
            rooms = roomsService.findById(IntId);
        }catch(Exception ex){
            rooms = roomsService.findByRoom(id);
        }

        return rooms;
    }
}
