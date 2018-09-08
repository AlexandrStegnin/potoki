package com.art.converter;

import com.art.model.MailingGroups;
import com.art.service.MailingGroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import javax.annotation.Resource;
import java.math.BigInteger;

public class StringToMailingGroupsConverter implements Converter<String, MailingGroups> {

    @Resource(name = "mailingGroupsService")
    private MailingGroupsService mailingGroupsService;

    @Autowired
    public StringToMailingGroupsConverter(MailingGroupsService mailingGroupsService) {
        this.mailingGroupsService = mailingGroupsService;
    }

    public MailingGroups convert(String id) {
        MailingGroups mailingGroups;
        BigInteger IntId = new BigInteger(id);
        mailingGroups = mailingGroupsService.findById(IntId);
        return mailingGroups;
    }

}
