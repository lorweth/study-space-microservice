package vn.vnedu.studyspace.answer_store.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import vn.vnedu.studyspace.answer_store.config.KafkaProperties;
import vn.vnedu.studyspace.answer_store.service.dto.GroupMemberDTO;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Map;

@Service
public class KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private static final String CREATE_TOPIC = "GROUP_STORE.GROUP_USER.SAVE"; //<application name>.<dataset name>.<event>
    private static final String DELETE_TOPIC = "GROUP_STORE.GROUP_USER.DELETE";

    private final KafkaProperties kafkaProperties;

    private KafkaReceiver<String, String> kafkaCreateReceiver;
    private KafkaReceiver<String, String> kafkaDeleteReceiver;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final GroupMemberService groupMemberService;

    public KafkaConsumerService(KafkaProperties kafkaProperties, GroupMemberService groupMemberService) {
        this.kafkaProperties = kafkaProperties;
        this.groupMemberService = groupMemberService;
    }

    @PostConstruct
    public void start() {
        log.info("Kafka consumer starting...");
        Map<String, Object> consumerProps = kafkaProperties.getConsumerProps();
        this.kafkaCreateReceiver = KafkaReceiver.create(
            ReceiverOptions.<String, String>create(consumerProps).subscription(Collections.singletonList(CREATE_TOPIC))
        );
        this.kafkaDeleteReceiver = KafkaReceiver.create(
            ReceiverOptions.<String, String>create(consumerProps).subscription(Collections.singletonList(DELETE_TOPIC))
        );
        listenSaveMessageFromGroupMember().subscribe();
        listenDeleteMessageFromGroupMember().subscribe();
    }

    public Flux<GroupMemberDTO> listenSaveMessageFromGroupMember() {
        log.debug("consumer group member....");
        return kafkaCreateReceiver
            .receive()
            .map(ConsumerRecord::value)
            .map(
                value -> {
                    try{
                        return objectMapper.readValue(value, GroupMemberDTO.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            )
            .flatMap(
                dto -> {
                    return groupMemberService
                        .existById(dto.getId())
                        .flatMap(
                            exists -> {
                                if(Boolean.TRUE.equals(exists))
                                    return groupMemberService.save(dto);
                                else
                                    return groupMemberService.insert(dto);
                            }
                        );
                }
            );
    }

    public Flux<Void> listenDeleteMessageFromGroupMember() {
        log.info("Consumer delete groupMember");
        return kafkaDeleteReceiver
            .receive()
            .map(ConsumerRecord::value)
            .flatMap(
                value -> {
                    try {
                        Long groupMemberId = objectMapper.readValue(value, Long.class);
                        return groupMemberService.delete(groupMemberId);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
            );
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown kafka consumer");
    }
}
