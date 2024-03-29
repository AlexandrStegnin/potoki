package com.art.model.supporting;

import com.art.model.AppUser;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@ToString(exclude = {"users", "fileBucket"})
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SendingMail implements Serializable {

    private List<AppUser> users;
    private String subject;
    private String body;
    private List<FileBucket> fileBucket;

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<FileBucket> getFileBucket() {
        return fileBucket;
    }

    public void setFileBucket(List<FileBucket> fileBucket) {
        this.fileBucket = fileBucket;
    }

}
