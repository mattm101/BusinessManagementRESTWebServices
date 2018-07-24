package com.springproject27.springproject.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springproject27.springproject.exception.EntityAlreadyExistsException;
import com.springproject27.springproject.permission.Permission;
import com.springproject27.springproject.role.Role;
import com.springproject27.springproject.vacation.Vacation;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = 3, message = "Username must be longer than 3")
    @Column(unique = true)
    private String username;

    @NotEmpty(message = "Provide password")
    @Size(min = 8, message = "Password must be longer than 8")
    @JsonIgnore
    private String password;

    @NotEmpty
    @Size(min = 2, max = 30, message = "First name must be between 2 and 30 letters.")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 letters.")
    @Column(name = "last_name")
    private String lastName;

    @Size(min = 9, max = 20, message = "Provided number must be between 9 and 20")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @NotEmpty(message = "Please provide email address")
    @Column(unique = true)
    @Size(min = 5, max = 50, message = "Incorrect email address")
    private String email;

    @CreatedDate
    @Column(name = "creation_date")
    private Date creationDate;

    @LastModifiedDate
    @Column(name = "last_accessed_date")
    private Date lastAccessedDate;

    @Column(name = "date_of_employment")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Builder.Default
    private Date dateOfEmployment = null;

    @Column(name = "token_expired")
    private boolean tokenExpired;

    @Column(name = "available_vacation_days")
    @Builder.Default
    private int availableVacationDays = 0;

    @Column(name = "vacation_days_for_user")
    @Builder.Default
    private int vacationDaysForUseInYear = 0;

    private boolean enabled;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "user")
    @JsonIgnoreProperties("user")
    @JsonManagedReference
    @Builder.Default
    private Set<Vacation> vacations = new HashSet<>();

    @Transient
    private Set<Permission> getPermission() {
        return roles.stream().flatMap(x -> x.getPermissions().stream())
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.addAll(roles);
        authorities.addAll(getPermission());
        return authorities;
    }

    @Override
    public String getUsername() {
        return username;
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

    private boolean isAvailableGoOnVacation(final Vacation vacation) {
        return vacationDaysForUseInYear > 0 || vacationDaysForUseInYear > vacation.getDifferenceBetweenTwoDates();
    }

    private int getAvailableVacationDays(Vacation vacation) {
        return Math.toIntExact(vacationDaysForUseInYear - vacation.getDifferenceBetweenTwoDates());
    }

    public User addVacation(@NotNull Vacation vacation) throws EntityAlreadyExistsException {
        if (vacations.contains(vacation) && !isAvailableGoOnVacation(vacation)) {
            throw new EntityAlreadyExistsException("User with id = " + id + " already has this vacation");
        }
        vacations.add(vacation);
        availableVacationDays = getAvailableVacationDays(vacation);
        return this;
    }
}
