package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.springframework.context.ApplicationContext;

public class S3SolrExceptionRecordRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3SolrExceptionRecordRouteBuilder s3SolrExceptionRecordRouteBuilder;

    @Mock
    CamelContext camelContext;


    @Test
    public void S3OngoingAccessionReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String matchingReportsDirectory = "daily.reconciliation.file";
        String submitCollectionS3ReportPath = "solr.configsets.dir";

    }
}
