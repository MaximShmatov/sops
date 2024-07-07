package org.sops.types;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum RoleType {
    POSTER("poster"),
    PROCESSOR("processor");

    private final String alias;

    RoleType(String alias) {
        this.alias = alias;
    }

    public static RoleType of(String roleAlias) {
        return Arrays.stream(values())
                .filter(value -> value.alias.equals(roleAlias.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No enum constant of alias: " + roleAlias));
    }

}
