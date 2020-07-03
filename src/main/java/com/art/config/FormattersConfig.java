package com.art.config;

import com.art.config.application.WebConfig;
import com.art.converter.*;
import com.art.service.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@EnableWebMvc
@Configuration
@Import(WebConfig.class)
public class FormattersConfig extends WebMvcConfigurerAdapter {

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "cashSourcesService")
    private CashSourcesService cashSourcesService;

    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "appTokenService")
    private AppTokenService appTokenService;

    @Resource(name = "clientTypeService")
    private ClientTypeService clientTypeService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleToUserRoleConverter(roleService));
        registry.addConverter(new StringToUserConverter(userService));
        registry.addConverter(new StringToFacilityConverter(facilityService));
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToUnderFacilitiesConverter(underFacilitiesService));
        registry.addConverter(new StringToCashSourceConverter(cashSourcesService));
        registry.addConverter(new StringToNewCashDetailConverter(newCashDetailsService));
        registry.addConverter(new StringToTypeClosingInvestConverter(typeClosingInvestService));
        registry.addConverter(new StringToShareKindConverter(shareKindService));
        registry.addConverter(new StringToRoomsConverter(roomsService));
        registry.addConverter(new StringToTokenConverter(appTokenService));
        registry.addConverter(new StringToClientTypeConverter(clientTypeService));
    }
}
