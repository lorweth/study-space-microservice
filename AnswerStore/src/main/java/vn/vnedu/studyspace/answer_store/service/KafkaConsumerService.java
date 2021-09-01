package vn.vnedu.studyspace.answer_store.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import vn.vnedu.studyspace.answer_store.config.KafkaProperties;
import vn.vnedu.studyspace.answer_store.service.dto.GroupMemberDTO;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;


@Service
public class KafkaConsumerService {

    private final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    private static final String GROUP_USER_CREATE_TOPIC = "GROUP_STORE.GROUP_USER.SAVE"; //<application name>.<dataset name>.<event>

    private final AtomicBoolean closed = new AtomicBoolean(false);

    private final KafkaProperties kafkaProperties;

    private KafkaConsumer<String, String> kafkaConsumer;

    private ObjectMapper objectMapper = new ObjectMapper();

    private GroupMemberService groupMemberService;

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public KafkaConsumerService(KafkaProperties kafkaProperties, GroupMemberService groupMemberService) {
        this.kafkaProperties = kafkaProperties;
        this.groupMemberService = groupMemberService;
    }

    @PostConstruct
    public void start() {
        log.info("Kafka consumer starting...");
        this.kafkaConsumer = new KafkaConsumer<>(kafkaProperties.getConsumerProps());
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        executorService.execute(() -> {
            try {
                while(!closed.get()) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(3));
                    for (ConsumerRecord<String, String> record : records) {
                        log.info("Consumed message in {} : {}", GROUP_USER_CREATE_TOPIC, record);

                        GroupMemberDTO groupMemberDTO = objectMapper.readValue(record.value(), GroupMemberDTO.class);
                        groupMemberDTO = groupMemberService.save(groupMemberDTO);
                    }
                }
                kafkaConsumer.commitSync();
            }catch (WakeupException e) {
                if(!closed.get()) throw e;
            }
            catch(Exception e) {
                log.info(e.getMessage(), e);
            }finally {
                log.info("Kafka consumer close");
                kafkaConsumer.close();
            }
        });


    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return kafkaConsumer;
    }

    public void shutdown() {
        log.info("Shutdown kafka consumer");
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
