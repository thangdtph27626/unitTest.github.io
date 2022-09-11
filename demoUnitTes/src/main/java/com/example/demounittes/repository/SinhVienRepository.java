package com.example.demounittes.repository;

import com.example.demounittes.model.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author thangdt
 */
public interface SinhVienRepository extends JpaRepository<SinhVien, String> {

    @Query(value = """
            SELECT CASE WHEN COUNT(s.id) > 0 THEN\s
                        TRUE ELSE FALSE END\s
                        FROM sinh_vien s\s
                        WHERE s.email like :email
            """, nativeQuery = true)
    int selectExistsEmail(@Param("email") String email);
}
