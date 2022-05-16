package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class FSAccessionSummaryReportRouteBuilderUT extends BaseTestCaseUT
{

        @InjectMocks
        FSAccessionSummaryReportRouteBuilder fsAccessionSummaryReportRouteBuilder;

        @Mock
        CamelContext context;


        @Test
        public void FSAccessionSummaryReportRouteBuilder()  throws  Exception {
            String reportsDirectory = "scsb.collection.report.directory";
        }


    }
