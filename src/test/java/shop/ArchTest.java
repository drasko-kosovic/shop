package shop;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("shop");

        noClasses()
            .that()
                .resideInAnyPackage("shop.service..")
            .or()
                .resideInAnyPackage("shop.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..shop.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
