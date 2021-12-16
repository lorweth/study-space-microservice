package vn.vnedu.studyspace.answer_store.service.mapper;

import org.mapstruct.*;
import vn.vnedu.studyspace.answer_store.domain.TimeTable;
import vn.vnedu.studyspace.answer_store.service.dto.TimeTableDTO;

/**
 * Mapper for the entity {@link TimeTable} and its DTO {@link TimeTableDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TimeTableMapper extends EntityMapper<TimeTableDTO, TimeTable> {}
