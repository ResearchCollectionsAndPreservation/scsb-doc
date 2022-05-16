package org.recap.camel.route;


import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;


public class S3AccessionSummaryReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3AccessionSummaryReportRouteBuilder s3AccessionSummaryReportRouteBuilder;

    @Mock
    CamelContext context;



    @Test
    public void  S3AccessionSummaryReportRouteBuilder()
    {
        boolean addS3RoutesOnStartup = true;
        String accessionPathS = "test";
    }
}
