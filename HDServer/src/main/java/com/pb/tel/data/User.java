package com.pb.tel.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.pb.tel.data.custom.type.TypeArrayToSet;
import com.pb.tel.data.custom.type.TypeArrayToUser;
import com.pb.tel.data.custom.type.TypeRoles;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * Created by vladimir on 26.07.19.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Entity
@Table(name = "operators")
@TypeDefs({@TypeDef(typeClass = TypeArrayToUser.class, name = TypeArrayToUser.NAME),
           @TypeDef(typeClass = TypeArrayToSet.class, name = TypeArrayToSet.NAME),
           @TypeDef(typeClass = TypeRoles.class, name = TypeRoles.NAME)})

@DynamicUpdate
public class User implements Serializable, UserDetails {

    public User(){}

    public User(String login){
        this.login = login;
    }

    @Id
    @Column(name = "login", nullable=false)
    private String login;

    @Column(name = "password_digest", nullable=false)
    private String password;

    @Column(name = "full_name", nullable=true)
    private String fullName;

    @Column(name = "description", nullable=true)
    private String description;

    @Column(name="authorities")
    @Type(type = TypeRoles.NAME)
    @JsonIgnore
    private Collection<Roles> roles;

    @Column(name = "friends", nullable=true)
    @Type(type = TypeArrayToUser.NAME)
    private Set<User> friends;

    @Column(name = "create_time", nullable=true)
    @JsonIgnore
    private Long createTime;

    @Column(name = "blocked", nullable=true)
    @JsonIgnore
    private Boolean blocked;


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return Roles.convertRolesToAuthority(roles);
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword(){ return this.password; }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Roles> roles) {
        this.roles = roles;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) return false;
        if (description != null ? !description.equals(user.description) : user.description != null) return false;
        if (roles != null ? !roles.equals(user.roles) : user.roles != null) return false;
        if (friends != null ? !friends.equals(user.friends) : user.friends != null) return false;
        if (createTime != null ? !createTime.equals(user.createTime) : user.createTime != null) return false;
        return blocked != null ? blocked.equals(user.blocked) : user.blocked == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (roles != null ? roles.hashCode() : 0);
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (blocked != null ? blocked.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", description='" + description + '\'' +
                ", roles=" + roles +
                ", friends=" + friends +
                ", createTime=" + createTime +
                ", blocked=" + blocked +
                '}';
    }
}
