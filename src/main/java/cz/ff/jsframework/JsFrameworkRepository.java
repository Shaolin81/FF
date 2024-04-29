package cz.ff.jsframework;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface JsFrameworkRepository extends ListCrudRepository<JsFrameworkEntity, Long> {

    /**
     * String parameters can't contain reserved characters for SQL LIKE operator (%, _).
     * @param nameLike
     * @param versionLike
     * @param deprecationDateMin
     * @param deprecationDateMax
     * @param hypeLevelMin
     * @param hypeLevelMax
     * @return found records
     */
    @Query("""
        SELECT new cz.ff.jsframework.JsFrameworkView(
            f.name,
            f.version,
            f.deprecationDate,
            f.hypeLevel
        )
        FROM JsFrameworkEntity f
        WHERE (:nameLike IS NULL OR LOWER(f.name) LIKE CONCAT('%', LOWER(:nameLike), '%'))
            AND (:versionLike IS NULL OR LOWER(f.version) LIKE CONCAT('%', LOWER(:versionLike), '%'))
            AND (:deprecationDateMin IS NULL OR f.deprecationDate >= :deprecationDateMin)
            AND (:deprecationDateMax IS NULL OR f.deprecationDate <= :deprecationDateMax)
            AND (:hypeLevelMin IS NULL OR f.hypeLevel >= :hypeLevelMin)
            AND (:hypeLevelMax IS NULL OR f.hypeLevel <= :hypeLevelMax)
        ORDER BY f.name ASC, f.version ASC
    """)
    List<JsFrameworkView> searchByProvidedCriteria(
            String nameLike, String versionLike, LocalDate deprecationDateMin, LocalDate deprecationDateMax, Integer hypeLevelMin, Integer hypeLevelMax, Pageable pageable);

    Optional<JsFrameworkEntity> findByNameIgnoreCaseAndVersionIgnoreCase(String name, String version);

    Optional<JsFrameworkView> findViewByNameIgnoreCaseAndVersionIgnoreCase(String name, String version);

    /**
     * Deletes record by a given name
     * @param name js framework name
     * @param version js framework version
     * @return number of deleted records in database
     */
    @Transactional
    int deleteByNameIgnoreCaseAndVersionIgnoreCase(String name, String version);
}
