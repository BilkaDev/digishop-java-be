package pl.networkmanager.bilka.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.networkmanager.bilka.auth.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
