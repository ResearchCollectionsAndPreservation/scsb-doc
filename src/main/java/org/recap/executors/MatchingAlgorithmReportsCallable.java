package org.recap.executors;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.model.jpa.MatchingBibEntity;
import org.recap.repository.jpa.MatchingBibDetailsRepository;
import org.recap.util.MatchingAlgorithmUtil;
import org.springframework.util.StopWatch;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

@Slf4j
public class MatchingAlgorithmReportsCallable implements Callable<Map<String, Integer>> {

    private MatchingBibDetailsRepository matchingBibDetailsRepository;
    private Integer batchSize;
    private String matchPoint;
    private Integer matchPointScore;
    private Map<String, Integer> institutionCounterMap;
    private MatchingAlgorithmUtil matchingAlgorithmUtil;

    /**
     * This method instantiates a new MatchingAlgorithmReportsCallable
     *
     * @param matchingBibDetailsRepository the report data details repository
     * @param batchSize                    the batch size
     * @param matchPoint                   the collection group map
     * @param matchPointScore              the institution map
     */
    public MatchingAlgorithmReportsCallable(MatchingBibDetailsRepository matchingBibDetailsRepository, Integer batchSize, Map<String, Integer> institutionCounterMap, MatchingAlgorithmUtil matchingAlgorithmUtil, String matchPoint, Integer matchPointScore) {
        this.matchingBibDetailsRepository = matchingBibDetailsRepository;
        this.batchSize = batchSize;
        this.matchPoint = matchPoint;
        this.matchPointScore = matchPointScore;
        this.institutionCounterMap = institutionCounterMap;
        this.matchingAlgorithmUtil = matchingAlgorithmUtil;

    }

    /**
     * This method is used to check for monograph status of bib and updates the CGD .
     *
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Integer> call() throws Exception {
        List<Integer> multiMatchBibIdsForMatchPoint1AndMatchPoint2 = null;
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String[] matchPointArray = matchPoint.split(",");
        String matchPoint1 = matchPointArray[0];
        String matchPoint2 = matchPointArray[1];
        if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIsbn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndIssn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndLccn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndIssn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndLccn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndLccn();
        } else if (matchPoint1.equalsIgnoreCase(ScsbConstants.TITLE_MATCH_SOLR) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForOclcAndTitle();
        } else if (matchPoint1.equalsIgnoreCase(ScsbConstants.TITLE_MATCH_SOLR) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForIsbnAndTitle();
        } else if (matchPoint1.equalsIgnoreCase(ScsbConstants.TITLE_MATCH_SOLR) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForIssnAndTitle();
        } else if (matchPoint1.equalsIgnoreCase(ScsbConstants.TITLE_MATCH_SOLR) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
            multiMatchBibIdsForMatchPoint1AndMatchPoint2 = matchingBibDetailsRepository.getMultiMatchBibIdsForLccnAndTitle();
        }
        List<List<Integer>> multipleMatchBibIds = Lists.partition(multiMatchBibIdsForMatchPoint1AndMatchPoint2, batchSize);
        Map<String, Set<Integer>> matchPoint1AndBibIdMap = new HashMap<>();
        Map<String, Set<Integer>> matchPoint2AndBibIdMap = new HashMap<>();
        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        buildBibIdAndBibEntityMap(multipleMatchBibIds, matchPoint1AndBibIdMap, matchPoint2AndBibIdMap, bibEntityMap, matchPoint1, matchPoint2);
        Set<String> matchPoint1Set = new HashSet<>();
        for (Iterator<String> iterator = matchPoint1AndBibIdMap.keySet().iterator(); iterator.hasNext(); ) {
            String matchPoint1Key = iterator.next();
            if (!matchPoint1Set.contains(matchPoint1Key)) {
                StringBuilder matchPoints1 = new StringBuilder();
                StringBuilder matchPoints2 = new StringBuilder();
                matchPoint1Set.add(matchPoint1Key);
                Set<Integer> matchPoint1BibIds = matchPoint1AndBibIdMap.get(matchPoint1Key);
                for (Iterator<String> iterator2 = matchPoint2AndBibIdMap.keySet().iterator(); iterator2.hasNext(); ) {
                    String matchPoint2Key = iterator2.next();
                    Set<Integer> matchPoint2BibIds = matchPoint2AndBibIdMap.get(matchPoint2Key);
                    Collection<Integer> matchedBibIds = CollectionUtils.intersection(matchPoint1BibIds, matchPoint2BibIds);
                    Set<Integer> tempBibIds = new HashSet<>(matchedBibIds);
                    if (!matchedBibIds.isEmpty()) {
                        for (Integer bibId : matchedBibIds) {
                            MatchingBibEntity matchingBibEntity = bibEntityMap.get(bibId);
                            if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getOclc());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getIsbn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getOclc());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getIssn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getOclc());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getLccn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getIsbn());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getIssn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getIsbn());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getLccn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getIssn());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getLccn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getTitle());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getOclc());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getTitle());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getIsbn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getTitle());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getIssn());
                            } else if (matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) && matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
                                matchPoints1.append(StringUtils.isNotBlank(matchPoints1.toString()) ? "," : "").append(matchingBibEntity.getTitle());
                                matchPoints2.append(StringUtils.isNotBlank(matchPoints2.toString()) ? "," : "").append(matchingBibEntity.getLccn());
                            }
                            //String[] matchPoint1List = matchPoints1.toString().split(",");
                            //tempBibIds.addAll(matchingAlgorithmUtil.getBibIdsForCriteriaValue(matchPoint1AndBibIdMap, matchPoint1Set, matchPoint1Key, matchPoint1, matchPoint1List, bibEntityMap, matchPoints1));
                        }
                        matchingAlgorithmUtil.populateAndSaveReportEntity(tempBibIds, bibEntityMap, matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) ? ScsbCommonConstants.OCLC_CRITERIA : matchPoint1.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) ? ScsbCommonConstants.TITLE : matchPoint1, matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE) ? ScsbCommonConstants.TITLE : matchPoint2.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) ? ScsbCommonConstants.OCLC_CRITERIA : matchPoint2, matchPoints1.toString(), matchPoints2.toString(), institutionCounterMap, matchPointScore);
                    }
                }
            }
        }
        stopWatch.stop();
        log.info("Time taken to save - {} and {} Combination Reports : {}", matchPoint1, matchPoint2, stopWatch.getTotalTimeSeconds());
        return institutionCounterMap;
    }

    private void buildBibIdAndBibEntityMap(List<List<Integer>> multipleMatchBibIds, Map<String, Set<Integer>> matchPoint1AndBibIdMap, Map<String, Set<Integer>> matchPoint2AndBibIdMap, Map<Integer, MatchingBibEntity> bibEntityMap, String matchPoint1, String matchPoint2) {
        log.info(ScsbConstants.TOTAL_BIB_ID_PARTITION_LIST, multipleMatchBibIds.size());
        for (List<Integer> bibIds : multipleMatchBibIds) {
            List<MatchingBibEntity> bibEntitiesBasedOnBibIds = matchingBibDetailsRepository.getMultiMatchBibEntitiesBasedOnBibIds(bibIds, matchPoint1, matchPoint2);
            if (CollectionUtils.isNotEmpty(bibEntitiesBasedOnBibIds)) {
                matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint1AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint1, bibEntityMap);
                matchingAlgorithmUtil.populateBibIdWithMatchingCriteriaValue(matchPoint2AndBibIdMap, bibEntitiesBasedOnBibIds, matchPoint2, bibEntityMap);
            }
        }
    }

}
