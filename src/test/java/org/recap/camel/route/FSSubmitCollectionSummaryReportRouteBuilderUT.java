package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class FSSubmitCollectionSummaryReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSSubmitCollectionSummaryReportRouteBuilder fsSubmitCollectionSummaryReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSSubmitCollectionSummaryReportRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}
