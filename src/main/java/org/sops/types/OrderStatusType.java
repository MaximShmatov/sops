package org.sops.types;

import lombok.Getter;

@Getter
public enum OrderStatusType {
    READY("ready"),
    IN_PROCESS("inProgress"),
    PROCESSED("processed");

    private final String alias;

    OrderStatusType(String alias) {
        this.alias = alias;
    }

    public static OrderStatusType of(String orderStatusAlias) {
        for (OrderStatusType value : values()) {
            if (value.alias.equals(orderStatusAlias.trim())) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum constant of alias: " + orderStatusAlias);
    }

}
