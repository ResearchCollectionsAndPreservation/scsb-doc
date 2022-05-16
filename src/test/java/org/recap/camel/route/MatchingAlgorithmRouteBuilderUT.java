package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.camel.processor.ReportProcessor;
import zipkin2.Call;

public class MatchingAlgorithmRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    MatchingAlgorithmRouteBuilder matchingAlgorithmRouteBuilder;

    @Mock
    CamelContext context;




    @Test
    public void MatchingAlgorithmRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}
