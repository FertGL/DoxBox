package ru.gl.docs.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gl.docs.entity.Document;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findById(Long id);

    List<Document> findAllByOrderByDocumentNameAsc();

    @Query("SELECT d FROM Document d JOIN d.users u WHERE u.id = :userId")
    List<Document> findDocumentsByUserId(@Param("userId") Long userId);

    @Query("SELECT d.id FROM Document d JOIN d.users u WHERE u.id = :userId")
    List<Long> findDocumentIdsByUserId(@Param("userId") Long userId);
}
