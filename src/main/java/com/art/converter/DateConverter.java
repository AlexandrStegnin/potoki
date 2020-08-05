package com.art.converter;

import org.springframework.core.convert.converter.Converter;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateConverter implements Converter<String, Date> {

    public Date convert(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date utilDate = null;

        try {
            utilDate = format.parse(date);
            utilDate = new Date(utilDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return utilDate;

    }
}
