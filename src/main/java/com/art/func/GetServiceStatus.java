package com.art.func;

import com.art.service.ServiceUnavailableService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class GetServiceStatus {
    @Resource(name = "serviceUnavailableService")
    private ServiceUnavailableService serviceUnavailableService;

    public int getStatus() {
        return serviceUnavailableService.getServiceUnavailable(new BigInteger("1"));
    }

}
