package com.app.backend.entity;

import com.app.backend.entity.baseEntity.BaseEntity;
import com.app.backend.entity.enumm.Status;
import com.app.backend.exception.CommonException;
import com.app.backend.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "email", unique = true)
    private String email;
    @Column(nullable = false, name = "username", unique = true)
    private String username;


    @Column(nullable = false, name = "pass")
    private String password;

    @Column(nullable = false, name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false, name = "incident_count")
    private Integer incidentCount=0;

    @ManyToOne
    @JoinColumn(name = "manager_group_id")
    private ManagerGroup managerGroup;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles;

    @OneToMany(mappedBy = "userUsing", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingUsing;

    @OneToMany(mappedBy = "userApproved", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookingsApproved;


    @OneToMany(mappedBy = "incidentBy", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DeviceIncident> incidentList;


    public void validStatus(){
        if (getStatus() == Status.INACTIVE){
            throw new CommonException(ErrorCode.AUTH_ACCOUNT_INACTIVE);
        }

        if (getStatus() == Status.DISABLED){
            throw new CommonException(ErrorCode.AUTH_ACCOUNT_DISABLED);
        }

        if (getStatus() == Status.LOCKED) {
            throw new CommonException(ErrorCode.AUTH_ACCOUNT_LOCKED);
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !status.equals(Status.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !status.equals(Status.INACTIVE);
    }
}
