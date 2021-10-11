package com.art.model;

import com.art.model.supporting.dto.TokenDTO;

import javax.persistence.*;
import java.util.UUID;

/**
 * @author Alexandr Stegnin
 */

@Entity
@Table(name = "app_token")
public class AppToken {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_token_generator")
  @SequenceGenerator(name = "app_token_generator", sequenceName = "app_token_id_seq")
  @Column(name = "Id")
  private Long id;

  @Column(name = "app_name")
  private String appName;

  @Column(name = "token")
  private String token;

  public AppToken() {
    this.token = generateToken();
  }

  public AppToken(TokenDTO dto) {
    this.id = dto.getId();
    this.appName = dto.getAppName();
    this.token = dto.getToken();
    if (this.token == null) {
      this.token = generateToken();
    }
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getAppName() {
    return appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  private String generateToken() {
    return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
  }

}
