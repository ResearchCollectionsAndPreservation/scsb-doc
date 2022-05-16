package org.recap.camel.route;

import org.apache.camel.CamelContext;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.recap.BaseTestCaseUT;

public class EmailRouteBuilderUT extends BaseTestCaseUT
{
    @InjectMocks
    EmailRouteBuilder EmailRouteBuilderUT;

    @Mock
    CamelContext context;

    @Test
    public void  EmailRouteBuilder() throws Exception
    {
        String username = "email.smtp.server.username";
        String passwordDirectory  = "email.smtp.server.password.file";
        String from = "email.smtp.server.address.from";
        String upadteCgdTo = "email.scsb.updateCgd.to";
        String updateCGDCC = "email.scsb.updateCgd.cc";
        String batchJobTo = "email.scsb.updateCgd.cc";
        String updateCgdSubject = "email.scsb.batch.job.to";
        String batchJobSubject = "email.scsb.updateCgd.subject";
        String smtpServer = "email.scsb.batch.job.subject";
        String cgdReportEmailSubject = "scsb.cgd.report.mail.subject";
    }
}
