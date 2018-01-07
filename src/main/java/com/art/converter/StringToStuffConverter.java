package com.art.converter;

import com.art.model.Stuffs;
import com.art.service.StuffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToStuffConverter implements Converter<String, Stuffs> {

    //@Autowired
    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Autowired
    public StringToStuffConverter(StuffService stuffService) {
        this.stuffService = stuffService;
    }

    /**
     * Gets UserProfile by Id
     * @see Converter#convert(Object)
     */
    public Stuffs convert(String id) {
        Stuffs stuffs = null;
        try{
            BigInteger IntId = new BigInteger(id);
            stuffs = stuffService.findById(IntId);
        }catch(Exception ex){
            stuffs = stuffService.findByStuff(id);
        }

        System.out.println("Stuff : "+stuffs);
        return stuffs;
    }
}
