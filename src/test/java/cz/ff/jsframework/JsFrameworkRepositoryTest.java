package cz.ff.jsframework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class JsFrameworkRepositoryTest {

    @Autowired
    private JsFrameworkRepository jsFrameworkRepository;

    @BeforeEach
    void setupBeforeEach() {
        // insert testing data
        jsFrameworkRepository.save(new JsFrameworkEntity("Foo", "1.0.alpha", LocalDate.parse("2024-11-05"), 5));
    }

    @Nested
    class SearchByProvidedCriteriaTest {

        @Test
        void testSearchByCriteriaForNameLike() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    "oO", null, null, null, null, null, null);
            assertEquals(1, found.size());
        }

        @Test
        void testSearchByCriteriaForVersionLike() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    null, "1.0.ALPHA", null, null, null, null, null);
            assertEquals(1, found.size());
        }

        @Test
        void testSearchByCriteriaForDeprecationDateBetweenFound() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    null, null, LocalDate.parse("2024-11-05"), LocalDate.parse("2024-11-05"), null, null, null);
            assertEquals(1, found.size());
        }

        @Test
        void testSearchByCriteriaForDeprecationDateBetweenNotFound() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    null, null, LocalDate.parse("2024-11-04"), LocalDate.parse("2024-11-04"), null, null, null);
            assertEquals(0, found.size());
        }

        @Test
        void testSearchByCriteriaForHypeLevelFound() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    null, null, null, null, 5, 6, null);
            assertEquals(1, found.size());
        }

        @Test
        void testSearchByCriteriaForHypeLevelNotFound() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    null, null, null, null, 3, 4, null);
            assertEquals(0, found.size());
        }

        @Test
        void testSearchByAllCriteriaFound() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    "oo", "1.0", LocalDate.parse("2024-11-05"), LocalDate.parse("2024-11-05"), 4, 5, null);
            assertEquals(1, found.size());
        }

        @Test
        void testSearchByAllCriteriaAndOneNotMatch() {
            List<JsFrameworkView> found = jsFrameworkRepository.searchByProvidedCriteria(
                    "ba", "1.0", LocalDate.parse("2024-11-05"), LocalDate.parse("2024-11-05"), 4, 5, null);
            assertEquals(0, found.size());
        }
    }
}
