package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

public class S3MatchingReportsRouteBuilderUT extends BaseTestCaseUT {

    @Mock
    S3MatchingReportsRouteBuilder reportsRouteBuilder;

    @Mock
    CamelContext camelContext;

    @Mock
    ApplicationContext applicationContext;


    @Test
    public void S3MatchingReportsRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String matchingReportsDirectory = "daily.reconciliation.file";
        String s3MatchingReportsDirectory = "solr.configsets.dir";

    }
}
