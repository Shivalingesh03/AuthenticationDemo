package com.ssb.Entity;

import java.util.Set;

public enum Role {

    ADMIN(Set.of(Permissions.WRITE,Permissions.POST, Permissions.DELETE)),
    USER(Set.of(Permissions.READ));

    private final Set<Permissions> permissions;


    Role(Set<Permissions> permissions) {
        this.permissions = permissions;
    }
}
