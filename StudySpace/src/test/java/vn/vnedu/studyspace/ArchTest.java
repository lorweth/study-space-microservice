package vn.vnedu.studyspace;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("vn.vnedu.studyspace");

        noClasses()
            .that()
            .resideInAnyPackage("vn.vnedu.studyspace.service..")
            .or()
            .resideInAnyPackage("vn.vnedu.studyspace.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..vn.vnedu.studyspace.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
