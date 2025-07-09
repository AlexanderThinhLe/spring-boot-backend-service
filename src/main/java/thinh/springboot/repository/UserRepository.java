package thinh.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import thinh.springboot.model.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
} 