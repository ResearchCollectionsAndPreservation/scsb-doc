package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class S3SubmitCollectionSuccessReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3SubmitCollectionSuccessReportRouteBuilder s3SubmitCollectionSuccessReportRouteBuilder;

    @Mock
    CamelContext camelContext;


    @Test
    public void S3SubmitCollectionSuccessReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String submitCollectionS3ReportPath = "solr.configsets.dir";

    }
}
