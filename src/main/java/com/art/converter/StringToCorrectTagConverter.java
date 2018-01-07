package com.art.converter;

import com.art.model.AlphaCorrectTags;
import com.art.service.AlphaCorrectTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToCorrectTagConverter implements Converter<String, AlphaCorrectTags> {

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Autowired
    public StringToCorrectTagConverter(AlphaCorrectTagsService alphaCorrectTagsService){
        this.alphaCorrectTagsService = alphaCorrectTagsService;
    }

    public AlphaCorrectTags convert(String id) {
        BigInteger IntId = new BigInteger(id);
        AlphaCorrectTags alphaCorrectTags;
        alphaCorrectTags = alphaCorrectTagsService.findById(IntId);
        return alphaCorrectTags;
    }
}
