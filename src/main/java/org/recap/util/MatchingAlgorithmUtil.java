package org.recap.util;

import com.google.common.collect.Lists;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.recap.PropertyKeyConstants;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.controller.SolrIndexController;
import org.recap.matchingalgorithm.MatchScoreReport;
import org.recap.matchingalgorithm.MatchScoreUtil;
import org.recap.matchingalgorithm.MatchingCounter;
import org.recap.model.jpa.*;
import org.recap.model.solr.BibItem;
import org.recap.model.solr.SolrIndexRequest;
import org.recap.repository.jpa.*;
import org.recap.service.accession.SolrIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.recap.ScsbConstants.MATCHING_COUNTER_OPEN;
import static org.recap.ScsbConstants.MATCHING_COUNTER_SHARED;
import static org.recap.ScsbConstants.MATCHING_COUNTER_UPDATED_OPEN;
import static org.recap.ScsbConstants.MATCHING_COUNTER_UPDATED_SHARED;

/**
 * Created by angelind on 4/11/16.
 */
@Component
public class MatchingAlgorithmUtil {

    private static final Logger logger = LoggerFactory.getLogger(MatchingAlgorithmUtil.class);

    @Autowired
    private ProducerTemplate producerTemplate;

    @Autowired
    private MatchingMatchPointsDetailsRepository matchingMatchPointsDetailsRepository;

    @Autowired
    private MatchingBibDetailsRepository matchingBibDetailsRepository;

    @Resource(name = "recapSolrTemplate")
    private SolrTemplate solrTemplate;

    @Autowired
    private SolrQueryBuilder solrQueryBuilder;

    @Autowired
    private MatchingAlgorithmReportDetailRepository matchingAlgorithmReportDetailRepository;

    @Autowired
    private MatchingAlgorithmReportDataDetailsRepository matchingAlgorithmReportDataDetailsRepository ;

    @Autowired
    SolrIndexService solrIndexService;

    private String and = " AND ";

    private String coreParentFilterQuery = "{!parent which=\"ContentType:parent\"}";

    @Value("${" + PropertyKeyConstants.MATCHING_REPORT_HEADER_VALUE_LENGTH + "}")
    private Integer matchingHeaderValueLength;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private BibliographicDetailsRepository bibliographicDetailsRepository;

    @Autowired
    private BibliographicDetailsRepositoryForMatching bibliographicDetailsRepositoryForMatching;

    @Autowired
    private SolrIndexController bibItemIndexExecutorService;

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Gets matching algorithm report detail repository.
     *
     * @return the report detail repository
     */
    public MatchingAlgorithmReportDetailRepository getMatchingAlgorithmReportDetailRepository() {
        return matchingAlgorithmReportDetailRepository;
    }

    /**
     * This method populates and save the reports for single match bibs.
     *
     * @param batchSize the batch size
     * @param matching  the matching
     * @param institutionCounterMap
     * @param singleMatchScore
     * @return the single match bibs and save report
     */
    public Map<String,Integer> getSingleMatchBibsAndSaveReport(Integer batchSize, String matching, Map<String, Integer> institutionCounterMap, Integer singleMatchScore) {
        Map<String, Set<Integer>> criteriaMap = new HashMap<>();
        Map<Integer, MatchingBibEntity> bibEntityMap = new HashMap<>();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        List<Integer> singleMatchBibIdsBasedOnMatching = matchingBibDetailsRepository.getSingleMatchBibIdsBasedOnMatching(matching);
        stopWatch.stop();
        logger.info("Time taken to fetch {} from db : {} ",matching,stopWatch.getTotalTimeSeconds());
        logger.info("Total {}  : {} " ,matching ,singleMatchBibIdsBasedOnMatching.size());

        if(CollectionUtils.isNotEmpty(singleMatchBibIdsBasedOnMatching)) {
            List<List<Integer>> bibIdLists = Lists.partition(singleMatchBibIdsBasedOnMatching, batchSize);
            logger.info("Total {} list : {} ",matching, bibIdLists.size());
            for (Iterator<List<Integer>> iterator = bibIdLists.iterator(); iterator.hasNext(); ) {
                List<Integer> bibIds = iterator.next();
                List<MatchingBibEntity> matchingBibEntities = matchingBibDetailsRepository.getBibEntityBasedOnBibIds(bibIds);
                if(CollectionUtils.isNotEmpty(matchingBibEntities)) {
                    for (Iterator<MatchingBibEntity> matchingBibEntityIterator = matchingBibEntities.iterator(); matchingBibEntityIterator.hasNext(); ) {
                        MatchingBibEntity matchingBibEntity = matchingBibEntityIterator.next();
                        Integer bibId = matchingBibEntity.getBibId();
                        String matchCriteriaValue = getMatchCriteriaValue(matching, matchingBibEntity);
                        if(!bibEntityMap.containsKey(bibId)) {
                            bibEntityMap.put(bibId, matchingBibEntity);
                        }
                        populateCriteriaMap(criteriaMap, bibId, matchCriteriaValue);
                    }
                }
            }

            Set<String> criteriaValueSet = new HashSet<>();
            for (Iterator<String> iterator = criteriaMap.keySet().iterator(); iterator.hasNext(); ) {
                String criteriaValue = iterator.next();
                if (!criteriaValueSet.contains(criteriaValue) && criteriaMap.get(criteriaValue).size() > 1) {
                    StringBuilder matchPointValue = new StringBuilder();
                    criteriaValueSet.add(criteriaValue);
                    Set<Integer> bibIds = criteriaMap.get(criteriaValue);
                    Set<Integer> tempBibIds = new HashSet<>(bibIds);
                    for (Integer bibId : bibIds) {
                        MatchingBibEntity matchingBibEntity = bibEntityMap.get(bibId);
                        matchPointValue.append(StringUtils.isNotBlank(matchPointValue.toString()) ? "," : "").append(getMatchCriteriaValue(matching, matchingBibEntity));
                        String[] criteriaValueList = matchPointValue.toString().split(",");
                        tempBibIds.addAll(getBibIdsForCriteriaValue(criteriaMap, criteriaValueSet, criteriaValue, matching, criteriaValueList, bibEntityMap, matchPointValue));
                    }
                    List<Integer> tempBibIdList = new ArrayList<>(tempBibIds);
                    saveReportForSingleMatch(matchPointValue.toString(), tempBibIdList, matching, bibEntityMap, false,institutionCounterMap,singleMatchScore);
                }
            }
        }
        return institutionCounterMap;
    }

    /**
     * Process pending matching bibs map.
     *
     * @param matchingBibEntityList the matching bib entity list
     * @param matchingBibIds        the matching bib ids
     * @param institutionCounterMap
     * @return the map
     */
    public Map processPendingMatchingBibs(List<MatchingBibEntity> matchingBibEntityList, Set<Integer> matchingBibIds, Map<String, Integer> institutionCounterMap) {
        Integer singleMatchScore=0;
        if(CollectionUtils.isNotEmpty(matchingBibEntityList)) {
            for(MatchingBibEntity matchingBibEntity : matchingBibEntityList) {
                if(!matchingBibIds.contains(matchingBibEntity.getId())) {
                    Map<Integer, MatchingBibEntity> matchingBibEntityMap = new HashMap<>();
                    String matchPointValue = "";
                    String query = "";
                    if(matchingBibEntity.getMatching().equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC)) {
                        singleMatchScore=MatchScoreUtil.OCLC_SCORE;
                        matchPointValue = matchingBibEntity.getOclc();
                        if(StringUtils.isNotBlank(matchPointValue))
                            query = solrQueryBuilder.solrQueryForInitialMatching(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC, Arrays.asList(matchPointValue.split(",")));
                    } else if(matchingBibEntity.getMatching().equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
                        singleMatchScore=MatchScoreUtil.ISBN_SCORE;
                        matchPointValue = matchingBibEntity.getIsbn();
                        if(StringUtils.isNotBlank(matchPointValue))
                            query = solrQueryBuilder.solrQueryForInitialMatching(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN, Arrays.asList(matchPointValue.split(",")));
                    } else if(matchingBibEntity.getMatching().equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
                        singleMatchScore=MatchScoreUtil.ISSN_SCORE;
                        matchPointValue = matchingBibEntity.getIssn();
                        if(StringUtils.isNotBlank(matchPointValue))
                            query = solrQueryBuilder.solrQueryForInitialMatching(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN, Arrays.asList(matchPointValue.split(",")));
                    } else if(matchingBibEntity.getMatching().equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
                        singleMatchScore=MatchScoreUtil.LCCN_SCORE;
                        matchPointValue = matchingBibEntity.getLccn();
                        query = solrQueryBuilder.solrQueryForInitialMatching(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN, matchPointValue);
                    }
                    List<Integer> bibIds = getBibsFromSolr(query);
                    if(bibIds.size() > 1) {
                        List<MatchingBibEntity> bibEntities = matchingBibDetailsRepository.findByMatchingAndBibIdIn(matchingBibEntity.getMatching(), bibIds);
                        for(MatchingBibEntity bibEntity : bibEntities) {
                            matchingBibEntityMap.put(bibEntity.getBibId(), bibEntity);
                            matchingBibIds.add(bibEntity.getId());
                        }
                        saveReportForSingleMatch(matchPointValue, bibIds, matchingBibEntity.getMatching(), matchingBibEntityMap, true, institutionCounterMap, singleMatchScore);
                    }
                }
            }
        }

