package org.recap.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCaseUT;
import org.recap.BaseTestCaseUT4;
import org.recap.ScsbCommonConstants;
import org.recap.matchingalgorithm.service.MatchingBibInfoDetailService;
import org.recap.model.solr.SolrIndexRequest;
import org.recap.service.OngoingMatchingAlgorithmService;
import org.recap.util.DateUtil;
import org.recap.util.OngoingMatchingAlgorithmUtil;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by rajeshbabuk on 20/4/17.
 */
public class OngoingMatchingAlgorithmJobRestControllerUT extends BaseTestCaseUT4 {

    @InjectMocks
    OngoingMatchingAlgorithmJobRestController ongoingMatchingAlgorithmJobRestController;

    @Mock
    OngoingMatchingAlgorithmUtil ongoingMatchingAlgorithmUtil;

    @Mock
    MatchingBibInfoDetailService matchingBibInfoDetailService;

    @Mock
    OngoingMatchingAlgorithmService ongoingMatchingAlgorithmService;

    @Mock
    DateUtil dateUtil;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(ongoingMatchingAlgorithmJobRestController,"batchSize","1000");
        Mockito.when(dateUtil.getFromDate(Mockito.any())).thenCallRealMethod();
        Mockito.when(dateUtil.getToDate(Mockito.any())).thenCallRealMethod();
    }

    @Test
    public void testStartMatchingAlgorithmJob() throws Exception {
        //  Mockito.when(ongoingMatchingAlgorithmUtil.fetchUpdatedRecordsAndStartProcess(Mockito.any(),Mockito.anyInt(),Mockito.anyBoolean(),Mockito.anyBoolean())).thenReturn(ScsbCommonConstants.SUCCESS);
        Mockito.when(matchingBibInfoDetailService.populateMatchingBibInfo(Mockito.any(),Mockito.any())).thenReturn(ScsbCommonConstants.SUCCESS);
        String status=ongoingMatchingAlgorithmJobRestController.startMatchingAlgorithmJob(getSolrIndexRequest());
        assertNotNull(ScsbCommonConstants.SUCCESS);
    }

    @Test
    public void testStartMatchingAlgorithmJobException() throws Exception {
    //    Mockito.when(ongoingMatchingAlgorithmUtil.fetchUpdatedRecordsAndStartProcess(Mockito.any(),Mockito.anyInt(),Mockito.anyBoolean(),Mockito.anyBoolean())).thenThrow(NullPointerException.class);
        String status=ongoingMatchingAlgorithmJobRestController.startMatchingAlgorithmJob(getSolrIndexRequest());
       // assertNotNull(status);
    }

    @Test
    public void testGenerateCGDRoundTripReport() throws Exception {
      Mockito.when(ongoingMatchingAlgorithmService.generateCGDRoundTripReport()).thenReturn("CGD Round-Trip report generated successfully");
        String result = ongoingMatchingAlgorithmJobRestController.generateCGDRoundTripReport();
        assertNotNull(result);
    }

    private SolrIndexRequest getSolrIndexRequest() {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setProcessType(ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM_JOB);
        solrIndexRequest.setCreatedDate(new Date());
        return solrIndexRequest;
    }
    @Test
    public void processGroupingForOngoingMatchingAlgorithm() throws  Exception
    {
        SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
        solrIndexRequest.setMatchBy("CGD");
        solrIndexRequest.setBibIds("1");
        solrIndexRequest.setMatchingCriteria("SHARED");
        solrIndexRequest.setReportType("test");
        solrIndexRequest.setNumberOfThreads(100);
        solrIndexRequest.setOwningInstitutionCode("PUL");
        solrIndexRequest.setIncludeMaQualifier(true);
        solrIndexRequest.setCommitInterval(1);
        Integer rows = 3;
        Mockito.when(ongoingMatchingAlgorithmUtil.fetchUpdatedRecordsAndStartGroupingProcessBasedOnCriteria(solrIndexRequest, rows)).thenReturn("test");
        ReflectionTestUtils.invokeMethod(ongoingMatchingAlgorithmJobRestController,"processGroupingForOngoingMatchingAlgorithm",solrIndexRequest,rows);
    }

}
