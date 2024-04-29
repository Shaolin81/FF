package cz.ff.jsframework;

import cz.ff.jsframework.api.JsFrameworksApi;
import cz.ff.jsframework.api.model.JsFrameworkCreateUpdateDTO;
import cz.ff.jsframework.api.model.JsFrameworkViewDTO;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;

@RestController
public class JsFrameworkController implements JsFrameworksApi {
    // TODO MKL: handle 4xx,5xx errors

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JsFrameworkService jsFrameworkService;

    public JsFrameworkController(JsFrameworkService jsFrameworkService) {
        this.jsFrameworkService = jsFrameworkService;
    }

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException() {
    }

    @Override
    public ResponseEntity<List<JsFrameworkViewDTO>> getJsFrameworks(Pageable pageable, String nameLike, String versionLike, LocalDate deprecationDateMin
            , LocalDate deprecationDateMax, Integer hypeLevelMin, Integer hypeLevelMax) {
        // strings used for search can't contain % and _ (see searchByProvidedCriteria javadoc)
        // these characters are not allowed by JS Framework API, so we are safe
        return ResponseEntity.ok(
                jsFrameworkService.findBy(pageable, nameLike, versionLike, deprecationDateMin, deprecationDateMax, hypeLevelMin, hypeLevelMax)
        );
    }

    @Override
    public ResponseEntity<Void> createOrUpdateJsFramework(String name, String version, JsFrameworkCreateUpdateDTO jsFrameworkCreateUpdateDTO) {
        return switch (jsFrameworkService.createOrUpdateJsFramework(name, version, jsFrameworkCreateUpdateDTO)) {
            case UPDATED -> new ResponseEntity<>(HttpStatus.NO_CONTENT);
            case CREATED -> new ResponseEntity<>(HttpStatus.CREATED);
        };
    }

    @Override
    public ResponseEntity<JsFrameworkViewDTO> getJsFrameworkByNameAndVersion(String name, String version) {
        return jsFrameworkService.getJsFrameworkView(name, version)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteJsFrameworkByNameAndVersion(String name, String version) {
        if (jsFrameworkService.delete(name, version)) {
            LOG.info("JS Framework {} was successfully deleted.", name);
            return ResponseEntity.noContent().build();
        } else {
            LOG.warn("Requested JS Framework {} to delete doesn't exist.", name);
            return ResponseEntity.notFound().build();
        }
    }
}
