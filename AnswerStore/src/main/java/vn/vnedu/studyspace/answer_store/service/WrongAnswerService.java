package vn.vnedu.studyspace.answer_store.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheet;
import vn.vnedu.studyspace.answer_store.domain.AnswerSheetItem;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetItemRepository;
import vn.vnedu.studyspace.answer_store.repository.AnswerSheetRepository;
import vn.vnedu.studyspace.answer_store.service.dto.AnswerDTO;
import vn.vnedu.studyspace.answer_store.service.dto.SummaryResultDTO;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class WrongAnswerService {

    private final Logger log = LoggerFactory.getLogger(WrongAnswerService.class);

    @Autowired
    private FeignClientService feignClientService;

    private final AnswerSheetRepository answerSheetRepository;

    private final AnswerSheetItemRepository answerSheetItemRepository;

    public WrongAnswerService(AnswerSheetItemRepository answerSheetItemRepository, AnswerSheetRepository answerSheetRepository) {
        this.answerSheetItemRepository = answerSheetItemRepository;
        this.answerSheetRepository = answerSheetRepository;
    }

    /**
     * Find wrong answer of the sheet.
     *
     * @param sheetId the id of the sheet.
     * @param authorization the authorization to call request from other server.
     * @return the list of entity.
     */
    @Transactional(readOnly = true)
    public Flux<AnswerDTO> findWrongAnswer(Long sheetId, String authorization) {
        log.debug("Request to get all wrong answer of the answer sheet {}", sheetId);
        return answerSheetItemRepository
            .findByAnswerSheet(sheetId)
            .map(AnswerSheetItem::getQuestionId)
            .collectList()
            .flatMap(questionIdList -> feignClientService.getCorrectAnswer(authorization, questionIdList))
            .flatMap(correctAnswerList ->
                answerSheetItemRepository
                    .findByAnswerSheet(sheetId)
                    .collectList()
                    .flatMap(userOptionList -> {
                        List<Long> storedQuestionIdList = new LinkedList<>();
                        List<AnswerDTO> wrongOptionList = new LinkedList<>();
                        for(AnswerSheetItem userOption: userOptionList){
                            for(AnswerDTO correct: correctAnswerList){
                                if(
                                    Objects.equals(userOption.getQuestionId(), correct.getQuestionId()) // same question
                                        && !Objects.equals(userOption.getAnswerId(), correct.getAnswerId()) // different answer
                                        && !storedQuestionIdList.contains(userOption.getQuestionId())
                                ){
                                    storedQuestionIdList.add(userOption.getQuestionId()); // store questionId checked

                                    AnswerDTO wrongAnswer = new AnswerDTO();
                                    wrongAnswer.setQuestionId(correct.getQuestionId());
                                    wrongAnswer.setAnswerId(correct.getAnswerId());
                                    wrongAnswer.setQuestionContent(correct.getQuestionContent());
                                    wrongAnswer.setAnswerContent(correct.getAnswerContent());
                                    wrongOptionList.add(wrongAnswer);
                                }
                            }
                        }

                        return Mono.just(wrongOptionList);
                    })

            )
            .flatMapMany(Flux::fromIterable);
    }

    @Transactional
    public Flux<SummaryResultDTO> getAllSummaryResultOfExam(Long examId, String currentUserLogin, String authorization) {
        log.debug("Get summary result of the exam: {}", examId);
        return answerSheetRepository
            .findAllByExamIdAndUserLogin(examId, currentUserLogin)
            .flatMap(answerSheet -> answerSheetItemRepository
                .findByAnswerSheet(answerSheet.getId())
                .map(AnswerSheetItem::getQuestionId)
                .collectList()
                .flatMap(questionIdList -> feignClientService.getCorrectAnswer(authorization, questionIdList))
                .flatMap(correctAnswerList ->
                    answerSheetItemRepository
                        .findByAnswerSheet(answerSheet.getId())
                        .collectList()
                        .flatMap(userOptionList -> {
                            List<Long> storedQuestionIdList = new LinkedList<>();
                            int wrongAnswerNumber = 0;
                            for(AnswerSheetItem userOption: userOptionList){
                                for(AnswerDTO correct: correctAnswerList){
                                    if(
                                        Objects.equals(userOption.getQuestionId(), correct.getQuestionId()) // same question
                                            && !Objects.equals(userOption.getAnswerId(), correct.getAnswerId()) // different answer
                                            && !storedQuestionIdList.contains(userOption.getQuestionId())
                                    ){
                                        storedQuestionIdList.add(userOption.getQuestionId()); // store questionId checked
                                        wrongAnswerNumber++;
                                    }
                                }
                            }
                            SummaryResultDTO newSummary = new SummaryResultDTO();
                            newSummary.setSheetId(answerSheet.getId());
                            newSummary.setWrongAnswerCount(wrongAnswerNumber);
                            newSummary.setTime(answerSheet.getCreatedAt());

                            return Mono.just(newSummary);
                        })

                )
            );

    }
}
