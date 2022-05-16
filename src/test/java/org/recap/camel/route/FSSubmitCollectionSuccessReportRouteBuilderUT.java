package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.controller.BaseControllerUT;

public class FSSubmitCollectionSuccessReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSSubmitCollectionSuccessReportRouteBuilder fsSubmitCollectionSuccessReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSSubmitCollectionSuccessReportRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}
