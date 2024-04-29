package cz.ff.jsframework;

import cz.ff.jsframework.api.model.JsFrameworkCreateUpdateDTO;
import cz.ff.jsframework.api.model.JsFrameworkViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class JsFrameworkService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JsFrameworkRepository jsFrameworkRepository;

    private final JsFrameworkMapper jsFrameworkMapper;

    public JsFrameworkService(JsFrameworkRepository jsFrameworkRepository, JsFrameworkMapper jsFrameworkMapper) {
        this.jsFrameworkRepository = jsFrameworkRepository;
        this.jsFrameworkMapper = jsFrameworkMapper;
    }

    public Optional<JsFrameworkViewDTO> getJsFrameworkView(String name, String version) {
        return jsFrameworkRepository
                .findViewByNameIgnoreCaseAndVersionIgnoreCase(name, version)
                .map(jsFrameworkMapper::map);
    }

    public List<JsFrameworkViewDTO> findBy(Pageable pageable, String nameLike
            , String versionLike
            , LocalDate deprecationDateMin
            , LocalDate deprecationDateMax
            , Integer hypeLevelMin
            , Integer hypeLevelMax
    ) {
        return jsFrameworkMapper.map(jsFrameworkRepository
                .searchByProvidedCriteria(nameLike, versionLike, deprecationDateMin, deprecationDateMax, hypeLevelMin, hypeLevelMax, pageable));
    }

    public enum UpsertResult {
        CREATED, UPDATED
    }

    @Transactional
    public UpsertResult createOrUpdateJsFramework(String name, String version, JsFrameworkCreateUpdateDTO jsFrameworkCreateUpdateDTO) {
        Optional<JsFrameworkEntity> jsFramework = jsFrameworkRepository.findByNameIgnoreCaseAndVersionIgnoreCase(name, version);

        if (jsFramework.isPresent()) {
            LOG.info("Updating JS Framework: {} with version: {}", jsFramework.get().getName(), jsFramework.get().getVersion());
            jsFramework.get().setDeprecationDate(jsFrameworkCreateUpdateDTO.getDeprecationDate());
            jsFramework.get().setHypeLevel(jsFrameworkCreateUpdateDTO.getHypeLevel());

            return UpsertResult.UPDATED;
        } else {
            LOG.info("Creating JS Framework: {} with version: {}", name, version);
            JsFrameworkEntity jsFrameworkNew = new JsFrameworkEntity(name, version, jsFrameworkCreateUpdateDTO.getDeprecationDate(), jsFrameworkCreateUpdateDTO.getHypeLevel());
            jsFrameworkRepository.save(jsFrameworkNew);

            return UpsertResult.CREATED;
        }
    }

    public boolean delete(String name, String version) {
        int numberOfDeleted = jsFrameworkRepository.deleteByNameIgnoreCaseAndVersionIgnoreCase(name, version);
        if (numberOfDeleted > 0) {
            LOG.info("JS Framework: {} with version: {} was successfully deleted.", name, version);
            return true;
        } else {
            LOG.warn("Requested JS Framework: {} with version: {} to delete doesn't exist.", name, version);
            return false;
        }
    }
}
