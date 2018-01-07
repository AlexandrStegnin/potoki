package com.art.model.supporting;

import com.art.model.MailingGroups;
import com.art.model.Users;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@ToString(exclude = {"mailingGroups", "users", "fileBucket"})
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SendingMail implements Serializable {

    private List<MailingGroups> mailingGroups;
    private List<Users> users;
    private String subject;
    private String body;
    private List<FileBucket> fileBucket;

    public List<Users> getUsers(){
        return users;
    }
    public void setUsers(List<Users> users){
        this.users = users;
    }

    public List<MailingGroups> getMailingGroups(){
        return mailingGroups;
    }
    public void setMailingGroups(List<MailingGroups> mailingGroups){
        this.mailingGroups = mailingGroups;
    }

    public String getBody(){
        return body;
    }
    public void setBody(String body){
        this.body = body;
    }

    public String getSubject(){
        return subject;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }

    public List<FileBucket> getFileBucket(){
        return fileBucket;
    }
    public void setFileBucket(List<FileBucket> fileBucket){
        this.fileBucket = fileBucket;
    }

}
