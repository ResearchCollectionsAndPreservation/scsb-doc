package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.springframework.beans.factory.annotation.Value;

public class S3DeAccessionSummaryReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3DeAccessionSummaryReportRouteBuilder s3DeAccessionSummaryReportRouteBuilder;



    @Mock
    CamelContext context;


    public void S3DeAccessionSummaryReportRouteBuilder() throws Exception{
            boolean addS3RoutesOnStartup = true;
            String deaccessionPathS3 = "test";
}
    }

