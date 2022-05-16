package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class FSSubmitCollectionRejectionReportRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    FSSubmitCollectionRejectionReportRouteBuilder fsSubmitCollectionRejectionReportRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void FSSubmitCollectionRejectionReportRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}
