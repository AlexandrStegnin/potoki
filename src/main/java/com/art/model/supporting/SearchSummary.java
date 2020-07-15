package com.art.model.supporting;

import com.art.model.*;
import com.art.model.supporting.enums.ShareType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SearchSummary implements Serializable {

    private List<AppUser> investorsList;
    private String facilityStr;
    private Date dateStart;
    private Date dateEnd;
    private String email;
    private String password;
    private String login;

    private String iYear;
    private String iMonth;
    private String pay;
    private String underFacilityStr;

    private String investor;
    private AppUser user;

    private String inn;
    private String account;
    private String organization;

    private String switchSite;
    private String activateSite;

    private List<Facility> facilityList;

    private String searchStuff;

    private String tableForSearch;

    private String cashSource;
    private String cashSourceId;

    private String cashType;
    private String cashTypeId;

    private String newCashDetail;
    private String newCashDetailId;

    private String invType;
    private String invTypeId;

    private String period;

    private String typeClosingInvest;
    private String typeClosingInvestId;

    private String shareKind;
    private String shareKindId;

    private String what;
    private BigDecimal commission;
    private BigDecimal divideSum;
    private BigInteger divideSumId;
    private MarketingTree salesChanel;

    private List<InvestorsCash> investorsCashList;
    private Facility reFacility;
    private UnderFacility underFacility;
    private UnderFacility reUnderFacility;
    private Date dateReinvest;
    private InvestorsCash investorsCash;
    private List<Long> reinvestIdList;
    private UsersAnnexToContracts usersAnnexToContracts;
    private int annexesCnt;
    private AnnexToContracts annexToContracts;
    private Room room;
    private String dateClose;
    private List<Long> cashIdList;
    private List<UnderFacility> underFacilityList;
    private int pageNumber;
    private BigDecimal commissionNoMore;

    private Facility facility;

    private LocalDate startDate;
    private LocalDate endDate;

    private String[] investors;

    private List<UnderFacility> reUnderFacilityList;

    private Date realDateGiven;

    private ShareType shareType;

    public String getFacilityStr() {
        return facilityStr;
    }

    public void setFacilityStr(String facilityStr) {
        this.facilityStr = facilityStr;
    }

    public String getTableForSearch() {
        return tableForSearch;
    }

    public void setTableForSearch(String tableForSearch) {
        this.tableForSearch = tableForSearch;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSwitchSite() {
        return switchSite;
    }

    public void setSwitchSite(String switchSite) {
        this.switchSite = switchSite;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getActivateSite() {
        return activateSite;
    }

    public void setActivateSite(String activateSite) {
        this.activateSite = activateSite;
    }

    public String getInvestor() {
        return investor;
    }

    public void setInvestor(String investor) {
        this.investor = investor;
    }

    public String getiYear() {
        return iYear;
    }

    public void setiYear(String iYear) {
        this.iYear = iYear;
    }

    public String getiMonth() {
        return iMonth;
    }

    public void setiMonth(String iMonth) {
        this.iMonth = iMonth;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getUnderFacilityStr() {
        return underFacilityStr;
    }

    public void setUnderFacilityStr(String underFacilityStr) {
        this.underFacilityStr = underFacilityStr;
    }

    public String getCashSource() {
        return cashSource;
    }

    public void setCashSource(String cashSource) {
        this.cashSource = cashSource;
    }

    public String getCashSourceId() {
        return cashSourceId;
    }

    public void setCashSourceId(String cashSourceId) {
        this.cashSourceId = cashSourceId;
    }

    public String getCashType() {
        return cashType;
    }

    public void setCashType(String cashType) {
        this.cashType = cashType;
    }

    public String getCashTypeId() {
        return cashTypeId;
    }

    public void setCashTypeId(String cashTypeId) {
        this.cashTypeId = cashTypeId;
    }

    public String getNewCashDetail() {
        return newCashDetail;
    }

    public void setNewCashDetail(String newCashDetail) {
        this.newCashDetail = newCashDetail;
    }

    public String getNewCashDetailId() {
        return newCashDetailId;
    }

    public void setNewCashDetailId(String newCashDetailId) {
        this.newCashDetailId = newCashDetailId;
    }

    public String getInvType() {
        return invType;
    }

    public void setInvType(String invType) {
        this.invType = invType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getInvTypeId() {
        return invTypeId;
    }

    public void setInvTypeId(String invTypeId) {
        this.invTypeId = invTypeId;
    }

    public String getTypeClosingInvest() {
        return typeClosingInvest;
    }

    public void setTypeClosingInvest(String typeClosingInvest) {
        this.typeClosingInvest = typeClosingInvest;
    }

    public String getTypeClosingInvestId() {
        return typeClosingInvestId;
    }

    public void setTypeClosingInvestId(String typeClosingInvestId) {
        this.typeClosingInvestId = typeClosingInvestId;
    }

    public String getShareKind() {
        return shareKind;
    }

    public void setShareKind(String shareKind) {
        this.shareKind = shareKind;
    }

    public String getShareKindId() {
        return shareKindId;
    }

    public void setShareKindId(String shareKindId) {
        this.shareKindId = shareKindId;
    }

    public List<InvestorsCash> getInvestorsCashList() {
        return investorsCashList;
    }

    public void setInvestorsCashList(List<InvestorsCash> investorsCashList) {
        this.investorsCashList = investorsCashList;
    }

    public Facility getReFacility() {
        return reFacility;
    }

    public void setReFacility(Facility reFacility) {
        this.reFacility = reFacility;
    }

    public UnderFacility getReUnderFacility() {
        return reUnderFacility;
    }

    public void setReUnderFacility(UnderFacility reUnderFacility) {
        this.reUnderFacility = reUnderFacility;
    }

    public Date getDateReinvest() {
        return dateReinvest;
    }

    public void setDateReinvest(Date dateReinvest) {
        this.dateReinvest = dateReinvest;
    }

    public UsersAnnexToContracts getUsersAnnexToContracts() {
        return usersAnnexToContracts;
    }

    public void setUsersAnnexToContracts(UsersAnnexToContracts usersAnnexToContracts) {
        this.usersAnnexToContracts = usersAnnexToContracts;
    }

    public int getAnnexesCnt() {
        return annexesCnt;
    }

    public void setAnnexesCnt(int annexesCnt) {
        this.annexesCnt = annexesCnt;
    }

    public AnnexToContracts getAnnexToContracts() {
        return annexToContracts;
    }

    public void setAnnexToContracts(AnnexToContracts annexToContracts) {
        this.annexToContracts = annexToContracts;
    }

    public InvestorsCash getInvestorsCash() {
        return investorsCash;
    }

    public void setInvestorsCash(InvestorsCash investorsCash) {
        this.investorsCash = investorsCash;
    }

    public List<Long> getReinvestIdList() {
        return reinvestIdList;
    }

    public void setReinvestIdList(List<Long> reinvestIdList) {
        this.reinvestIdList = reinvestIdList;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getDateClose() {
        return dateClose;
    }

    public void setDateClose(String dateClose) {
        this.dateClose = dateClose;
    }

    public List<Long> getCashIdList() {
        return cashIdList;
    }

    public void setCashIdList(List<Long> cashIdList) {
        this.cashIdList = cashIdList;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public List<UnderFacility> getUnderFacilityList() {
        return underFacilityList;
    }

    public void setUnderFacilityList(List<UnderFacility> underFacilityList) {
        this.underFacilityList = underFacilityList;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public BigDecimal getCommissionNoMore() {
        return commissionNoMore;
    }

    public void setCommissionNoMore(BigDecimal commissionNoMore) {
        this.commissionNoMore = commissionNoMore;
    }

    public Facility getFacility() {
        return facility;
    }

    public void setFacility(Facility facility) {
        this.facility = facility;
    }

    public UnderFacility getUnderFacility() {
        return underFacility;
    }

    public BigDecimal getDivideSum() {
        return this.divideSum;
    }

    public void setDivideSum(BigDecimal divideSum) {
        this.divideSum = divideSum;
    }

    public List<AppUser> getInvestorsList() {
        return investorsList;
    }

    public void setInvestorsList(List<AppUser> investorsList) {
        this.investorsList = investorsList;
    }

    public List<UnderFacility> getReUnderFacilityList() {
        return reUnderFacilityList;
    }

    public void setReUnderFacilityList(List<UnderFacility> reUnderFacilityList) {
        this.reUnderFacilityList = reUnderFacilityList;
    }

    public Date getRealDateGiven() {
        return realDateGiven;
    }

    public void setRealDateGiven(Date realDateGiven) {
        this.realDateGiven = realDateGiven;
    }

    public ShareType getShareType() {
        return shareType;
    }

    public void setShareType(ShareType shareType) {
        this.shareType = shareType;
    }
}
