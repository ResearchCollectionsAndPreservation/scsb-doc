package org.recap.camel.route;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.apache.camel.model.dataformat.BindyType;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.csv.SubmitCollectionReportRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by hemalathas on 21/12/16.
 */
@Slf4j
@Component
public class S3SubmitCollectionRejectionReportRouteBuilder {

    /**
     * This method instantiates a new route builder to generate submit collection rejection report to the S3.
     *
     * @param context                    the context
     * @param submitCollectionS3ReportPath the submit Collection Report Path
     */
    @Autowired
    public S3SubmitCollectionRejectionReportRouteBuilder(CamelContext context, @Value("${" + PropertyKeyConstants.S3_ADD_S3_ROUTES_ON_STARTUP + "}") boolean addS3RoutesOnStartup, @Value("${" + PropertyKeyConstants.S3_SUBMIT_COLLECTION_SUPPORT_TEAM_REPORT_DIR + "}") String submitCollectionS3ReportPath) {
        try {
            if (addS3RoutesOnStartup) {
                context.addRoutes(new RouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        from(ScsbCommonConstants.FTP_SUBMIT_COLLECTION_REJECTION_REPORT_Q)
                                .routeId(ScsbConstants.FTP_SUBMIT_COLLECTION_REJECTION_REPORT_ID)
                                .marshal().bindy(BindyType.Csv, SubmitCollectionReportRecord.class)
                                .setHeader(S3Constants.KEY, simple(submitCollectionS3ReportPath + "${in.header.fileName}-${date:now:ddMMMyyyyHHmmss}.csv"))
                                .to(ScsbConstants.SCSB_CAMEL_S3_TO_ENDPOINT);
                    }
                });
            }
        } catch (Exception e) {
            log.error(ScsbCommonConstants.LOG_ERROR, e);
        }
    }

}
