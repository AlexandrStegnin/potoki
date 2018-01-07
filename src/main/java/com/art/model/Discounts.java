package com.art.model;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "DISCOUNTS")
public class Discounts {
    private BigInteger id;
    private float discount;
    private Date discount_start;
    private Date discount_end;
    private Users discountRentor;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    public Users getDiscountRentor(){
        return discountRentor;
    }
    public void setDiscountRentor(Users discountRentor){
        this.discountRentor = discountRentor;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    @Column(name = "DISCOUNT")
    public float getDiscount() {
        return discount;
    }
    public void setDiscount(float discount) {
        this.discount = discount;
    }

    @Column(name = "DISCOUNT_START")
    public Date getDiscount_start() {
        return discount_start;
    }
    public void setDiscount_start(Date discount_start) {
        this.discount_start = discount_start;
    }

    @Column(name = "DISCOUNT_END")
    public Date getDiscount_end() {
        return discount_end;
    }
    public void setDiscount_end(Date discount_end) {
        this.discount_end = discount_end;
    }
}
