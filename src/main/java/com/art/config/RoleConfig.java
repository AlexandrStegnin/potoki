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
public class RoleConfig extends WebMvcConfigurerAdapter {

    @Resource(name = "stuffService")
    private StuffService stuffService;

    @Resource(name = "roleService")
    private RoleService roleService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "facilityService")
    private FacilityService facilityService;

    @Resource(name = "paymentsMethodService")
    private PaymentsMethodService paymentsMethodService;

    @Resource(name = "paymentsTypeService")
    private PaymentsTypeService paymentsTypeService;

    @Resource(name = "underFacilitiesService")
    private UnderFacilitiesService underFacilitiesService;

    @Resource(name = "bonusTypesService")
    private BonusTypesService bonusTypesService;

    @Resource(name = "typeExpensesService")
    private TypeExpensesService typeExpensesService;

    @Resource(name = "alphaCorrectTagsService")
    private AlphaCorrectTagsService alphaCorrectTagsService;

    @Resource(name = "mailingGroupsService")
    private MailingGroupsService mailingGroupsService;

    @Resource(name = "cashSourcesService")
    private CashSourcesService cashSourcesService;

    @Resource(name = "cashTypesService")
    private CashTypesService cashTypesService;

    @Resource(name = "newCashDetailsService")
    private NewCashDetailsService newCashDetailsService;

    @Resource(name = "investorsTypesService")
    private InvestorsTypesService investorsTypesService;

    @Resource(name = "typeClosingInvestService")
    private TypeClosingInvestService typeClosingInvestService;

    @Resource(name = "shareKindService")
    private ShareKindService shareKindService;

    @Resource(name = "roomsService")
    private RoomsService roomsService;

    @Resource(name = "appTokenService")
    private AppTokenService appTokenService;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new RoleToUserRoleConverter(roleService));
        registry.addConverter(new StringToStuffConverter(stuffService));
        registry.addConverter(new StringToUserConverter(userService));
        registry.addConverter(new StringToFacilityConverter(facilityService));
        registry.addConverter(new StringToPaymentsMethodConverter(paymentsMethodService));
        registry.addConverter(new StringToDateConverter());
        registry.addConverter(new StringToPaymentsTypeConverter(paymentsTypeService));
        registry.addConverter(new StringToUnderFacilitiesConverter(underFacilitiesService));
        registry.addConverter(new StringToBonusTypesConverter(bonusTypesService));
        registry.addConverter(new StringToTypeExpensesConverter(typeExpensesService));
        registry.addConverter(new StringToCorrectTagConverter(alphaCorrectTagsService));
        registry.addConverter(new StringToMailingGroupsConverter(mailingGroupsService));
        registry.addConverter(new StringToCashSourceConverter(cashSourcesService));
        registry.addConverter(new StringToCashTypeConverter(cashTypesService));
        registry.addConverter(new StringToNewCashDetailConverter(newCashDetailsService));
        registry.addConverter(new StringToInvestorsTypesConverter(investorsTypesService));
        registry.addConverter(new StringToTypeClosingInvestConverter(typeClosingInvestService));
        registry.addConverter(new StringToShareKindConverter(shareKindService));
        registry.addConverter(new StringToRoomsConverter(roomsService));
        registry.addConverter(new StringToTokenConverter(appTokenService));
    }
}
