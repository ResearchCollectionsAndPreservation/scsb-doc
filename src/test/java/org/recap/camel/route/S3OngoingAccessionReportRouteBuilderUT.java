package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.springframework.context.ApplicationContext;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public class S3OngoingAccessionReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3OngoingAccessionReportRouteBuilder s3OngoingAccessionReportRouteBuilder;

    @Mock
    CamelContext camelContext;

    @Mock
    ApplicationContext applicationContext;


    @Test
    public void S3OngoingAccessionReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String matchingReportsDirectory = "daily.reconciliation.file";
        String s3MatchingReportsDirectory = "solr.configsets.dir";

    }
}


