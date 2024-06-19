package com.CptFranck.SportsPeak.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "app_privilege")
public class PrivilegeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_id_seq")
    @SequenceGenerator(name = "privilege_id_seq", sequenceName = "privilege_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "name", unique = true, length = 50, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<RoleEntity> roles;
}
