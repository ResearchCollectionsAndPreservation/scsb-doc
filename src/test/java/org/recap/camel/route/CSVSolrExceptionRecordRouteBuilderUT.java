package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;
import org.recap.PropertyKeyConstants;
import org.springframework.beans.factory.annotation.Value;

public class CSVSolrExceptionRecordRouteBuilderUT extends BaseTestCaseUT {

    @InjectMocks
    CSVSolrExceptionRecordRouteBuilder csvSolrExceptionRecordRouteBuilder;

    @Mock
    CamelContext context;


    @Test
    public void CSVSolrExceptionRecordRouteBuilder()  throws  Exception {
        String solrReportsDirectory = "test";

    }


     public void configure() throws Exception
     {
         
      }


}