package com.art.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "users")
@EqualsAndHashCode
@Entity
@Table(name = "MailingGroups")
public class MailingGroups implements Serializable{
    private BigInteger id;
    private String mailingGroup;
    private List<Users> users;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public BigInteger getId(){
        return id;
    }
    public void setId(BigInteger id){
        this.id = id;
    }

    public String getMailingGroup(){
        return mailingGroup;
    }
    public void setMailingGroup(String mailingGroup){
        this.mailingGroup = mailingGroup;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UsersMailingGroups",
            joinColumns = { @JoinColumn(name = "MailingGroupsId", referencedColumnName = "id") },
            inverseJoinColumns = @JoinColumn(name = "UserId", referencedColumnName = "id"))
    public List<Users> getUsers(){
        return users;
    }
    public void setUsers(List<Users> users){
        this.users = users;
    }
}
