package archunit;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "*", importOptions =  { ImportOption.DoNotIncludeTests.class })
public class ArchitectureTest {
    @ArchTest
    public static final ArchRule CYCLE_RULE = slices().matching("..(*)..").should().beFreeOfCycles();
}
