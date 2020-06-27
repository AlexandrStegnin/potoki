package com.art.model;

import com.art.model.supporting.enums.InvestorsExpEnum;
import com.art.model.supporting.InvestorsPlanSale;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

@SqlResultSetMapping(
        name = "InvestorsPlanSaleMapping",
        classes = @ConstructorResult(
                targetClass = InvestorsPlanSale.class,
                columns = {
                        @ColumnResult(name = "dateGivedCash", type = Date.class),
                        @ColumnResult(name = "facility", type = String.class),
                        @ColumnResult(name = "givedCash", type = float.class),
                        @ColumnResult(name = "dolya", type = float.class),
                        @ColumnResult(name = "dohodNaRukiObshii", type = float.class),
                        @ColumnResult(name = "dohodnostGodovaya3", type = float.class)
                }))
@NamedNativeQuery(
        name = "InvestorsPlanSale",
        query = "SELECT DateGivedCash, Facility, GivedCash, dolya, dohodNaRukiObshii, " +
                "dohodnostGodovaya3 " +
                "FROM InvestorsPlanSaleForIDEA " +
                "WHERE InvestorId = ? " +
                "ORDER BY Facility, DateGivedCash",
        resultClass = InvestorsPlanSale.class,
        resultSetMapping = "InvestorsPlanSaleMapping")

@Getter
@Setter
@ToString(exclude = {"investor", "facility"})
@EqualsAndHashCode(exclude = {"investor", "facility"})
@Entity
@Table(name = "InvestorsExpenses")
public class InvestorsExpenses implements Serializable {

    private BigInteger id;
    private InvestorsExpEnum classExp;
    private float sizeExp;
    private Date dateStExp;
    private Date dateEndExp;

    private TypeExpenses typeExpenses;
    private Facilities facility;
    private Users investor;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "ClassExp")
    public InvestorsExpEnum getClassExp() {
        return classExp;
    }

    public void setClassExp(InvestorsExpEnum classExp) {
        this.classExp = classExp;
    }

    @OneToOne
    @JoinColumn(name = "TypeExpId", referencedColumnName = "ID")
    public TypeExpenses getTypeExpenses() {
        return typeExpenses;
    }

    public void setFacility(TypeExpenses typeExpenses) {
        this.typeExpenses = typeExpenses;
    }

    @OneToOne
    @JoinColumn(name = "FacilityId", referencedColumnName = "ID")
    public Facilities getFacility() {
        return facility;
    }

    public void setFacility(Facilities facility) {
        this.facility = facility;
    }

    @OneToOne
    @JoinColumn(name = "InvestorId", referencedColumnName = "ID")
    public Users getInvestor() {
        return investor;
    }

    public void setInvestor(Users investor) {
        this.investor = investor;
    }

    @Transient
    public String getDateStExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateStExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }

    @Transient
    public String getDateEndExpToLocalDate() {
        String localDate = "";
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        try {
            localDate = format.format(dateEndExp);
        } catch (Exception ignored) {
        }

        return localDate;
    }
}
