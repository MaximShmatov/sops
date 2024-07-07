package org.sops.database.entities;

import java.io.Serializable;

public interface BaseEntity <T extends Serializable> {
    T getId();
}
