package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class FSSubmitCollectionExceptionReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSSubmitCollectionExceptionReportRouteBuilder  fsSubmitCollectionExceptionReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSOngoingAccessionReportRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}

