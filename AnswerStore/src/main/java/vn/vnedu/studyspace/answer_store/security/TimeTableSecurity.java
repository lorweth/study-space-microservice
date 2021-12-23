package vn.vnedu.studyspace.answer_store.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.repository.TimeTableRepository;
import vn.vnedu.studyspace.answer_store.web.rest.errors.BadRequestAlertException;

import java.util.Objects;

@Component
public class TimeTableSecurity {

    private final Logger log = LoggerFactory.getLogger(TimeTableSecurity.class);

    private final TimeTableRepository timeTableRepository;

    public TimeTableSecurity(TimeTableRepository timeTableRepository) {
        this.timeTableRepository = timeTableRepository;
    }

    public Boolean hasPermission(Long timeTableId) {
        log.debug("Check permission to call request");
        return timeTableRepository
            .findById(timeTableId)
            .switchIfEmpty(Mono.error(new BadRequestAlertException("Entity not found", "AnswerStore", "entityNotFound")))
            .flatMap(
                timeTable -> SecurityUtils
                    .getCurrentUserLogin()
                    .map(userLogin -> Objects.equals(timeTable.getUserLogin(), userLogin))
            )
            .block();
    }

}
