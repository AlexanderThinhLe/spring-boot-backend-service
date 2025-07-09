package thinh.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thinh.springboot.model.AddressEntity;

@Repository // @Repository is optional in here
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    
} 