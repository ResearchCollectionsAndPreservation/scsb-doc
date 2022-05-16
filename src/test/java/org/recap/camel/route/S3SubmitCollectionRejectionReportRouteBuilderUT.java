package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class S3SubmitCollectionRejectionReportRouteBuilderUT extends BaseTestCaseUT
{
     @Mock
      S3SubmitCollectionRejectionReportRouteBuilder s3SubmitCollectionRejectionReportRouteBuilder;


    @Mock
    CamelContext camelContext;


    @Test
    public void S3SubmitCollectionFailureReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String matchingReportsDirectory = "daily.reconciliation.file";
        String submitCollectionS3ReportPath = "solr.configsets.dir";

    }
}
