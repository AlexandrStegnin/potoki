package com.art.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "Emails")
public class Emails implements Serializable {
  private BigInteger id;
  private String email;
  private AppUser user;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_tx_generator")
  @SequenceGenerator(name = "account_tx_generator", sequenceName = "account_transaction_id_seq")
  @Column(name = "Id")
  public BigInteger getId() {
    return id;
  }

  public void setId(BigInteger id) {
    this.id = id;
  }

  @Column(name = "Email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @ManyToOne(cascade =
      {
          CascadeType.DETACH,
          CascadeType.MERGE,
          CascadeType.REFRESH,
          CascadeType.PERSIST
      },
      fetch = FetchType.LAZY)
  @JoinColumn(name = "UserId", referencedColumnName = "Id")
  public AppUser getUser() {
    return user;
  }

  public void setUser(AppUser user) {
    this.user = user;
  }

}
