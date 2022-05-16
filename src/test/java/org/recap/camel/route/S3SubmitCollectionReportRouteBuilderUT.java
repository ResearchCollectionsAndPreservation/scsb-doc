package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class S3SubmitCollectionReportRouteBuilderUT extends BaseTestCaseUT
{
    @Mock
    S3SubmitCollectionReportRouteBuilder  S3SubmitCollectionReportRouteBuilderUT;

    @Mock
    CamelContext camelContext;



    @Test
    public void S3SubmitCollectionReportRouteBuilder() throws Exception {
        boolean addS3RoutesOnStartu = true;
        String submitCollectionS3ReportPath = "solr.configsets.dir";

    }
}


