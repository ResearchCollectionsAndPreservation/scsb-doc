package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;;
import org.recap.BaseTestCaseUT;

public class MatchingAlgorithmJobRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    MatchingAlgorithmJobRouteBuilder matchingAlgorithmJobRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void MatchingAlgorithmJobRouteBuilder() throws Exception
    {
        String reportsDirectory = "test";
    }
}
