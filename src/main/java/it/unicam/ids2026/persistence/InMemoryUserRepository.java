package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.roles.User;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class InMemoryUserRepository extends InMemoryRepository<User, UUID> implements UserRepository {

    public InMemoryUserRepository() {
        super(User::getId);
    }
}
