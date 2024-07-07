package org.sops;

import org.sops.database.entities.UserEntity;
import org.sops.types.RoleType;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {TestSopsApplication.class})
public class BaseTest {

    protected final String posterPassword = "$2a$10$e1GhWgjtZ6nrYjz8rx7Zuuoay64ezWNngHGmuY9i.toO7ZmI7dvNu";

    protected final String processorPassword = "$2a$10$XAuqsc9DIDg8r/BbG0TcpOcxm7x9c92i/zzAA0K./vb3hv2gBb6QC";

    protected final UserEntity posterEntity = new UserEntity(1, "User1", posterPassword, "email", RoleType.POSTER);

    protected final UserEntity processorEntity = new UserEntity(3, "User3", processorPassword, "email", RoleType.PROCESSOR);

}
