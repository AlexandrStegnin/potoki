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

    private final CashSourceService cashSourceService;

    private final NewCashDetailService newCashDetailService;

    private final TypeClosingService typeClosingService;

    private final RoomService roomService;

    private final AppTokenService appTokenService;

    public FormattersConfig(RoleService roleService, UserService userService, FacilityService facilityService, UnderFacilityService underFacilityService, CashSourceService cashSourceService, NewCashDetailService newCashDetailService, TypeClosingService typeClosingService, RoomService roomService, AppTokenService appTokenService) {
        this.roleService = roleService;
        this.userService = userService;
        this.facilityService = facilityService;
        this.underFacilityService = underFacilityService;
        this.cashSourceService = cashSourceService;
        this.newCashDetailService = newCashDetailService;
        this.typeClosingService = typeClosingService;
        this.roomService = roomService;
        this.appTokenService = appTokenService;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleConverter(roleService));
        registry.addConverter(new UserConverter(userService));
        registry.addConverter(new FacilityConverter(facilityService));
        registry.addConverter(new DateConverter());
        registry.addConverter(new StringToUnderFacilityConverter(underFacilityService));
        registry.addConverter(new CashSourceConverter(cashSourceService));
        registry.addConverter(new NewCashDetailConverter(newCashDetailService));
        registry.addConverter(new TypeClosingConverter(typeClosingService));
        registry.addConverter(new StringToRoomConverter(roomService));
        registry.addConverter(new TokenConverter(appTokenService));
        registry.addConverter(new ShareTypeConverter());
    }
}
