package AVISANDROID;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = {
//        "src/test/resources/AVISANDROID/test.feature",
//        "src/test/resources/AVISANDROID/eta-fare.feature",
//        "src/test/resources/AVISANDROID/payment-book.feature",
        "src/test/resources/AVISANDROID/corporate-book-late.feature"
        })
public class RunCucumberTest {

}
