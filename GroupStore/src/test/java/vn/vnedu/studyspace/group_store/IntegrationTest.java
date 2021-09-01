package vn.vnedu.studyspace.group_store;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import vn.vnedu.studyspace.group_store.GroupStoreApp;
import vn.vnedu.studyspace.group_store.config.TestSecurityConfiguration;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { GroupStoreApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
