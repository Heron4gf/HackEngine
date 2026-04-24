package it.unicam.ids2026.persistence;

import it.unicam.ids2026.core.roles.User;

import java.util.UUID;

public interface UserRepository extends DomainRepository<User, UUID> {
}
