package vn.vnedu.studyspace.group_store.service.errors;

public class KafkaServiceException extends RuntimeException {

    public KafkaServiceException(Throwable e) {
        super(e);
    }
}
