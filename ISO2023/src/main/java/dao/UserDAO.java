package dao;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import entities.User;


public interface UserDAO extends JpaRepository<User, String>{
	Optional<User> findByName(String name);
}
