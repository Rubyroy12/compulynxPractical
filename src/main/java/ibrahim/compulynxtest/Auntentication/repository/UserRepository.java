package ibrahim.compulynxtest.Auntentication.repository;
import ibrahim.compulynxtest.Auntentication.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    // Since email is unique, we'll find users by email
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByPhone(String phone);
}
