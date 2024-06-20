package pl.networkmanager.bilka.ftpserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.networkmanager.bilka.ftpserver.entity.Image;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUuid(String uuid);

    @Query(nativeQuery = true, value = "SELECT * FROM image_data where createat < current_date - interval '2 days' and isused = false")
    List<Image> findDontUseImages();

}
