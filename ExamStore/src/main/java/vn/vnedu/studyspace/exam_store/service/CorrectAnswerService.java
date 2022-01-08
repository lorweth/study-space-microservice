package vn.vnedu.studyspace.exam_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.vnedu.studyspace.exam_store.domain.Option;
import vn.vnedu.studyspace.exam_store.domain.Question;
import vn.vnedu.studyspace.exam_store.repository.OptionRepository;
import vn.vnedu.studyspace.exam_store.repository.QuestionRepository;
import vn.vnedu.studyspace.exam_store.service.dto.CorrectAnswerDTO;
import vn.vnedu.studyspace.exam_store.web.rest.errors.BadRequestAlertException;

import java.util.LinkedList;
import java.util.List;

@Service
public class CorrectAnswerService {
    private final Logger log = LoggerFactory.getLogger(CorrectAnswerService.class);

    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public CorrectAnswerService(QuestionRepository questionRepository, OptionRepository optionRepository){
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
    }

    /**
     * Get all Correct Option.
     *
     * @param questionIdList the list of questionId.
     * @return the list of CorrectAnswer.
     */
    @Transactional(readOnly = true)
    public List<CorrectAnswerDTO> getCorrectAnswer(List<Long> questionIdList) {
        log.debug("Request to get all correct answer of the exam");
        List<CorrectAnswerDTO> correctAnswerList = new LinkedList<>();

        for(Long questionId: questionIdList){
            if (!questionRepository.existsById(questionId)) {
                throw new BadRequestAlertException("Question not found", "CorrectAnswer", "questionNotFound");
            }

            CorrectAnswerDTO answer = new CorrectAnswerDTO();
            answer.setQuestionId(questionId);

            List<Option> optionOfQuestion = optionRepository.findAllByQuestionId(questionId);
            for(Option o: optionOfQuestion){
                if(Boolean.TRUE.equals(o.getIsCorrect())){
                    answer.setAnswerId(o.getId());
                }
            }
            correctAnswerList.add(answer);
        }
        return correctAnswerList;
    }

}
