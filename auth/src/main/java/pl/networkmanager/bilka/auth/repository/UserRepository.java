package pl.networkmanager.bilka.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.networkmanager.bilka.auth.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findUserByLogin(String login);

    @Query(nativeQuery = true, value = "SELECT * FROM users where login=?1 and islock=false and isenabled=true")
    Optional<User> findUserByLoginAndLockAndEnabled(String login);

    Optional<User> findUserByEmail(String email);
}
