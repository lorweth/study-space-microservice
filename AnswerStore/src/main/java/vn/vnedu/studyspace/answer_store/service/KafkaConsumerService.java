package vn.vnedu.studyspace.answer_store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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

    private static final String GROUP_USER_CREATE_TOPIC = "GROUP_STORE.GROUP_USER.SAVE"; //<application name>.<dataset name>.<event>

    private final KafkaProperties kafkaProperties;

    private KafkaReceiver<String, String> kafkaReceiver;

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
        ReceiverOptions<String, String> receiverOptions = ReceiverOptions.<String, String>create(consumerProps)
            .subscription(Collections.singletonList(GROUP_USER_CREATE_TOPIC));
        this.kafkaReceiver = KafkaReceiver.create(receiverOptions);
        consumeGroupMember().subscribe();
    }

    public Flux<GroupMemberDTO> consumeGroupMember() {
        log.debug("consumer group member....");
        return this.kafkaReceiver
            .receive()
            .map(ConsumerRecord::value)
            .flatMap(
                record -> {
                    try {
                        GroupMemberDTO groupMemberDTO = objectMapper.readValue(record, GroupMemberDTO.class);
                        return groupMemberService
                            .existById(groupMemberDTO.getId())
                            .flatMap(exists -> {
                                if(exists)
                                    return groupMemberService.save(groupMemberDTO);
                                return groupMemberService.insert(groupMemberDTO);
                            });
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
