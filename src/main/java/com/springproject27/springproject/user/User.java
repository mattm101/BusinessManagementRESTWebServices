package com.springproject27.springproject.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springproject27.springproject.permission.Permission;
import com.springproject27.springproject.role.Role;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, message = "Nazwa uzytkownika być dłuższa niż 3 znaki")
    @Column(unique = true)
    private String username;

    @NotNull
    @NotEmpty(message = "Wpisz hasło")
    @Size(min = 8, message = "Hasło musi być dłuższe niż 8 znaków")
    @JsonIgnore
    private String password;

    @NotNull
    @Size(min=2, max=30, message = "Imię musi być dłuższe niż 2 a krótsze niż 30 znaków")
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min=2, max=50, message = "Nazwisko musi być dłuższe niż 2 a krótsze niż 50 znaków")
    @Column(name = "last_name")
    private String lastName;

    @Size(min=9, max=20, message = "Za krótki numer")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @Column(unique = true)
    @NotEmpty(message = "Prosze wprowadzić adres email")
    @Size(min=5, max=50, message = "Nie poprawny adres e-mail")
    private String email;

    @CreatedDate
    @Column(name = "creation_date")
    private Date creationDate;

    @LastModifiedDate
    @Column(name = "last_accessed_date")
    private Date lastAccessedDate;

    private boolean enabled;

    @Column(name = "token_expired")
    private boolean tokenExpired;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}
    )
    private Set<Role> roles;

    @Transient
    public Set<Permission> getPermission(){
        Set<Permission> permissions= new HashSet<>();
        for (Role role:roles) {
            permissions.addAll(role.getPermissions());
        }
        return permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(roles);
        authorities.addAll(getPermission());
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
