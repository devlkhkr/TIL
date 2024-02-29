package tdd.junit.testing.cmsn.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class CmsnServiceTest {
    private CmsnService cmsnService = new CmsnService();
    @Test
    public void calcCmsn() {
        int calcTestResult = cmsnService.calcCmsn(2,3);

        assertThat(calcTestResult)
                .isPositive()
                .isGreaterThanOrEqualTo(3)
                .isLessThan(10);
    }
}
