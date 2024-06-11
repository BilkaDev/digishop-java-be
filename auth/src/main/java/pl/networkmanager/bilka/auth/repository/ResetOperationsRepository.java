package pl.networkmanager.bilka.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.networkmanager.bilka.auth.entity.ResetOperations;
import pl.networkmanager.bilka.auth.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface ResetOperationsRepository extends JpaRepository<ResetOperations, Long> {

    @Modifying
    void deleteAllByUser(User user);

    Optional<ResetOperations> findByUid(String uid);

    @Query(nativeQuery = true, value = "SELECT * FROM resetoperations where createdate <= current_timestamp - INTERVAL '15 minutes'")
    List<ResetOperations> findExpiredOperations();

}
