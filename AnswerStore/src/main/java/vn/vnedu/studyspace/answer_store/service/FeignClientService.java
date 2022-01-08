package vn.vnedu.studyspace.answer_store.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "examstore")
public interface FeignClientService {

    @GetMapping("/api/questions/test-feign")
    Mono<String> demo(@RequestHeader("Authorization") String authorize);
}
