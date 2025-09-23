package ru.gl.docs.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gl.docs.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query(value = "SELECT * FROM Users u WHERE u.passport_number = :passportNumber", nativeQuery = true)
    Users findByPassport(@Param("passportNumber") String passportNumber);
    Page<Users> findByPassportContaining(String passportNumber, Pageable pageable);
    boolean existsByPassport(String passportNumber);
}
