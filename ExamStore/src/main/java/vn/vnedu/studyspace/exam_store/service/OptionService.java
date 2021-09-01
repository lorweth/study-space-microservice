package vn.vnedu.studyspace.exam_store.service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.Option;
import vn.vnedu.studyspace.exam_store.repository.OptionRepository;
import vn.vnedu.studyspace.exam_store.service.dto.OptionDTO;
import vn.vnedu.studyspace.exam_store.service.mapper.OptionMapper;

/**
 * Service Implementation for managing {@link Option}.
 */
@Service
@Transactional
public class OptionService {

    private final Logger log = LoggerFactory.getLogger(OptionService.class);

    private final OptionRepository optionRepository;

    private final OptionMapper optionMapper;

    public OptionService(OptionRepository optionRepository, OptionMapper optionMapper) {
        this.optionRepository = optionRepository;
        this.optionMapper = optionMapper;
    }

    /**
     * Save a option.
     *
     * @param optionDTO the entity to save.
     * @return the persisted entity.
     */
    public OptionDTO save(OptionDTO optionDTO) {
        log.debug("Request to save Option : {}", optionDTO);
        Option option = optionMapper.toEntity(optionDTO);
        option = optionRepository.save(option);
        return optionMapper.toDto(option);
    }

    /**
     * Partially update a option.
     *
     * @param optionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OptionDTO> partialUpdate(OptionDTO optionDTO) {
        log.debug("Request to partially update Option : {}", optionDTO);

        return optionRepository
            .findById(optionDTO.getId())
            .map(
                existingOption -> {
                    optionMapper.partialUpdate(existingOption, optionDTO);

                    return existingOption;
                }
            )
            .map(optionRepository::save)
            .map(optionMapper::toDto);
    }

    /**
     * Get all the options.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OptionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Options");
        return optionRepository.findAll(pageable).map(optionMapper::toDto);
    }

    /**
     * Get one option by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OptionDTO> findOne(Long id) {
        log.debug("Request to get Option : {}", id);
        return optionRepository.findById(id).map(optionMapper::toDto);
    }

    /**
     * Delete the option by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Option : {}", id);
        optionRepository.deleteById(id);
    }
}
