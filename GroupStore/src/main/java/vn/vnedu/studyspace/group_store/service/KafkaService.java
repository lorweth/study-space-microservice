package vn.vnedu.studyspace.group_store.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import vn.vnedu.studyspace.group_store.config.KafkaProperties;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberDTO;
import vn.vnedu.studyspace.group_store.service.dto.GroupMemberKafkaDTO;
import vn.vnedu.studyspace.group_store.service.errors.KafkaServiceException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class KafkaService {

    private final Logger log = LoggerFactory.getLogger(KafkaService.class);

    public static final String CREATE_TOPIC = "GROUP_STORE.GROUP_USER.SAVE"; //<application name>.<dataset name>.<event>

    public static final String DELETE_TOPIC = "GROUP_STORE.GROUP_USER.DELETE";

    private final KafkaProperties kafkaProperties;

    private KafkaProducer<String, String> kafkaProducer;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaService(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    @PostConstruct
    public void initialize() {
        log.info("Kafka producer initializing...");
        this.kafkaProducer = new KafkaProducer<>(kafkaProperties.getProducerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("Kafka producer initialized");
    }

    public void storeGroupMember(GroupMemberDTO groupMemberDTO) {
        try {
            GroupMemberKafkaDTO groupMemberKafkaDTO = new GroupMemberKafkaDTO(groupMemberDTO);
            String message = objectMapper.writeValueAsString(groupMemberKafkaDTO);
            ProducerRecord<String, String> record = new ProducerRecord<>(CREATE_TOPIC, message);
            kafkaProducer.send(record);
        }
        catch (JsonProcessingException e) {
            log.error("Could not send group member data with error: ", e);
            throw new KafkaServiceException(e);
        }
    }

    public void deleteGroupMember(Long groupMemberId) {
        try {
            String message = objectMapper.writeValueAsString(groupMemberId);
            ProducerRecord<String, String> record = new ProducerRecord<>(DELETE_TOPIC, message);
            kafkaProducer.send(record);
        }catch (JsonProcessingException e) {
            log.error("Could not delete group member data with error: ", e);
            throw new KafkaServiceException(e);
        }
    }

    @PreDestroy
    public void shutdown() {
        log.info("Shutdown kafka producer");
        kafkaProducer.close();
    }

}
