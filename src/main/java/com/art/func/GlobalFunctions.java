package com.art.func;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class GlobalFunctions {

    public int getMonthInt(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getYearInt(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        return Integer.parseInt(dateFormat.format(date));
    }
    public int getDayInt(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
        return Integer.parseInt(dateFormat.format(date));
    }

}
