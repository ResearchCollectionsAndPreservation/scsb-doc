package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.springframework.beans.factory.annotation.Value;

public class FSDeAccessionSummaryReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSDeAccessionSummaryReportRouteBuilder fsDeAccessionSummaryReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSDeAccessionSummaryReportRouteBuilder () throws Exception
    {
        String reportsDirectory = "test";
    }
}
