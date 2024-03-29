package org.recap.executors;



import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.recap.BaseTestCaseUT;
import org.recap.model.jpa.MatchingBibEntity;
import org.recap.repository.jpa.MatchingBibDetailsRepository;
import org.recap.util.MatchingAlgorithmUtil;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;


public class MatchingAlgorithmReportsCallableUT extends BaseTestCaseUT {

    @InjectMocks
    MatchingAlgorithmReportsCallable matchingAlgorithmReportsCallable;

    @Mock
    MatchingBibDetailsRepository matchingBibDetailsRepository;

    @Mock
    MatchingAlgorithmUtil matchingAlgorithmUtil;

    @Test
    public void MatchingAlgorithmReportsCallable() throws Exception {
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,ISBN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();

    }

    @Test
    public void Matchingpoint() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,ISSN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }



    @Test
    public void Matchingpoint2() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,LCCN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void Matchingpoint3() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISBN,ISSN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void Matchingpoint4() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISBN,LCCN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void Matchingpoint5() throws Exception {
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISSN,LCCN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }


//    @Test
//    public void Matchingpoint6() throws Exception {
//
//        Integer batchSize = 1;
//        Map<String, Integer> institutionCounterMap = new HashMap<>();
//        institutionCounterMap.put("1", 1);
//        institutionCounterMap.put("2", 2);
//        institutionCounterMap.put("3", 3);
//        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
//        String matchPoint = "OCLCNumber,Title_match";
//        Integer matchPointScore = 2;
//        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
//        matchingAlgorithmReportsCallable.call();
//    }

    @Test
    public void Matchingpoint7() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISBN,Title_match";
        Integer matchPointScore = 2;
        try {
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        }catch (Exception e){}
    }


    @Test
    public void Matchingpoint8() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "LCCN,Title_match";
        Integer matchPointScore = 2;
        try {
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        }catch(Exception e){}
    }

    @Test
    public void Matchingpoint9() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISSN,Title_match";
        Integer matchPointScore = 2;
        try {
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        }catch (Exception e){}
    }

    @Test
    public void Matchingpoint10() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,OCLCNumber";
        Integer matchPointScore = 2;
        try {
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        }catch(Exception e){}
    }


    @Test
    public void Matchingpoint11() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,ISBN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void Matchingpoint12() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,LCCN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void Matchingpoint13() throws Exception {

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,ISSN";
        Integer matchPointScore = 2;
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();
    }

    @Test
    public void buildBibIdAndBibEntityMap() throws Exception {
        List<Integer> multiMatchBibIdsForMatchPoint1AndMatchPoint2 = new ArrayList<>();
        multiMatchBibIdsForMatchPoint1AndMatchPoint2.add(1);
        List<List<Integer>> multipleMatchBibIds = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        integers.add(0);
        integers.add(1);
        multipleMatchBibIds.add(0, integers);
        multipleMatchBibIds.add(1, integers);
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> integers1 = new HashSet<>();
        integers1.add(1);
        integers1.add(2);
        matchPoint1AndBibIdMap.put("1", integers1);
        Map<String, Set<Integer>> matchPoint2AndBibIdMap = new HashMap<>();
        matchPoint2AndBibIdMap.put("test",integers1);
        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("test1");
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("123456");
        matchingBibEntity.setMaterialType("test");
        bibEntityMap.put(1, matchingBibEntity);
        bibEntitiesBasedOnBibIds.add(matchingBibEntity);
        String matchPoint1 = "ISSN";
        String matchPoint2 = "LCCN";
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(multiMatchBibIdsForMatchPoint1AndMatchPoint2);
        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
        //Mockito.doCallRealMethod(matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap)));
        ReflectionTestUtils.invokeMethod(matchingAlgorithmReportsCallable, "buildBibIdAndBibEntityMap", multipleMatchBibIds ,matchPoint1AndBibIdMap,matchPoint2AndBibIdMap,bibEntityMap, matchPoint1, matchPoint2);

    }


    @Test
    public void Match() throws Exception {

        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);

        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setTitle("test");
        matchingBibEntity.setOclc("67336");
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("OCLCNumber");
        matchingBibEntity.setId(1);
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setOwningInstBibId("1");
        matchingBibEntity.setOwningInstitution("PUL");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("1234");
        matchingBibEntity.setMaterialType("Monograph");
        matchingBibEntity.setRoot("test4");
        bibEntityMap.put(1, matchingBibEntity);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("OCLCNumber");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISBN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);


        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("ISBN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("OCLCNumber");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,ISBN";
        Integer matchPointScore = 2;
        try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
            // ReflectionTestUtils.setField(matchingAlgorithmReportsCallable,"matchingHeaderValueLength",1;
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
// Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
// Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }
    }

    @Test
    public void Match1() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        set.add(4);
        set.add(5);
        matchPoint1AndBibIdMap.put("test", set);

        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setTitle("test");
        matchingBibEntity.setOclc("67336");
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("OCLCNumber");
        matchingBibEntity.setId(1);
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setOwningInstBibId("1");
        matchingBibEntity.setOwningInstitution("PUL");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("1234");
        matchingBibEntity.setMaterialType("Monograph");
        matchingBibEntity.setRoot("test4");
        bibEntityMap.put(1, matchingBibEntity);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("OCLCNumber");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISSN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("ISSN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("OCLCNumber");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,ISSN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
try {
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
    Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
    Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
    //  Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
    // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
    //   matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
    matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
    matchingAlgorithmReportsCallable.call();
}catch (Exception e){}
    }

    @Test
    public void Match2() throws Exception {

        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);


        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setTitle("test");
        matchingBibEntity.setOclc("67336");
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("OCLCNumber");
        matchingBibEntity.setId(1);
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setOwningInstBibId("1");
        matchingBibEntity.setOwningInstitution("PUL");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("1234");
        matchingBibEntity.setMaterialType("Monograph");
        matchingBibEntity.setRoot("test4");
        bibEntityMap.put(1, matchingBibEntity);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("OCLCNumber");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("LCCN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);


        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("LCCN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("OCLCNumber");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "OCLCNumber,LCCN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
        try {
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            //  Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            //Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            //     matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }
    }
    @Test
    public void Match4() throws Exception {

        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);


        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setTitle("test");
        matchingBibEntity.setOclc("67336");
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("ISBN");
        matchingBibEntity.setId(1);
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setOwningInstBibId("1");
        matchingBibEntity.setOwningInstitution("PUL");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("1234");
        matchingBibEntity.setMaterialType("Monograph");
        matchingBibEntity.setRoot("test4");
        bibEntityMap.put(1, matchingBibEntity);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("ISBN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISSN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("ISSN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("ISBN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);


        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISBN,ISSN";
        String matchPoint1 = "ISBN";
        Integer matchPointScore = 2;
try {
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
    Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
    Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
    //  Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
    //     Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
    //   matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
    matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
    matchingAlgorithmReportsCallable.call();
}catch(Exception e){}
    }


    @Test
    public void Match5() throws Exception {

        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);
        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("ISBN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISBN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("LCCN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("ISBN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);

        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISBN,LCCN";
        String matchPoint1 = "ISSN";
        Integer matchPointScore = 2;

//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2))
      Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
        //  Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
        //     Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
       // matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();


    }


    @Test
    public void Match6() throws Exception {

        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("ISSN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISSN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("LCCN");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("ISSN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);


        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "ISSN,LCCN";
        String matchPoint1 = "ISBN";
        Integer matchPointScore = 2;

//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn()).thenReturn(Arrays.asList(1, 2));
           Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
//        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
        Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
        //      Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
        //    Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
        //  matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
        matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
        matchingAlgorithmReportsCallable.call();

    }

    @Test
    public void Match7() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);
        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("OCLCNumber");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("OCLCNumber");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("Title_match");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("OCLCNumber");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,OCLCNumber";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
     //   try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
            //  Mockito.when(matchingAlgorithmUtil.verifyMatchingCombinationValuesForMultiMatchBibs(any(), any(), any(), any())).thenReturn(set);
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            //Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            //Mockito.doNothing().when(matchingAlgorithmUtil).populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
      //  } catch (Exception e) {
        }

    @Test
    public void Match8() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("ISBN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISBN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("Title_match");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("ISBN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,ISBN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
        try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle()).thenReturn(Arrays.asList(1, 2));
            // Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
            //  Mockito.when(matchingAlgorithmUtil.verifyMatchingCombinationValuesForMultiMatchBibs(any(), any(), any(), any())).thenReturn(set);
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            //Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            //Mockito.doNothing().when(matchingAlgorithmUtil).populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }
    }
    @Test
    public void Match9() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("ISSN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("ISSN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("Title_match");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("ISSN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);


        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,ISSN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
        try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle()).thenReturn(Arrays.asList(1, 2));
            //   Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
            //  Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
            //  Mockito.when(matchingAlgorithmUtil.verifyMatchingCombinationValuesForMultiMatchBibs(any(), any(), any(), any())).thenReturn(set);
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            //Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            //Mockito.doNothing().when(matchingAlgorithmUtil).populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }

    }
    @Test
    public void Match10() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);
        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("test");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("LCCN");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);

        MatchingBibEntity matchingBibEntity1isbn = new MatchingBibEntity();
        matchingBibEntity1isbn.setTitle("test");
        matchingBibEntity1isbn.setOclc("67336");
        matchingBibEntity1isbn.setStatus("test");
        matchingBibEntity1isbn.setMatching("LCCN");
        matchingBibEntity1isbn.setId(2);
        matchingBibEntity1isbn.setBibId(1);
        matchingBibEntity1isbn.setIsbn("123456");
        matchingBibEntity1isbn.setOwningInstBibId("1");
        matchingBibEntity1isbn.setOwningInstitution("PUL");
        matchingBibEntity1isbn.setLccn("12345");
        matchingBibEntity1isbn.setIssn("1234");
        matchingBibEntity1isbn.setMaterialType("Monograph");
        matchingBibEntity1isbn.setRoot("test4");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1isbn);





        List<MatchingBibEntity> bibEntitiesBasedOnBibIds1 = new ArrayList<>();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("Title_match");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(3);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);

        MatchingBibEntity matchingBibEntity2oclc = new MatchingBibEntity();
        matchingBibEntity2oclc.setTitle("Matched");
        matchingBibEntity2oclc.setStatus("test2");
        matchingBibEntity2oclc.setMatching("LCCN");
        matchingBibEntity2oclc.setIsbn("123456");
        matchingBibEntity2oclc.setLccn("12345");
        matchingBibEntity2oclc.setIssn("1234");
        matchingBibEntity2oclc.setOclc("67336");
        matchingBibEntity2oclc.setOwningInstBibId("2");
        matchingBibEntity2oclc.setOwningInstitution("CUL");
        matchingBibEntity2oclc.setRoot("test5");
        matchingBibEntity2oclc.setBibId(2);
        matchingBibEntity2oclc.setId(4);
        matchingBibEntity2oclc.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2oclc);
        bibEntitiesBasedOnBibIds1.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,LCCN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
        try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
            ///   Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle()).thenReturn(Arrays.asList(1, 2));
            //  Mockito.when(matchingAlgorithmUtil.verifyMatchingCombinationValuesForMultiMatchBibs(any(), any(), any(), any())).thenReturn(set);
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            //Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            //Mockito.doNothing().when(matchingAlgorithmUtil).populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }}
    @Test
    public void Match11() throws Exception {
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        matchPoint1AndBibIdMap.put("test", set);

        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        MatchingBibEntity matchingBibEntity = new MatchingBibEntity();
        matchingBibEntity.setTitle("test");
        matchingBibEntity.setOclc("67336");
        matchingBibEntity.setStatus("test");
        matchingBibEntity.setMatching("Title_match");
        matchingBibEntity.setId(1);
        matchingBibEntity.setBibId(1);
        matchingBibEntity.setIsbn("123456");
        matchingBibEntity.setOwningInstBibId("1");
        matchingBibEntity.setOwningInstitution("PUL");
        matchingBibEntity.setLccn("12345");
        matchingBibEntity.setIssn("1234");
        matchingBibEntity.setMaterialType("Monograph");
        matchingBibEntity.setRoot("test4");
        bibEntityMap.put(1, matchingBibEntity);

        List<MatchingBibEntity> bibEntitiesBasedOnBibIds = new ArrayList<>();
        MatchingBibEntity matchingBibEntity1 = new MatchingBibEntity();
        MatchingBibEntity matchingBibEntity2 = new MatchingBibEntity();
        matchingBibEntity1.setTitle("Matched");
        matchingBibEntity1.setOclc("67336");
        matchingBibEntity1.setStatus("test");
        matchingBibEntity1.setMatching("Title_match");
        matchingBibEntity1.setId(1);
        matchingBibEntity1.setBibId(1);
        matchingBibEntity1.setIsbn("123456");
        matchingBibEntity1.setOwningInstBibId("1");
        matchingBibEntity1.setOwningInstitution("PUL");
        matchingBibEntity1.setLccn("12345");
        matchingBibEntity1.setIssn("1234");
        matchingBibEntity1.setMaterialType("Monograph");
        matchingBibEntity1.setRoot("test4");


        matchingBibEntity2.setTitle("Matched");
        matchingBibEntity2.setStatus("test2");
        matchingBibEntity2.setMatching("Title_match");
        matchingBibEntity2.setIsbn("123456");
        matchingBibEntity2.setLccn("12345");
        matchingBibEntity2.setIssn("1234");
        matchingBibEntity2.setOclc("67336");
        matchingBibEntity2.setOwningInstBibId("2");
        matchingBibEntity2.setOwningInstitution("CUL");
        matchingBibEntity2.setRoot("test5");
        matchingBibEntity2.setBibId(2);
        matchingBibEntity2.setId(2);
        matchingBibEntity2.setMaterialType("Monograph");
        bibEntitiesBasedOnBibIds.add(matchingBibEntity1);
        bibEntitiesBasedOnBibIds.add(matchingBibEntity2);
        Integer batchSize = 1;
        Map<String, Integer> institutionCounterMap = new HashMap<>();
        institutionCounterMap.put("1", 1);
        institutionCounterMap.put("2", 2);
        institutionCounterMap.put("3", 3);
        //  MatchingAlgorithmUtil matchingAlgorithmUtil = new MatchingAlgorithmUtil();
        String matchPoint = "Title_match,LCCN";
        String matchPoint1 = "OCLCNumber";
        Integer matchPointScore = 2;
        try {
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(any(), any(), any())).thenReturn(bibEntitiesBasedOnBibIds);
            Mockito.when(matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle()).thenReturn(Arrays.asList(1, 2));
            // Mockito.when(matchingAlgorithmUtil.verifyMatchingCombinationValuesForMultiMatchBibs(any(), any(), any(), any())).thenReturn(set);
            //  Mockito.when(matchingAlgorithmUtil.getBibIdsForCriteriaValue(any(), any(), any(), any(), any(), any(), any())).thenReturn(set);
            // Mockito.when(matchingAlgorithmUtil.populateAndSaveReportEntity(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(institutionCounterMap);
            Mockito.doNothing().when(matchingAlgorithmUtil).populateBibIdWithMatchingCriteriaValue(any(), any(), any(), any());
            matchingAlgorithmReportsCallable = new MatchingAlgorithmReportsCallable(matchingBibDetailsRepository, batchSize, institutionCounterMap, matchingAlgorithmUtil, matchPoint, matchPointScore);
            matchingAlgorithmReportsCallable.call();
        } catch (Exception e) {
        }

    }}