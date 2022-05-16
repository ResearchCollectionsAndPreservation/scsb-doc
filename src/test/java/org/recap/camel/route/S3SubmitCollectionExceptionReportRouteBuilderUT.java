package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.apache.commons.compress.harmony.pack200.BandSet;
import org.junit.Test;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class S3SubmitCollectionExceptionReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3SubmitCollectionExceptionReportRouteBuilder s3SubmitCollectionExceptionReportRouteBuilder;

    @Mock
    CamelContext camelContext;


    @Test
    public void S3SubmitCollectionExceptionReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String matchingReportsDirectory = "daily.reconciliation.file";
        String submitCollectionS3ReportPath = "solr.configsets.dir";

    }
}