        return institutionCounterMap;
    }

    private List<Integer> getBibsFromSolr(String query) {
        List<Integer> bibIds = new ArrayList<>();
        try {
            SolrQuery solrQuery = new SolrQuery(query);
            solrQuery.setFields(ScsbCommonConstants.BIB_ID);
            QueryResponse queryResponse = solrTemplate.getSolrClient().query(solrQuery);
            SolrDocumentList solrDocumentList = queryResponse.getResults();
            long numFound = solrDocumentList.getNumFound();
            if(numFound > solrDocumentList.size()) {
                solrQuery.setRows((int) numFound);
                queryResponse = solrTemplate.getSolrClient().query(solrQuery);
                solrDocumentList = queryResponse.getResults();
            }
            for (Iterator<SolrDocument> iterator = solrDocumentList.iterator(); iterator.hasNext(); ) {
                SolrDocument solrDocument = iterator.next();
                Integer bibId = (Integer) solrDocument.getFieldValue(ScsbCommonConstants.BIB_ID);
                bibIds.add(bibId);
            }
        } catch (Exception e) {
            logger.error(ScsbCommonConstants.LOG_ERROR,e);
        }
        return bibIds;
    }

    /**
     * This method gets bib ids for the given criteria values.
     *
     * @param criteriaMap       the criteria map
     * @param criteriaValueSet  the criteria value set
     * @param criteriaValue     the criteria value
     * @param matching          the matching
     * @param criteriaValueList the criteria value list
     * @param bibEntityMap      the bib entity map
     * @param matchPointValue   the match point value
     * @return the bib ids for criteria value
     */
    public Set<Integer> getBibIdsForCriteriaValue(Map<String, Set<Integer>> criteriaMap, Set<String> criteriaValueSet, String criteriaValue,
                                                   String matching, String[] criteriaValueList, Map<Integer, MatchingBibEntity> bibEntityMap, StringBuilder matchPointValue) {
        Set<Integer> tempBibIdSet = new HashSet<>();
        for (String value : criteriaValueList) {
            criteriaValueSet.add(value);
            if (!value.equalsIgnoreCase(criteriaValue)) {
                Set<Integer> bibIdSet = criteriaMap.get(value);
                if (org.apache.commons.collections.CollectionUtils.isNotEmpty(bibIdSet)) {
                    for(Integer bibId : bibIdSet) {
                        MatchingBibEntity matchingBibEntity = bibEntityMap.get(bibId);
                        String matchCriteriaValue = getMatchCriteriaValue(matching, matchingBibEntity);
                        String[] matchCriteriaValueList = matchCriteriaValue.split(",");
                        for(String matchingValue : matchCriteriaValueList) {
                            if(!criteriaValueSet.contains(matchingValue)) {
                                matchPointValue.append(StringUtils.isNotBlank(matchPointValue.toString()) ? "," : "").append(matchingValue);
                                criteriaValueSet.add(matchingValue);
                            }
                        }
                    }
                    tempBibIdSet.addAll(bibIdSet);
                }
            }
        }
        return tempBibIdSet;
    }

    /**
     * This method replaces diacritics(~= accents) characters by replacing them to normal characters in title.
     *
     * @param title the title
     * @return the string
     */
    public String normalizeDiacriticsInTitle(String title) {
        String normalizedTitle = Normalizer.normalize(title, Normalizer.Form.NFD);
        normalizedTitle = normalizedTitle.replaceAll("[^\\p{ASCII}]", "");
        normalizedTitle = normalizedTitle.replaceAll("\\p{M}", "");
        return normalizedTitle;
    }

    /**
     * This method saves report for single match based on the criteria values (oclc,isbn,issn and lccn).
     *
     * @param criteriaValue        the criteria value
     * @param bibIdList            the bib id list
     * @param criteria             the criteria
     * @param matchingBibEntityMap the matching bib entity map
     * @param isPendingBibs        the is pending bibs
     * @param institutionCounterMap
     * @param singleMatchScore
     * @return the map
     */
    public Map<String, Integer> saveReportForSingleMatch(String criteriaValue, List<Integer> bibIdList, String criteria, Map<Integer, MatchingBibEntity> matchingBibEntityMap, boolean isPendingBibs, Map<String, Integer> institutionCounterMap, Integer singleMatchScore) {
        List<MatchingAlgorithmReportDataEntity> reportDataEntities = new ArrayList<>();
        Set<String> owningInstSet = new HashSet<>();
        Set<String> materialTypeSet = new HashSet<>();
        List<Integer> bibIds = new ArrayList<>();
        List<String> owningInstList = new ArrayList<>();
        List<String> materialTypeList = new ArrayList<>();
        Map<String,String> titleMap = new HashMap<>();
        List<MatchingAlgorithmReportEntity> reportEntitiesToSave = new ArrayList<>();
        List<String> owningInstBibIds = new ArrayList<>();


        int index=0;
        for (Iterator<Integer> iterator = bibIdList.iterator(); iterator.hasNext(); ) {
            Integer bibId = iterator.next();
                MatchingBibEntity matchingBibEntity = matchingBibEntityMap.get(bibId);
            if (matchingBibEntity != null) {
                owningInstSet.add(matchingBibEntity.getOwningInstitution());
                owningInstList.add(matchingBibEntity.getOwningInstitution());
                owningInstBibIds.add(matchingBibEntity.getOwningInstBibId());
                bibIds.add(bibId);
                materialTypeList.add(matchingBibEntity.getMaterialType());
                materialTypeSet.add(matchingBibEntity.getMaterialType());
                index = index + 1;
                if (StringUtils.isNotBlank(matchingBibEntity.getTitle())) {
                    String titleHeader = ScsbCommonConstants.TITLE + index;
                    getReportDataEntity(titleHeader, matchingBibEntity.getTitle(), reportDataEntities);
                    titleMap.put(titleHeader, matchingBibEntity.getTitle());
                }
            }
        }

        if(owningInstSet.size() > 1) {
            MatchingAlgorithmReportEntity reportEntity = new MatchingAlgorithmReportEntity();
            String fileName;
            String criteriaForFileName = criteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC) ? ScsbCommonConstants.OCLC_CRITERIA : criteria;
            if(isPendingBibs) {
                fileName = ScsbConstants.MATCHING_PENDING_BIBS;
            } else {
                fileName = criteriaForFileName;
            }
            reportEntity.setFileName(fileName);
            reportEntity.setInstitutionName(ScsbCommonConstants.ALL_INST);
            reportEntity.setCreatedDate(new Date());
            Set<String> unMatchingTitleHeaderSet = getMatchingAndUnMatchingBibsOnTitleVerification(titleMap);
            if(CollectionUtils.isNotEmpty(unMatchingTitleHeaderSet)) {

                reportEntitiesToSave.add(processReportsForUnMatchingTitles(criteriaForFileName, titleMap, bibIds,
                        materialTypeList, owningInstList, owningInstBibIds,
                        criteriaValue, unMatchingTitleHeaderSet));

            }
            if(materialTypeSet.size() != 1) {
                reportEntity.setType(ScsbConstants.MATERIAL_TYPE_EXCEPTION);
            } else {
                if(CollectionUtils.isNotEmpty(unMatchingTitleHeaderSet)){
                    reportEntity.setType(ScsbConstants.SINGLE_MATCH_TITLE_EXCEPTION);
                }else{
                    reportEntity.setType(ScsbConstants.SINGLE_MATCH);
                owningInstList.forEach(owningInst -> institutionCounterMap.replace(owningInst, +1));
                    singleMatchScore=MatchScoreUtil.getMatchScoreForSingleMatchAndTitle(singleMatchScore);
                }
            }

            getReportDataEntityList(reportDataEntities, owningInstList, bibIds, materialTypeList, owningInstBibIds,singleMatchScore);

            getReportDataEntity(criteriaForFileName, criteriaValue, reportDataEntities);

            reportEntity.addAll(reportDataEntities);
            reportEntitiesToSave.add(reportEntity);
            if(!isPendingBibs) {
                Map matchingBibMap = new HashMap();
                matchingBibMap.put(ScsbCommonConstants.STATUS, ScsbCommonConstants.COMPLETE_STATUS);
                matchingBibMap.put(ScsbConstants.MATCHING_BIB_IDS, bibIds);
                producerTemplate.sendBody("scsbactivemq:queue:updateMatchingBibEntityQ", matchingBibMap);
            }
        }
        if(CollectionUtils.isNotEmpty(reportEntitiesToSave)) {
            producerTemplate.sendBody("scsbactivemq:queue:saveMatchingReportsQ", reportEntitiesToSave);
        }
        return institutionCounterMap;
    }

    /**
     * This method gets set of matched and un-matched bibs on title verification.
     *
     * @param titleMap the title map
     * @return the matched and un matched bibs on title verification
     */
    public Set<String> getMatchingAndUnMatchingBibsOnTitleVerification(Map<String, String> titleMap) {

        Set<String> unMatchingTitleHeaderSet = new HashSet<>();
        if (titleMap != null) {
            List<String> titleHeaders = new ArrayList(titleMap.keySet());
            for(int i=0; i < titleMap.size(); i++) {
                for(int j=i+1; j < titleMap.size(); j++) {
                    String titleHeader1 = titleHeaders.get(i);
                    String titleHeader2 = titleHeaders.get(j);
                    String title1 = titleMap.get(titleHeader1);
                    String title2 = titleMap.get(titleHeader2);
                    title1 = getTitleToMatch(title1);
                    title2 = getTitleToMatch(title2);
                    if(!(title1.equalsIgnoreCase(title2))) {
                        unMatchingTitleHeaderSet.add(titleHeader1);
                        unMatchingTitleHeaderSet.add(titleHeader2);
                    }
                }
            }
        }
        return unMatchingTitleHeaderSet;
    }

    /**
     * This method gets matched title for  the given title.
     *
     * @param title the title
     * @return the title to match
     */
    public String getTitleToMatch(String title) {
        title = normalizeDiacriticsInTitle(title.trim());
        title = title.replaceAll("[^\\w\\s]", "").trim();
        title = title.replaceAll("\\s{2,}", " ");
        String titleToMatch = "";
        if(StringUtils.isNotBlank(title)) {
            String[] titleArray = title.split(" ");
            int count = 0;
            for (int j = 0; j < titleArray.length; j++) {
                String tempTitle = titleArray[j];
                if (!("a".equalsIgnoreCase(tempTitle) || "an".equalsIgnoreCase(tempTitle) || "the".equalsIgnoreCase(tempTitle))) {
                    titleToMatch = getTitleToMatch(titleToMatch, count, tempTitle);
                    count = count + 1;
                } else {
                    if(j != 0) {
                        titleToMatch = getTitleToMatch(titleToMatch, count, tempTitle);
                        count = count + 1;
                    }
                }
                if (count == 4) {
                    break;
                }
            }
        }
        return titleToMatch.replaceAll("\\s", "");
    }

    private String getTitleToMatch(String titleToMatch, int count, String tempTitle) {
        if (count == 0) {
            titleToMatch = tempTitle;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(titleToMatch);
            stringBuilder.append(" ");
            stringBuilder.append(tempTitle);
            titleToMatch = stringBuilder.toString();
        }
        return titleToMatch;
    }

    /**
     * This method gets a list of report data entities for matching algorithm reports.
     * @param reportDataEntities the report data entities
     * @param owningInstSet      the owning inst set
     * @param bibIds             the bib ids
     * @param materialTypes      the material types
     * @param owningInstBibIds   the owning inst bib ids
     */
    public void getReportDataEntityList(List<MatchingAlgorithmReportDataEntity> reportDataEntities, Collection owningInstSet, Collection bibIds, Collection materialTypes, List<String> owningInstBibIds,Integer matchScore) {
        checkAndAddReportDataEntities(reportDataEntities, bibIds, ScsbCommonConstants.BIB_ID);
        checkAndAddReportDataEntities(reportDataEntities, owningInstSet, ScsbCommonConstants.OWNING_INSTITUTION);
        checkAndAddReportDataEntities(reportDataEntities, materialTypes, ScsbConstants.MATERIAL_TYPE);
        checkAndAddReportDataEntities(reportDataEntities, owningInstBibIds, ScsbCommonConstants.OWNING_INSTITUTION_BIB_ID);
        checkAndAddReportDataEntities(reportDataEntities, Collections.singleton(matchScore), ScsbConstants.MATCH_SCORE);
    }

    private void checkAndAddReportDataEntities(List<MatchingAlgorithmReportDataEntity> reportDataEntities, Collection bibIds, String bibId) {
        if (CollectionUtils.isNotEmpty(bibIds)) {
            MatchingAlgorithmReportDataEntity bibIdReportDataEntity = getReportDataEntityForCollectionValues(bibIds, bibId);
            reportDataEntities.add(bibIdReportDataEntity);
        }
    }

    /**
     * This method gets report data entity for collection values.
     *
     * @param headerValues the header values
     * @param headerName   the header name
     * @return the report data entity for collection values
     */
    public MatchingAlgorithmReportDataEntity getReportDataEntityForCollectionValues(Collection headerValues, String headerName) {
        MatchingAlgorithmReportDataEntity bibIdReportDataEntity = new MatchingAlgorithmReportDataEntity();
        bibIdReportDataEntity.setHeaderName(headerName);
        String joinedHeaderValue = StringUtils.join(headerValues, ",");
        if (StringUtils.isNotBlank(joinedHeaderValue)){
            setTrimmedHeaderValue(headerName, bibIdReportDataEntity, joinedHeaderValue);
        }else {
            bibIdReportDataEntity.setHeaderValue(joinedHeaderValue);
        }
        return bibIdReportDataEntity;
    }

    private void setTrimmedHeaderValue(String headerName, MatchingAlgorithmReportDataEntity bibIdReportDataEntity, String joinedHeaderValue) {
        int headerValueLength = joinedHeaderValue.length();
        if (headerValueLength <= matchingHeaderValueLength){
            bibIdReportDataEntity.setHeaderValue(joinedHeaderValue);
        }else {
            logger.debug("Header value : {} ",joinedHeaderValue);
            logger.info("Maximum Header value crossed : {} for header name : {} and started truncating",joinedHeaderValue.length(),headerName);
            String substring = StringUtils.substring(joinedHeaderValue, 0, matchingHeaderValueLength);
            bibIdReportDataEntity.setHeaderValue(StringUtils.substringBeforeLast(substring,","));
        }
    }

    /**
     * This method populates bib id with matching criteria values.
     *
     * @param criteria1Map        the criteria 1 map
     * @param matchingBibEntities the matching bib entities
     * @param matchCriteria1      the match criteria 1
     * @param bibEntityMap        the bib entity map
     */
    public void populateBibIdWithMatchingCriteriaValue(Map<String, Set<Integer>> criteria1Map, List<MatchingBibEntity> matchingBibEntities, String matchCriteria1, Map<Integer, MatchingBibEntity> bibEntityMap) {
        for (Iterator<MatchingBibEntity> iterator = matchingBibEntities.iterator(); iterator.hasNext(); ) {
            MatchingBibEntity matchingBibEntity = iterator.next();
            Integer bibId = matchingBibEntity.getBibId();
            String matching = matchingBibEntity.getMatching();
            if(!bibEntityMap.containsKey(bibId)) {
                bibEntityMap.put(bibId, matchingBibEntity);
            }
            if(matching.equalsIgnoreCase(matchCriteria1)) {
                String criteriaValue1 = getMatchCriteriaValue(matchCriteria1, matchingBibEntity);
                populateCriteriaMap(criteria1Map, bibId, criteriaValue1);
            }
        }
    }

    /**
     * This method populates the bib ids for matching match point values.
     *
     * @param criteriaMap the criteria map
     * @param bibId       the bib id
     * @param value       the value
     */
    public void populateCriteriaMap(Map<String, Set<Integer>> criteriaMap, Integer bibId, String value) {
        String[] criteriaValues = value.split(",");
        for(String criteriaValue : criteriaValues) {
            if(StringUtils.isNotBlank(criteriaValue)) {
                if(criteriaMap.containsKey(criteriaValue)) {
                    Set<Integer> bibIds = criteriaMap.get(criteriaValue);
                    Set<Integer> bibIdSet = new HashSet<>(bibIds);
                    bibIdSet.add(bibId);
                    criteriaMap.put(criteriaValue, bibIdSet);
                } else {
                    Set<Integer> bibIdSet = new HashSet<>();
                    bibIdSet.add(bibId);
                    criteriaMap.put(criteriaValue,bibIdSet);
                }
            }
        }
    }

    /**
     * This method gets match point criteria value.
     *
     * @param matchCriteria     the match criteria
     * @param matchingBibEntity the matching bib entity
     * @return the match criteria value
     */
    public String getMatchCriteriaValue(String matchCriteria, MatchingBibEntity matchingBibEntity) {
        if(matchCriteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC)) {
            return matchingBibEntity.getOclc();
        } else if (matchCriteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN)) {
            return matchingBibEntity.getIsbn();
        } else if (matchCriteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN)) {
            return matchingBibEntity.getIssn();
        } else if (matchCriteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_LCCN)) {
            return matchingBibEntity.getLccn();
        } else if (matchCriteria.equalsIgnoreCase(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE)) {
            return matchingBibEntity.getTitle();
        }
        return "";
    }

    /**
     * This method populates and save report entity for multi match scenario in matching algorithm.
     *
     * @param bibIds       the bib ids
     * @param bibEntityMap the bib entity map
     * @param header1      the header 1
     * @param header2      the header 2
     * @param oclcNumbers  the oclc numbers
     * @param isbns        the isbns
     * @param matchScore
     * @return the map
     */
    public Map<String,Integer> populateAndSaveReportEntity(Set<Integer> bibIds, Map<Integer, MatchingBibEntity> bibEntityMap, String header1, String header2, String oclcNumbers, String isbns, Map<String, Integer> institutionCounterMap, Integer matchScore) {
        MatchingAlgorithmReportEntity reportEntity = new MatchingAlgorithmReportEntity();
        List<String> owningInstAllList = new ArrayList<>();
        List<MatchingAlgorithmReportDataEntity> reportDataEntities = new ArrayList<>();
        reportEntity.setFileName(header1 + "," + header2);
        reportEntity.setCreatedDate(new Date());
        reportEntity.setInstitutionName(ScsbCommonConstants.ALL_INST);
        List<String> owningInstList = new ArrayList<>();
        List<Integer> bibIdList = new ArrayList<>();
        List<String> materialTypeList = new ArrayList<>();
        Set<String> materialTypes = new HashSet<>();
        List<String> owningInstBibIds = new ArrayList<>();


        for (Iterator<Integer> integerIterator = bibIds.iterator(); integerIterator.hasNext(); ) {
            Integer bibId = integerIterator.next();
            MatchingBibEntity matchingBibEntity = bibEntityMap.get(bibId);
            owningInstAllList.add(matchingBibEntity.getOwningInstitution());
            owningInstList.add(matchingBibEntity.getOwningInstitution());
            bibIdList.add(matchingBibEntity.getBibId());
            materialTypes.add(matchingBibEntity.getMaterialType());
            materialTypeList.add(matchingBibEntity.getMaterialType());
            owningInstBibIds.add(matchingBibEntity.getOwningInstBibId());
        }
        if(materialTypes.size() == 1) {
            reportEntity.setType(ScsbConstants.MULTI_MATCH);
        } else {
            reportEntity.setType(ScsbConstants.MATERIAL_TYPE_EXCEPTION);
        }
        if(owningInstAllList.size() > 1) {
            getReportDataEntityList(reportDataEntities, owningInstList, bibIdList, materialTypeList, owningInstBibIds,matchScore);
            owningInstList.forEach(owningInst -> institutionCounterMap.replace(owningInst, institutionCounterMap.get(owningInst) + 1));
            if(StringUtils.isNotBlank(oclcNumbers)) {
                getReportDataEntity(header1, oclcNumbers, reportDataEntities);
            }
            if(StringUtils.isNotBlank(isbns)) {
                getReportDataEntity(header2, isbns, reportDataEntities);
            }
            reportEntity.addAll(reportDataEntities);
            Map matchingBibMap = new HashMap();
            matchingBibMap.put(ScsbCommonConstants.STATUS, ScsbCommonConstants.COMPLETE_STATUS);
            matchingBibMap.put(ScsbConstants.MATCHING_BIB_IDS, bibIdList);
            producerTemplate.sendBody("scsbactivemq:queue:updateMatchingBibEntityQ", matchingBibMap);
            producerTemplate.sendBody("scsbactivemq:queue:saveMatchingReportsQ", Arrays.asList(reportEntity));
        }
        return institutionCounterMap;
    }

    /**
     * This method gets report data entity.
     *  @param headerName         the header 1
     * @param headerValues       the header values
     * @param reportDataEntities the report data entities
     */
    public void getReportDataEntity(String headerName, String headerValues, List<MatchingAlgorithmReportDataEntity> reportDataEntities) {
        MatchingAlgorithmReportDataEntity criteriaReportDataEntity = new MatchingAlgorithmReportDataEntity();
        if (headerValues.length() > 10000) {
            logger.info(" Length of the header name with size greater than 10000- {} - size - {}",headerName, headerValues.length());
            headerValues = headerValues.substring(0,9996)+"...";
        }
        criteriaReportDataEntity.setHeaderName(headerName);
        criteriaReportDataEntity.setHeaderValue(headerValues);
        reportDataEntities.add(criteriaReportDataEntity);
    }

    /**
     * This method process reports for the bibs which came into matching algorithm but differs in title.
     *
     * @param fileName                 the file name
     * @param titleMap                 the title map
     * @param bibIds                   the bib ids
     * @param materialTypes            the material types
     * @param owningInstitutions       the owning institutions
     * @param owningInstBibIds         the owning inst bib ids
     * @param matchPointValue          the match point value
     * @param unMatchingTitleHeaderSet the un matching title header set
     * @return the report entity
     */
    public MatchingAlgorithmReportEntity processReportsForUnMatchingTitles(String fileName, Map<String, String> titleMap, List<Integer> bibIds, List<String> materialTypes, List<String> owningInstitutions,
                                                          List<String> owningInstBibIds, String matchPointValue, Set<String> unMatchingTitleHeaderSet) {
        MatchingAlgorithmReportEntity unMatchReportEntity = buildReportEntity(fileName);
        List<MatchingAlgorithmReportDataEntity> reportDataEntityList = new ArrayList<>();
        List<String> bibIdList = new ArrayList<>();
        List<String> materialTypeList = new ArrayList<>();
        List<String> owningInstitutionList = new ArrayList<>();
        List<String> owningInstBibIdList = new ArrayList<>();

        prepareReportForUnMatchingTitles(titleMap, bibIds, materialTypes, owningInstitutions, owningInstBibIds, unMatchingTitleHeaderSet, reportDataEntityList, bibIdList, materialTypeList, owningInstitutionList, owningInstBibIdList);

        getReportDataEntityList(reportDataEntityList, owningInstitutionList, bibIdList, materialTypeList, owningInstBibIdList,0);

        if(StringUtils.isNotBlank(matchPointValue)) {
            getReportDataEntity(fileName, matchPointValue, reportDataEntityList);
        }
        unMatchReportEntity.addAll(reportDataEntityList);
        return unMatchReportEntity;
    }

    public MatchingAlgorithmReportEntity buildReportEntity(String fileName) {
        MatchingAlgorithmReportEntity unMatchReportEntity = new MatchingAlgorithmReportEntity();
        unMatchReportEntity.setType("TitleException");
        unMatchReportEntity.setCreatedDate(new Date());
        unMatchReportEntity.setInstitutionName(ScsbCommonConstants.ALL_INST);
        unMatchReportEntity.setFileName(fileName);
        return unMatchReportEntity;
    }

    /**
     * This method prepares reports for the bibs which came into matching algorithm but differs in title
     *  @param titleMap                 the title map
     * @param bibIds                   the bib ids
     * @param materialTypes            the material types
     * @param owningInstitutions       the owning institutions
     * @param owningInstBibIds         the owning inst bib ids
     * @param unMatchingTitleHeaderSet the un matching title header set
     * @param reportDataEntityList     the report data entity list
     * @param bibIdList                the bib id list
     * @param materialTypeList         the material type list
     * @param owningInstitutionList    the owning institution list
     * @param owningInstBibIdList      the owning inst bib id list
     */
    public void prepareReportForUnMatchingTitles(Map<String, String> titleMap, List<Integer> bibIds, List<String> materialTypes, List<String> owningInstitutions, List<String> owningInstBibIds,
                                                 Set<String> unMatchingTitleHeaderSet, List<MatchingAlgorithmReportDataEntity> reportDataEntityList, List<String> bibIdList,
                                                 List<String> materialTypeList, List<String> owningInstitutionList, List<String> owningInstBibIdList) {
        for (Iterator<String> stringIterator = unMatchingTitleHeaderSet.iterator(); stringIterator.hasNext(); ) {
            String titleHeader = stringIterator.next();
            int i = Integer.parseInt(titleHeader.replace(ScsbCommonConstants.TITLE, ""));
            if(bibIds != null) {
                bibIdList.add(String.valueOf(bibIds.get(i-1)));
            }
            if(materialTypes != null) {
                materialTypeList.add(materialTypes.get(i-1));
            }
            if(owningInstitutions != null) {
                owningInstitutionList.add(owningInstitutions.get(i-1));
            }
            if(owningInstBibIds != null) {
                owningInstBibIdList.add(owningInstBibIds.get(i-1));
            }
            MatchingAlgorithmReportDataEntity titleReportDataEntity = new MatchingAlgorithmReportDataEntity();
            titleReportDataEntity.setHeaderName(titleHeader);
            titleReportDataEntity.setHeaderValue(titleMap.get(titleHeader));
            reportDataEntityList.add(titleReportDataEntity);
        }
    }

    /**
     * This method gets matching match points based on the field name.
     *
     * @param fieldName the field name
     * @return the matching match points entity
     * @throws Exception the exception
     */
    public List<MatchingMatchPointsEntity> getMatchingMatchPointsEntity(String fieldName) throws Exception {
        List<MatchingMatchPointsEntity> matchingMatchPointsEntities = new ArrayList<>();
        String query = ScsbCommonConstants.DOCTYPE + ":" + ScsbCommonConstants.BIB +
                and + ScsbConstants.BIB_CATALOGING_STATUS + ":" + ScsbCommonConstants.COMPLETE_STATUS +
                and + ScsbCommonConstants.IS_DELETED_BIB + ":" + ScsbConstants.FALSE +
                and + coreParentFilterQuery + ScsbConstants.ITEM_CATALOGING_STATUS + ":" + ScsbCommonConstants.COMPLETE_STATUS +
                and + coreParentFilterQuery + ScsbCommonConstants.IS_DELETED_ITEM + ":" + ScsbConstants.FALSE;
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.setFacet(true);
        solrQuery.addFacetField(fieldName);
        solrQuery.setFacetLimit(-1);
        solrQuery.setFacetMinCount(2);
        solrQuery.setRows(0);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        QueryResponse queryResponse = solrTemplate.getSolrClient().query(solrQuery);
        stopWatch.stop();
        logger.info("Total Time Taken to get {} duplicates from solr : {}  ",fieldName ,stopWatch.getTotalTimeSeconds());
        List<FacetField> facetFields = queryResponse.getFacetFields();
        for (FacetField facetField : facetFields) {
            List<FacetField.Count> values = facetField.getValues();
            for (Iterator<FacetField.Count> iterator = values.iterator(); iterator.hasNext(); ) {
                FacetField.Count next = iterator.next();
                String name = next.getName();
                if(StringUtils.isNotBlank(name)) {
                    MatchingMatchPointsEntity matchingMatchPointsEntity = new MatchingMatchPointsEntity();
                    matchingMatchPointsEntity.setMatchCriteria(fieldName);
                    matchingMatchPointsEntity.setCriteriaValue(name);
                    matchingMatchPointsEntity.setCriteriaValueCount((int) next.getCount());
                    matchingMatchPointsEntities.add(matchingMatchPointsEntity);
                }
            }
        }
        return matchingMatchPointsEntities;
    }

    /**
     * This method saves matching match point entities by using activemq.
     *
     * @param matchingMatchPointsEntities the matching match points entities
     */
    public void saveMatchingMatchPointEntities(List<MatchingMatchPointsEntity> matchingMatchPointsEntities) {
        int batchSize = 1000;
        int size = 0;
        if (CollectionUtils.isNotEmpty(matchingMatchPointsEntities)) {
            for (int i = 0; i < matchingMatchPointsEntities.size(); i += batchSize) {
                List<MatchingMatchPointsEntity> matchingMatchPointsEntityList = new ArrayList<>(matchingMatchPointsEntities.subList(i, Math.min(i + batchSize, matchingMatchPointsEntities.size())));
                producerTemplate.sendBody("scsbactivemq:queue:saveMatchingMatchPointsQ", matchingMatchPointsEntityList);
                size = size + matchingMatchPointsEntityList.size();
            }
        }
    }

    /**
     * This method gets cgd count based on institution from solr.
     *
     * @param owningInstitution the owning institution
     * @param cgd               the cgd
     * @return the cgd count based on inst
     * @throws SolrServerException the solr server exception
     * @throws IOException         the io exception
     */
    public Integer getCGDCountBasedOnInst(String owningInstitution, String cgd) throws SolrServerException, IOException {
        SolrQuery solrQuery = solrQueryBuilder.buildSolrQueryForCGDReports(owningInstitution, cgd);
        solrQuery.setRows(0);
        QueryResponse queryResponse = solrTemplate.getSolrClient().query(solrQuery);
        SolrDocumentList results = queryResponse.getResults();
        return Math.toIntExact(results.getNumFound());
    }

    /**
     * This method updates the reports which was found as an exception due to the different material types.
     *
     * @param exceptionRecordNums the exception record nums
     * @param batchSize           the batch size
     */
    public void updateExceptionRecords(List<Integer> exceptionRecordNums, Integer batchSize) {
        if(CollectionUtils.isNotEmpty(exceptionRecordNums)) {
            List<List<Integer>> exceptionRecordNumbers = Lists.partition(exceptionRecordNums, batchSize);
            for(List<Integer> exceptionRecordNumberList : exceptionRecordNumbers) {
                List<MatchingAlgorithmReportEntity> reportEntities = matchingAlgorithmReportDetailRepository.findByIdIn(exceptionRecordNumberList);
                for(MatchingAlgorithmReportEntity reportEntity : reportEntities) {
                    reportEntity.setType(ScsbConstants.MATERIAL_TYPE_EXCEPTION);
                }
                matchingAlgorithmReportDetailRepository.saveAll(reportEntities);
            }
        }
    }

    /**
     * This method updates reports which are found to be a monographic set record in database.
     *
     * @param nonMonographRecordNums the non monograph record nums
     * @param batchSize              the batch size
     */
    public void updateMonographicSetRecords(List<Integer> nonMonographRecordNums, Integer batchSize) {
        if(CollectionUtils.isNotEmpty(nonMonographRecordNums)) {
            List<List<Integer>> monographicSetRecordNumbers = Lists.partition(nonMonographRecordNums, batchSize);
            for(List<Integer> monographicSetRecordNumberList : monographicSetRecordNumbers) {
                List<MatchingAlgorithmReportDataEntity> reportDataEntitiesToUpdate = matchingAlgorithmReportDataDetailsRepository.getReportDataEntityByRecordNumIn(monographicSetRecordNumberList, ScsbConstants.MATERIAL_TYPE);
                if(CollectionUtils.isNotEmpty(reportDataEntitiesToUpdate)) {
                    for(MatchingAlgorithmReportDataEntity reportDataEntity : reportDataEntitiesToUpdate) {
                        String headerValue = reportDataEntity.getHeaderValue();
                        String[] materialTypes = headerValue.split(",");
                        List<String> modifiedMaterialTypes = new ArrayList<>();
                        for(int i=0; i < materialTypes.length; i++) {
                            modifiedMaterialTypes.add(ScsbConstants.MONOGRAPHIC_SET);
                        }
                        reportDataEntity.setHeaderValue(StringUtils.join(modifiedMaterialTypes, ","));
                    }
                    matchingAlgorithmReportDataDetailsRepository.saveAll(reportDataEntitiesToUpdate);
                }
            }
        }
    }

    /**
     * This method saves the summary report for the counts of the CGD in each institutions.
     *
     * @param type the type
     */
    public void saveCGDUpdatedSummaryReport(String type) {
        MatchingAlgorithmReportEntity reportEntity = new MatchingAlgorithmReportEntity();
        reportEntity.setType(type);
        reportEntity.setFileName(ScsbConstants.SUMMARY_REPORT_FILE_NAME);
        reportEntity.setCreatedDate(new Date());
        reportEntity.setInstitutionName(ScsbCommonConstants.ALL_INST);
        List<MatchingAlgorithmReportDataEntity> reportDataEntities = new ArrayList<>();
        List<String> allInstitutionCodesExceptSupportInstitution = commonUtil.findAllInstitutionCodesExceptSupportInstitution();
        for (String institutionCode : allInstitutionCodesExceptSupportInstitution) {
            logger.info("{} Final Counter Value:{} " ,institutionCode, MatchingCounter.getSpecificInstitutionCounterMap(institutionCode).get(MATCHING_COUNTER_SHARED));
            getReportDataEntity(institutionCode+"SharedCount", String.valueOf(MatchingCounter.getSpecificInstitutionCounterMap(institutionCode).get(MATCHING_COUNTER_UPDATED_SHARED)), reportDataEntities);
            getReportDataEntity(institutionCode+"OpenCount", String.valueOf(MatchingCounter.getSpecificInstitutionCounterMap(institutionCode).get(MATCHING_COUNTER_UPDATED_OPEN)), reportDataEntities);
        }
        reportEntity.addAll(reportDataEntities);
        getMatchingAlgorithmReportDetailRepository().save(reportEntity);
    }

    /**
     * This method populates matching counter for Ongoing Matching Algorithm to process the CGD update in the matching algorithm.
     *
     * @throws IOException         the io exception
     * @throws SolrServerException the solr server exception
     */
    public void populateMatchingCounter() throws IOException, SolrServerException {
        List<String> institutions = commonUtil.findAllInstitutionCodesExceptSupportInstitution();
        MatchingCounter.setScsbInstitutions(institutions);
        MatchingCounter.reset();
        for (String institution : institutions) {
            synchronized (MatchingCounter.class) {
                Map<String, Integer> specificInstitutionCounterMap = MatchingCounter.getSpecificInstitutionCounterMap(institution);
                specificInstitutionCounterMap.put(MATCHING_COUNTER_SHARED, getCGDCountBasedOnInst(institution, ScsbConstants.SHARED));
                specificInstitutionCounterMap.put(MATCHING_COUNTER_OPEN, getCGDCountBasedOnInst(institution, ScsbConstants.OPEN));
                logger.info("{} Initial Counter Value: {}",institution,specificInstitutionCounterMap.get(MATCHING_COUNTER_SHARED));
                MatchingCounter.setSpecificInstitutionCounterMap(institution, specificInstitutionCounterMap);
            }
        }
    }

    public Map<Integer, BibliographicEntity> getBibIdAndBibEntityMap(Set<String> bibIdsList){
        List<BibliographicEntity> bibliographicEntityList = bibliographicDetailsRepository.findByIdIn(bibIdsList.stream().map(s -> Integer.valueOf(s)).collect(toList()));
        Map<Integer, BibliographicEntity> bibliographicEntityMap = bibliographicEntityList.stream().collect(Collectors.toMap(BibliographicEntity::getId, Function.identity()));
        return bibliographicEntityMap;
    }

    public Map<Integer, BibliographicEntity> getbibIdAndBibMap(Set<Integer> bibIdsList){
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        logger.info("Fetching Bibs for Matching");
        List<BibliographicEntity> bibliographicEntityList = bibliographicDetailsRepository.findByOwningInstitutionIdInAndIdIn(commonUtil.findAllInstitutionIdsExceptSupportInstitution(),bibIdsList.parallelStream().collect(toList()));
        stopWatch.stop();
        logger.info("Totat time taken to fetch {} bibs is {}",bibliographicEntityList.size(),stopWatch.getTotalTimeSeconds());
        Map<Integer, BibliographicEntity> bibliographicEntityMap = bibliographicEntityList.stream().collect(Collectors.toMap(BibliographicEntity::getId, Function.identity()));
        return bibliographicEntityMap;
    }

    public Set<Integer> extractBibIdsFromMatchScoreReports(List<MatchScoreReport> matchScoreReportList) {
        return matchScoreReportList.stream()
                .flatMap(matchScoreReport -> matchScoreReport.getBibIds().stream())
                .collect(Collectors.toSet());
    }

    public Optional<Map<Integer,BibliographicEntity>> groupBibsForInitialMatching(List<BibliographicEntity> bibliographicEntityList, Integer matchScore) {
        List<BibliographicEntity> newlyGroupedBibs=new ArrayList<>();
        List<BibliographicEntity> updatedWithExistingGroupedBibs=new ArrayList<>();
        List<BibliographicEntity> combinedBibs=new ArrayList<>();

        Set<String> matchingIdentities = bibliographicEntityList.stream().
                filter(bibliographicEntity -> bibliographicEntity.getMatchingIdentity() != null)
                .map(bibliographicEntity -> bibliographicEntity.getMatchingIdentity())
                .collect(toSet());
        if (matchingIdentities.size() > 1) {
            combinedBibs = combineGroupedBibsForInitialMatching(matchingIdentities, bibliographicEntityList, matchScore);
        } else {

            String matchingIdentity = getMatchingIdentityValueForInitialMatching(bibliographicEntityList);

            Map<Boolean, List<BibliographicEntity>> partionedByMatchingIdentity = partitionBibsByMatchingIdentityForInitialMatching(bibliographicEntityList);

            if (CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(true))) {
                newlyGroupedBibs = initialMatchingroupBibsForNewEntries(matchScore, matchingIdentity, partionedByMatchingIdentity);
            }
            if (CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(false))) {
                updatedWithExistingGroupedBibs = intialMatchingGroupBibsForExistingEntries(matchScore, partionedByMatchingIdentity, matchingIdentity);
            }
        }
        return Optional.ofNullable(Stream.of(newlyGroupedBibs,updatedWithExistingGroupedBibs,combinedBibs)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(BibliographicEntity::getId,Function.identity(),(oldValue,newValue)->newValue)));
    }

    public Optional<Map<Integer,BibliographicEntityForMatching>> updateBibsForMatchingIdentifier(List<BibliographicEntityForMatching> bibliographicEntityList, Map<Integer, BibItem> bibItemMap) {
        List<BibliographicEntityForMatching> newlyGroupedBibs=new ArrayList<>();
        List<BibliographicEntityForMatching> updatedWithExistingGroupedBibs=new ArrayList<>();
        List<BibliographicEntityForMatching> combinedBibs=new ArrayList<>();

        Set<String> matchingIdentities = bibliographicEntityList.stream().
                filter(bibliographicEntity -> bibliographicEntity.getMatchingIdentity() != null)
                .map(bibliographicEntity -> bibliographicEntity.getMatchingIdentity())
                .collect(toSet());
        if(matchingIdentities.size()>1){
            combinedBibs=combineGroupedBibs(matchingIdentities,bibliographicEntityList,bibItemMap);
        }else {
            String matchingIdentity = getMatchingIdentityValue(bibliographicEntityList);
            Map<Boolean, List<BibliographicEntityForMatching>> partionedByMatchingIdentity = partitionBibsByMatchingIdentity(bibliographicEntityList);

            if (CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(true))) {
                newlyGroupedBibs = groupCGDForNewEntries(bibItemMap, matchingIdentity, partionedByMatchingIdentity);
            }
            if (CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(false))) {
                updatedWithExistingGroupedBibs = groupCGDForExistingEntries(bibItemMap, partionedByMatchingIdentity, matchingIdentity);
            }
        }
        return Optional.ofNullable(Stream.of(newlyGroupedBibs,updatedWithExistingGroupedBibs,combinedBibs)
                .flatMap(Collection::stream)
                .collect(Collectors.toMap(BibliographicEntityForMatching::getBibliographicId,Function.identity(),(oldValue,newValue)->newValue)));
    }

    private List<BibliographicEntityForMatching> combineGroupedBibs(Set<String> matchingIdentities, List<BibliographicEntityForMatching> bibliographicEntityList, Map<Integer, BibItem> bibItemMap) {
        List<Integer> allInstitutionIdsExceptSupportInstitution = commonUtil.findAllInstitutionIdsExceptSupportInstitution();
        List<String> matchingIdentifiers = matchingIdentities.stream().collect(toList());
        List<BibliographicEntityForMatching> existingGroupedBibs = bibliographicDetailsRepositoryForMatching.findByOwningInstitutionIdInAndMatchingIdentityIn(allInstitutionIdsExceptSupportInstitution, matchingIdentifiers);
        List<BibliographicEntityForMatching> updatedCollectedBibs = bibliographicEntityList.stream()
                .filter(bibliographicEntity -> null != bibItemMap.get(bibliographicEntity.getBibliographicId()) && !(bibliographicEntity.getMatchScore() == bibItemMap.get(bibliographicEntity.getBibliographicId()).getMatchScore()))
                .map(bibliographicEntity -> {
                   // bibliographicEntity.setAnamolyFlag(true);
                    String updatedMatchScore = MatchScoreUtil.calculateMatchScore(MatchScoreUtil.convertDecimalToBinary(bibItemMap.get(bibliographicEntity.getBibliographicId()).getMatchScore()), MatchScoreUtil.convertDecimalToBinary(bibliographicEntity.getMatchScore()));
                    bibliographicEntity.setMatchScore(MatchScoreUtil.convertBinaryToDecimal(updatedMatchScore));
                    return bibliographicEntity;
                })
                .collect(toList());
        String matchingIdentifier = matchingIdentifiers.stream().findFirst().get();
        return Stream.of(existingGroupedBibs,updatedCollectedBibs)
                .flatMap(bibliographicEntities -> bibliographicEntities.stream())
                .map(bibliographicEntity -> {
                    bibliographicEntity.setMatchingIdentity(matchingIdentifier);
              //      bibliographicEntity.setAnamolyFlag(true);
                    return bibliographicEntity;
                })
                .collect(Collectors.toList());

    }

    private List<BibliographicEntity> combineGroupedBibsForInitialMatching(Set<String> matchingIdentities, List<BibliographicEntity> bibliographicEntityList,Integer matchScore) {
        List<Integer> allInstitutionIdsExceptSupportInstitution = commonUtil.findAllInstitutionIdsExceptSupportInstitution();
        List<String> matchingIdentifiers = matchingIdentities.stream().collect(toList());
        List<BibliographicEntity> existingGroupedBibs = bibliographicDetailsRepository.findByOwningInstitutionIdInAndMatchingIdentityIn(allInstitutionIdsExceptSupportInstitution, matchingIdentifiers);
        List<BibliographicEntity> updatedCollectedBibs = bibliographicEntityList.stream()
                .filter(bibliographicEntity -> !(bibliographicEntity.getMatchScore() == matchScore))
                .map(bibliographicEntity -> {
                //    bibliographicEntity.setAnamolyFlag(true);
                    String updatedMatchScore = MatchScoreUtil.calculateMatchScore(MatchScoreUtil.convertDecimalToBinary(matchScore), MatchScoreUtil.convertDecimalToBinary(bibliographicEntity.getMatchScore()));
                    bibliographicEntity.setMatchScore(MatchScoreUtil.convertBinaryToDecimal(updatedMatchScore));
                    return bibliographicEntity;
                })
                .collect(toList());
        String matchingIdentifier = matchingIdentifiers.stream().findFirst().get();
        return Stream.of(existingGroupedBibs,updatedCollectedBibs)
                .flatMap(bibliographicEntities -> bibliographicEntities.stream())
                .map(bibliographicEntity -> {
                    bibliographicEntity.setMatchingIdentity(matchingIdentifier);
                //    bibliographicEntity.setAnamolyFlag(true);
                    return bibliographicEntity;
                })
                .collect(Collectors.toList());

    }


    private Map<Boolean, List<BibliographicEntity>> partitionBibsByMatchingIdentityForInitialMatching(List<BibliographicEntity> bibliographicEntityList) {
        return bibliographicEntityList.stream()
                .collect(Collectors.partitioningBy(bibliographicEntity -> StringUtils.isEmpty(bibliographicEntity.getMatchingIdentity())));
    }

    private Map<Boolean, List<BibliographicEntityForMatching>> partitionBibsByMatchingIdentity(List<BibliographicEntityForMatching> bibliographicEntityList) {
        return bibliographicEntityList.stream()
                .collect(Collectors.partitioningBy(bibliographicEntity -> StringUtils.isEmpty(bibliographicEntity.getMatchingIdentity())));
    }

    private List<BibliographicEntity> intialMatchingGroupBibsForExistingEntries(Integer matchScore, Map<Boolean, List<BibliographicEntity>> partionedByMatchingIdentity,String matchingIdentity) {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();

       // To find and update bibs for anamoly flag if required with existing matching identifier
        List<BibliographicEntity> bibliographicEntityListWithExistingMatchingIdentifier = bibliographicDetailsRepository.findByOwningInstitutionIdInAndMatchingIdentity(commonUtil.findAllInstitutionIdsExceptSupportInstitution(),matchingIdentity);

        List<BibliographicEntity> bibliographicEntities = partionedByMatchingIdentity.get(false);
        Set<Integer> matchScores = bibliographicEntities.stream().map(bibliographicEntity -> bibliographicEntity.getMatchScore()).collect(toSet());
     //   boolean isAnamolyFlagUpdateNeeded=(!(matchScores.size()==1 && matchScores.contains(matchScore)))?true:false;
       // if (isAnamolyFlagUpdateNeeded){
            List<Integer> bibIdsFromPartitionedByMatchingIdentity = bibliographicEntities.stream().map(bibliographicEntity -> bibliographicEntity.getId()).collect(toList());
            List<Integer> bibIdsExisting = bibliographicEntityListWithExistingMatchingIdentifier.stream().map(bibliographicEntity -> bibliographicEntity.getId()).collect(toList());
           // List<Integer> bibIdsToUpdateAnamolyFlag = Stream.of(bibIdsFromPartitionedByMatchingIdentity, bibIdsExisting).flatMap(bibIds -> bibIds.stream()).collect(toList());
        //    bibliographicDetailsRepository.updateAnamolyFlag(bibIdsToUpdateAnamolyFlag);
       // }
        List<BibliographicEntity> modifiedBibs = partionedByMatchingIdentity.get(false).stream()
                .filter(bibliographicEntity -> !(bibliographicEntity.getMatchScore() == matchScore))
                .map(bibliographicEntity -> {
        /*            if(isAnamolyFlagUpdateNeeded){
                        bibliographicEntity.setAnamolyFlag(true);
                    }
        */          String updatedMatchScore = MatchScoreUtil.calculateMatchScore(MatchScoreUtil.convertDecimalToBinary(matchScore), MatchScoreUtil.convertDecimalToBinary(bibliographicEntity.getMatchScore()));
                    bibliographicEntity.setMatchScore(MatchScoreUtil.convertBinaryToDecimal(updatedMatchScore));
                    return bibliographicEntity;
                })
                .collect(toList());
        stopWatch.stop();
        return modifiedBibs;
    }

    private List<BibliographicEntityForMatching> groupCGDForExistingEntries(Map<Integer, BibItem> bibItemMap, Map<Boolean, List<BibliographicEntityForMatching>> partionedByMatchingIdentity,String matchingIdentity) {
        StopWatch stopWatch=new StopWatch();
        stopWatch.start();
        // To find and update bibs for anamoly flag if required with existing matching identifier
        List<BibliographicEntityForMatching> bibliographicEntityListWithExistingMatchingIdentifier = bibliographicDetailsRepositoryForMatching.findByOwningInstitutionIdInAndMatchingIdentity(commonUtil.findAllInstitutionIdsExceptSupportInstitution(),matchingIdentity);

        List<BibliographicEntityForMatching> bibliographicEntities = partionedByMatchingIdentity.get(false);
        Set<Integer> matchScores = bibliographicEntities.stream().map(bibliographicEntity -> bibliographicEntity.getMatchScore()).collect(toSet());
        Set<Integer> matchScoresFromBibItemMap = bibItemMap.values().stream().map(bibItem -> bibItem.getMatchScore()).collect(toSet());
        boolean isAnamolyFlagUpdateNeeded=false;
        if(!(((matchScores.size()==1) && (matchScoresFromBibItemMap.size()==1)) && CollectionUtils.containsAny(matchScores,matchScoresFromBibItemMap))){
            isAnamolyFlagUpdateNeeded=true;
        }
        /*if (isAnamolyFlagUpdateNeeded){
            List<Integer> bibIdsFromPartitionedByMatchingIdentity = bibliographicEntities.stream().map(bibliographicEntity -> bibliographicEntity.getId()).collect(toList());
            List<Integer> bibIdsExisting = bibliographicEntityListWithExistingMatchingIdentifier.stream().map(bibliographicEntity -> bibliographicEntity.getId()).collect(toList());
            List<Integer> bibIdsToUpdateAnamolyFlag = Stream.of(bibIdsFromPartitionedByMatchingIdentity, bibIdsExisting).flatMap(bibIds -> bibIds.stream()).collect(toList());
            bibliographicDetailsRepository.updateAnamolyFlag(bibIdsToUpdateAnamolyFlag);
        }*/
      //  boolean finalIsAnamolyFlagUpdateNeeded = isAnamolyFlagUpdateNeeded;
        List<BibliographicEntityForMatching> modifiedBibs = partionedByMatchingIdentity.get(false).stream()
                .filter(bibliographicEntity -> null != bibItemMap.get(bibliographicEntity.getBibliographicId()) && !(bibliographicEntity.getMatchScore() == bibItemMap.get(bibliographicEntity.getBibliographicId()).getMatchScore()))
                .map(bibliographicEntity -> {
        /*            if(finalIsAnamolyFlagUpdateNeeded){
                        bibliographicEntity.setAnamolyFlag(true);
                    }
        */          String updatedMatchScore = MatchScoreUtil.calculateMatchScore(MatchScoreUtil.convertDecimalToBinary(bibItemMap.get(bibliographicEntity.getBibliographicId()).getMatchScore()), MatchScoreUtil.convertDecimalToBinary(bibliographicEntity.getMatchScore()));
                    bibliographicEntity.setMatchScore(MatchScoreUtil.convertBinaryToDecimal(updatedMatchScore));
                    return bibliographicEntity;
                })
                .collect(toList());
        //     bibliographicEntity.setAnamolyFlag(true);
        List<BibliographicEntityForMatching> finalBibList = Stream.of(bibliographicEntityListWithExistingMatchingIdentifier, modifiedBibs)
                .flatMap(bibliographicEntityList -> bibliographicEntityList.stream())
                .collect(toList());

        stopWatch.stop();
        return finalBibList;
    }

    private List<BibliographicEntity> initialMatchingroupBibsForNewEntries(Integer matchScore, String matchingIdentity, Map<Boolean, List<BibliographicEntity>> partionedByMatchingIdentity) {
     /*   boolean isAnamolyFlag=false;
        if(CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(false))){
            isAnamolyFlag=true;
        }
        boolean finalIsAnamolyFlag = isAnamolyFlag;
     */
        return partionedByMatchingIdentity.get(true).stream()
                .map(bibliographicEntity -> {
                   /* if(finalIsAnamolyFlag && !(bibliographicEntity.getMatchScore()==matchScore)){
                        bibliographicEntity.setAnamolyFlag(true);
                    }
*/
                    bibliographicEntity.setMatchScore(matchScore);
                    bibliographicEntity.setMatchingIdentity(matchingIdentity);
                    return bibliographicEntity;
                })
                .collect(toList());
    }
    private List<BibliographicEntityForMatching> groupCGDForNewEntries(Map<Integer, BibItem> bibItemMap, String matchingIdentity, Map<Boolean, List<BibliographicEntityForMatching>> partionedByMatchingIdentity) {
      /*  boolean isAnamolyFlag=false;
        if(CollectionUtils.isNotEmpty(partionedByMatchingIdentity.get(false))){
            isAnamolyFlag=true;
        }
        boolean finalIsAnamolyFlag = isAnamolyFlag;
      */
        List<BibliographicEntityForMatching> bibliographicEntities = partionedByMatchingIdentity.get(true);
        return bibliographicEntities.stream()
                .map(bibliographicEntity -> {
                    BibItem bibItem = bibItemMap.get(bibliographicEntity.getBibliographicId());
        /*            if(finalIsAnamolyFlag){
                        bibliographicEntity.setAnamolyFlag(true);
                    }
        */          if(bibItem != null ){
                        bibliographicEntity.setMatchScore(bibItem.getMatchScore());
                    }
                    else {
                        bibliographicEntity.setMatchScore(0);
                    }
                    bibliographicEntity.setMatchingIdentity(matchingIdentity);
                    return bibliographicEntity;
                })
                .collect(toList());
    }

    private String getMatchingIdentityValue(List<BibliographicEntityForMatching> bibliographicEntityList) {
        Optional<BibliographicEntityForMatching> existingIdentifier = bibliographicEntityList.stream()
                .filter(bibliographicEntity -> StringUtils.isNotEmpty(bibliographicEntity.getMatchingIdentity()))
                .findFirst();
        String matchingIdentity = existingIdentifier.map(BibliographicEntityForMatching::getMatchingIdentity).orElseGet(() -> UUID.randomUUID().toString());
        return matchingIdentity;
    }

    private String getMatchingIdentityValueForInitialMatching(List<BibliographicEntity> bibliographicEntityList) {
        Optional<BibliographicEntity> existingIdentifier = bibliographicEntityList.stream()
                .filter(bibliographicEntity -> StringUtils.isNotEmpty(bibliographicEntity.getMatchingIdentity()))
                .findFirst();
        String matchingIdentity = existingIdentifier.map(BibliographicEntity::getMatchingIdentity).orElseGet(() -> UUID.randomUUID().toString());
        return matchingIdentity;
    }

    public Optional<Set<Integer>> updateBibForMatchingIdentifier(List<Integer> bibIdList) {
        List<BibliographicEntity> bibliographicEntityList = bibliographicDetailsRepository.findByIdIn(bibIdList);
        Optional<BibliographicEntity> existingIdentifier = bibliographicEntityList.stream().filter(bibliographicEntity -> StringUtils.isNotEmpty(bibliographicEntity.getMatchingIdentity())).findFirst();
        String matchingIdentity = existingIdentifier.map(BibliographicEntity::getMatchingIdentity).orElseGet(() -> UUID.randomUUID().toString());
        List<BibliographicEntity> bibliographicEntitiesToUpdate = bibliographicEntityList.stream()
                .filter(bibliographicEntity -> StringUtils.isEmpty(bibliographicEntity.getMatchingIdentity()))
                .map(bibliographicEntity -> {
                    bibliographicEntity.setMatchingIdentity(matchingIdentity);
                    return bibliographicEntity;
                })
                .collect(toList());
        if(!bibliographicEntitiesToUpdate.isEmpty()){
            logger.info("No of grouped bibs to save and index : {}",bibliographicEntitiesToUpdate.stream().count());
            bibliographicDetailsRepository.saveAll(bibliographicEntitiesToUpdate);
        }
        return Optional.ofNullable(bibliographicEntitiesToUpdate.stream().map(BibliographicEntity::getId).collect(toSet()));
    }

    @Transactional
    public void saveGroupedBibsToDb(Collection<BibliographicEntity> bibliographicEntities) {
        logger.info("Saving grouped Bibliographic entities to DB . Total size of bibs : {}",bibliographicEntities.size());
        bibliographicDetailsRepository.saveAll(bibliographicEntities);
        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public void saveGroupedBibsToDbForOngoing(Collection<BibliographicEntityForMatching> bibliographicEntities) {
        logger.debug("Saving grouped Bibliographic entities to DB . Total size of bibs : {}",bibliographicEntities.size());
        bibliographicDetailsRepositoryForMatching.saveAll(bibliographicEntities);
        entityManager.flush();
        entityManager.clear();
    }

    public int removeMatchingIdsInDB() {
        return bibliographicDetailsRepository.removeMatchingIdentifiers();
    }

    public Set<Integer> getBibIdsToRemoveMatchingIdsInSolr() throws IOException, SolrServerException {
        Set<Integer> bibIds = new HashSet<>();
        SolrQuery solrQuery = solrQueryBuilder.solrQueryToFetchMatchedRecords();
        QueryResponse queryResponse = solrTemplate.getSolrClient().query(solrQuery);
        SolrDocumentList solrDocumentList = queryResponse.getResults();
        if (CollectionUtils.isNotEmpty(solrDocumentList)) {
            for (SolrDocument bibSolrDocument : solrDocumentList) {
                bibIds.add((Integer) bibSolrDocument.getFieldValue(ScsbConstants.BIB_ID));
            }
        }
        return bibIds;
    }

    public String indexBibs(List<Integer> bibIds) {
        logger.info("Total number of BibIds received to index : {}", bibIds.size());
        if (!bibIds.isEmpty() && bibIds.size() >= 1000) {
            SolrIndexRequest solrIndexRequest = new SolrIndexRequest();
            solrIndexRequest.setNumberOfThreads(1);
            solrIndexRequest.setNumberOfDocs(1000);
            solrIndexRequest.setCommitInterval(10000);
            solrIndexRequest.setPartialIndexType("BibIdList");
            String collect = bibIds.stream().map(bibId -> String.valueOf(bibId)).collect(Collectors.joining(","));
            solrIndexRequest.setBibIds(collect);
            String bibsIndexed = bibItemIndexExecutorService.partialIndex(solrIndexRequest);
            logger.info("Status of Index : {}", bibsIndexed);
            return ScsbCommonConstants.SUCCESS;
        } else {
            String status = bibItemIndexExecutorService.indexByBibliographicId(bibIds);
            logger.info("Status of Index : {}, Total number of records indexed : {}", status, bibIds.size());
            return status;
        }
    }

    @Transactional
    public void resetMAQualifier(List<Integer> bibIds,boolean isCGDProcess) {
        logger.info("Updating MAQualifier for Bibs in DB. Total size of bibs : {}", bibIds.size());
        StopWatch stopWatchForMAQualifierUpdate=new StopWatch();
        stopWatchForMAQualifierUpdate.start();
        if (isCGDProcess) {
            bibliographicDetailsRepository.resetMAQualifier(bibIds, Collections.singletonList(ScsbCommonConstants.MA_QUALIFIER_2));
        } else {
            bibliographicDetailsRepository.resetMAQualifier(bibIds, Arrays.asList(ScsbCommonConstants.MA_QUALIFIER_1, ScsbCommonConstants.MA_QUALIFIER_3));
        }
        entityManager.flush();
        entityManager.clear();
        stopWatchForMAQualifierUpdate.stop();
        logger.info("Total time taken for updating MAQualifier in DB {} for size {}", stopWatchForMAQualifierUpdate.getTotalTimeSeconds(), bibIds.size());
    }

    @Transactional
    public void updateAnamolyFlagForBibs(List<Integer> bibIds) {
        logger.info("Updating Anamoly Flag for Bibs in DB. Total size of bibs : {}", bibIds.size());
        StopWatch stopWatchForAnamolyFlagUpdate = new StopWatch();
        stopWatchForAnamolyFlagUpdate.start();
        int countFirst = bibliographicDetailsRepository.updateAnamolyFlagFirstForBibIds(bibIds);
        logger.info("Total number of bibs updated with First Anamoly Flag Query: {}", countFirst);
        int countSecond = bibliographicDetailsRepository.updateAnamolyFlagSecondForBibIds(bibIds);
        logger.info("Total number of bibs updated with Second Anamoly Flag Query: {}", countSecond);
        stopWatchForAnamolyFlagUpdate.stop();
        logger.info("Total time taken for updating Anamoly Flag in DB {} for size {}", stopWatchForAnamolyFlagUpdate.getTotalTimeSeconds(), bibIds.size());
    }

    public Map<String, Integer> getMatchPointsCombinationMap() {
        Map<String, Integer> matchPointsCombinationMap = new HashMap<>();
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC + "," + ScsbCommonConstants.MATCH_POINT_FIELD_ISBN, MatchScoreUtil.OCLC_ISBN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC + "," + ScsbCommonConstants.MATCH_POINT_FIELD_ISSN, MatchScoreUtil.OCLC_ISSN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_OCLC + "," + ScsbCommonConstants.MATCH_POINT_FIELD_LCCN, MatchScoreUtil.OCLC_LCCN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE + "," + ScsbCommonConstants.MATCH_POINT_FIELD_OCLC, MatchScoreUtil.OCLC_TITLE_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN + "," + ScsbCommonConstants.MATCH_POINT_FIELD_ISSN, MatchScoreUtil.ISBN_ISSN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_ISBN + "," + ScsbCommonConstants.MATCH_POINT_FIELD_LCCN, MatchScoreUtil.ISBN_LCCN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE + "," + ScsbCommonConstants.MATCH_POINT_FIELD_ISBN, MatchScoreUtil.ISBN_TITLE_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_ISSN + "," + ScsbCommonConstants.MATCH_POINT_FIELD_LCCN, MatchScoreUtil.ISSN_LCCN_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE + "," + ScsbCommonConstants.MATCH_POINT_FIELD_ISSN, MatchScoreUtil.ISSN_TITLE_SCORE);
        matchPointsCombinationMap.put(ScsbCommonConstants.MATCH_POINT_FIELD_TITLE + "," + ScsbCommonConstants.MATCH_POINT_FIELD_LCCN, MatchScoreUtil.LCCN_TITLE_SCORE);
        return matchPointsCombinationMap;
    }
}
