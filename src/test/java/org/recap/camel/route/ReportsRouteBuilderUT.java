package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.camel.processor.ReportProcessor;

public class ReportsRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    ReportsRouteBuilder reportsRouteBuilder;

    @Mock
    CamelContext camelContext;

    @Mock
    ReportProcessor reportProcessor;

     @Test
    public void ReportsRouteBuilder() throws Exception {
     }





}
