package pl.networkmanager.bilka.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.networkmanager.bilka.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserByLogin(String login);

    Optional<User> findUserByUuid(String uid);

    Optional<User> findUserByEmail(String email);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true")
    Optional<User> findUserByLoginAndLockAndEnabled(String login);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true and role='ADMIN'")
    Optional<User> findUserByLoginAndLockAndEnabledAndIsAdmin(String login);
}
