package com.art.config;

import com.art.config.application.WebConfig;
import com.art.converter.*;
import com.art.service.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
@Import(WebConfig.class)
public class FormattersConfig extends WebMvcConfigurerAdapter {

    private final RoleService roleService;

    private final UserService userService;

    private final FacilityService facilityService;

    private final UnderFacilityService underFacilityService;

    private final CashSourcesService cashSourcesService;

    private final NewCashDetailsService newCashDetailsService;

    private final TypeClosingInvestService typeClosingInvestService;

    private final RoomService roomService;

    private final AppTokenService appTokenService;

    public FormattersConfig(RoleService roleService, UserService userService, FacilityService facilityService, UnderFacilityService underFacilityService, CashSourcesService cashSourcesService, NewCashDetailsService newCashDetailsService, TypeClosingInvestService typeClosingInvestService, RoomService roomService, AppTokenService appTokenService) {
        this.roleService = roleService;
        this.userService = userService;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.cashSourcesService = cashSourcesService;
        this.newCashDetailsService = newCashDetailsService;
        this.typeClosingInvestService = typeClosingInvestService;
        this.roomService = roomService;
        this.appTokenService = appTokenService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleToUserRoleConverter(roleService));
        registry.addConverter(new StringToUserConverter(userService));
        registry.addConverter(new StringToFacilityConverter(facilityService));
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToUnderFacilitiesConverter(underFacilityService));
        registry.addConverter(new StringToCashSourceConverter(cashSourcesService));
        registry.addConverter(new StringToNewCashDetailConverter(newCashDetailsService));
        registry.addConverter(new StringToTypeClosingInvestConverter(typeClosingInvestService));
        registry.addConverter(new StringToRoomsConverter(roomService));
        registry.addConverter(new StringToTokenConverter(appTokenService));
        registry.addConverter(new ShareTypeConverter());
    }
}
