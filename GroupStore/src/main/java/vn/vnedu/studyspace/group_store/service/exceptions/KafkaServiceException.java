package vn.vnedu.studyspace.group_store.service.exceptions;

public class KafkaServiceException extends RuntimeException {

    public KafkaServiceException(Throwable e) {
        super(e);
    }
}
