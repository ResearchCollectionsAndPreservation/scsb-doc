package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class FSSubmitCollectionFailureReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSSubmitCollectionFailureReportRouteBuilder fsSubmitCollectionFailureReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSSubmitCollectionFailureReportRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}


