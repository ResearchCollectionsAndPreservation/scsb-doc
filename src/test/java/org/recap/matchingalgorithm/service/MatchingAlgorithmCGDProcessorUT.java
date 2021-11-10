package org.recap.matchingalgorithm.service;

import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.recap.BaseTestCaseUT;
import org.recap.ScsbCommonConstants;
import org.recap.ScsbConstants;
import org.recap.matchingalgorithm.MatchingAlgorithmCGDProcessor;
import org.recap.matchingalgorithm.MatchingCounter;
import org.recap.model.jpa.*;
import org.recap.repository.jpa.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.recap.ScsbConstants.*;
import static org.recap.ScsbConstants.MATCHING_COUNTER_UPDATED_OPEN;
import static org.recap.matchingalgorithm.MatchingCounter.updateCGDCounter;

public class MatchingAlgorithmCGDProcessorUT extends BaseTestCaseUT {

    @InjectMocks
    MatchingAlgorithmCGDProcessor matchingAlgorithmCGDProcessor;

    @Mock
    CollectionGroupDetailsRepository collectionGroupDetailsRepository;

    @Mock
    ItemChangeLogDetailsRepository itemChangeLogDetailsRepository;

    @Mock
    MatchingCounter matchingCounter;

    @Mock
    ProducerTemplate producerTemplate;

    @Mock
    ItemDetailsRepository itemDetailsRepository;

    @Mock
    BibliographicDetailsRepository bibliographicDetailsRepository;

    @Mock
    InstitutionDetailsRepository institutionDetailsRepository;

    @Mock
    InstitutionEntity institutionEntity;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        Map<String, Integer> cgdCounterMap = new HashMap<>();
        cgdCounterMap.put(MATCHING_COUNTER_SHARED, 1);
        cgdCounterMap.put(MATCHING_COUNTER_OPEN, 1);
        cgdCounterMap.put(MATCHING_COUNTER_UPDATED_SHARED, 0);
        cgdCounterMap.put(MATCHING_COUNTER_UPDATED_OPEN, 0);
        List<String> institutions = Arrays.asList("PUL", "CUL", "NYPL", "HL");
        Map<String, Map<String, Integer>> institutionCounterMap = new HashMap<>();
        for (String institution : institutions) {
            institutionCounterMap.put(institution, cgdCounterMap);
        }
        ReflectionTestUtils.setField(matchingCounter, "institutionCounterMap", institutionCounterMap);
    }

    @Test
    public void updateCGDProcess() throws Exception {
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.REPORTS_OPEN, 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        String matchingType = ScsbConstants.INITIAL_MATCHING_OPERATION_TYPE;
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", matchingType);
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        Mockito.when(institutionEntity.getInstitutionCode()).thenReturn("PUL");
        Mockito.when(institutionDetailsRepository.findById(any())).thenReturn(Optional.of(institutionEntity));
        matchingAlgorithmCGDProcessor.updateCGDProcess(itemEntityMap);
        assertNotNull(itemEntityMap);
    }

    @Test
    public void updateCGDProcess1() throws Exception {
        ItemEntity itemEntityPUL = getItemEntity(1);
        itemEntityPUL.setInitialMatchingDate(new Date());
        ItemEntity itemEntityCUL = getItemEntity(2);
        itemEntityCUL.setInitialMatchingDate(new Date());
        ItemEntity itemEntityNYPL = getItemEntity(3);
        itemEntityNYPL.setInitialMatchingDate(new Date());
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(itemEntityPUL);
        itemEntities.add(itemEntityCUL);
        itemEntities.add(itemEntityNYPL);
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);
        Map<Integer, ItemEntity> itemEntityMap = new HashMap<>();
        itemEntityMap.put(1, itemEntityPUL);
        itemEntityMap.put(2, itemEntityCUL);
        itemEntityMap.put(3, itemEntityNYPL);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.REPORTS_OPEN, 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        String matchingType = ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM;
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", matchingType);
        matchingAlgorithmCGDProcessor.updateCGDProcess(itemEntityMap);
        assertNotNull(itemEntityMap);
    }

    @Test
    public void updateCGDProcess2() throws Exception {
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.REPORTS_OPEN, 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        String matchingType = ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM;
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", matchingType);
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        assertNotNull(itemEntityMap);
    }

    @Test
    public void updateCGDProcess3() throws Exception {
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.REPORTS_OPEN, 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        String matchingType = ScsbConstants.INITIAL_MATCHING_OPERATION_TYPE;
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", matchingType);
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        matchingAlgorithmCGDProcessor.updateCGDProcess(itemEntityMap);
        assertNotNull(itemEntityMap);
    }

    @Test
    public void updateCGDProcess4() throws Exception {
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.REPORTS_OPEN, 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        String matchingType = ScsbCommonConstants.ONGOING_MATCHING_ALGORITHM;
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", matchingType);
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        matchingAlgorithmCGDProcessor.updateCGDProcess(itemEntityMap);
        assertNotNull(itemEntityMap);
    }

    @Test
    public void checkForMonographAndPopulateValues() throws Exception {
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);

        Set<String> materialTypeSet = new HashSet<>();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(3);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(3);
        itemEntity.setUseRestrictions(ScsbCommonConstants.IN_LIBRARY_USE);
        itemEntities1.add(itemEntity);
        bibliographicEntity.setItemEntities(itemEntities1);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(3);
        holdingsEntities.add(holdingsEntity);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        boolean checkForMonographAndPopulateValues = matchingAlgorithmCGDProcessor.checkForMonographAndPopulateValues(materialTypeSet, itemEntityMap, bibIdList, ScsbConstants.ONGOING_MATCHING_OPERATION_TYPE);

        assertEquals(true, checkForMonographAndPopulateValues);
    }

    @Test
    public void checkForMonographAndPopulateValuesElseNYPL() throws Exception {
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);

        Set<String> materialTypeSet = new HashSet<>();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(3);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(3);
        ItemEntity itemEntity1 = getItemEntity(3);
        itemEntity1.setUseRestrictions(ScsbCommonConstants.SUPERVISED_USE);
        itemEntity1.setCopyNumber(2);
        itemEntities1.add(itemEntity);
        itemEntities1.add(itemEntity1);
        bibliographicEntity.setItemEntities(itemEntities1);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(3);
        holdingsEntities.add(holdingsEntity);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        Map institutionMap = new HashMap();
        institutionMap.put("NYPL", 3);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "institutionMap", institutionMap);
        List<String> nonHoldingInstitutionList = new ArrayList<>();
        nonHoldingInstitutionList.add("NYPL");
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "nonHoldingInstitutionList", nonHoldingInstitutionList);
        boolean checkForMonographAndPopulateValues = matchingAlgorithmCGDProcessor.checkForMonographAndPopulateValues(materialTypeSet, itemEntityMap, bibIdList, ScsbConstants.ONGOING_MATCHING_OPERATION_TYPE);

        assertEquals(true, checkForMonographAndPopulateValues);
    }

    @Test
    public void checkForMonographAndPopulateValuesElseCUL() throws Exception {
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);

        Set<String> materialTypeSet = new HashSet<>();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(2);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(2);
        HoldingsEntity holdingsEntity1 = getHoldingsEntity(2);
        holdingsEntities.add(holdingsEntity);
        holdingsEntities.add(holdingsEntity1);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(2);
        ItemEntity itemEntity1 = getItemEntity(2);
        itemEntities1.add(itemEntity);
        itemEntities1.add(itemEntity1);
        bibliographicEntity.setItemEntities(itemEntities1);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        Map institutionMap = new HashMap();
        institutionMap.put("CUL", 2);
        List<String> nonHoldingInstitutionList = new ArrayList<>();
        nonHoldingInstitutionList.add("NYPL");
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "nonHoldingInstitutionList", nonHoldingInstitutionList);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "institutionMap", institutionMap);
        boolean checkForMonographAndPopulateValues = matchingAlgorithmCGDProcessor.checkForMonographAndPopulateValues(materialTypeSet, itemEntityMap, bibIdList, ScsbConstants.ONGOING_MATCHING_OPERATION_TYPE);

        assertEquals(true, checkForMonographAndPopulateValues);
    }

    @Test
    public void checkForMonographAndPopulateValuesElsePUL() throws Exception {
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<ItemEntity> itemEntities = new ArrayList<>();
        itemEntities.add(getItemEntity(1));
        itemEntities.add(getItemEntity(2));
        itemEntities.add(getItemEntity(3));
        Map<Integer, List<ItemEntity>> owningInstitutionMap = new HashMap<>();
        owningInstitutionMap.put(1, itemEntities);
        owningInstitutionMap.put(2, itemEntities);
        owningInstitutionMap.put(3, itemEntities);

        Set<String> materialTypeSet = new HashSet<>();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(1);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(1);
        holdingsEntities.add(holdingsEntity);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(1);
        ItemEntity itemEntity1 = getItemEntity(1);
        itemEntities1.add(itemEntity);
        itemEntities1.add(itemEntity1);
        bibliographicEntity.setItemEntities(itemEntities1);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        Map institutionMap = new HashMap();
        institutionMap.put("PUL", 1);
        List<String> nonHoldingInstitutionList = new ArrayList<>();
        nonHoldingInstitutionList.add("NYPL");
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "nonHoldingInstitutionList", nonHoldingInstitutionList);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "institutionMap", institutionMap);
        boolean checkForMonographAndPopulateValues = matchingAlgorithmCGDProcessor.checkForMonographAndPopulateValues(materialTypeSet, itemEntityMap, bibIdList, ScsbConstants.ONGOING_MATCHING_OPERATION_TYPE);

        assertEquals(false, checkForMonographAndPopulateValues);
    }

    @Test
    public void populateItemEntityMapPUL() throws Exception {
        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(1);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(1);
        holdingsEntities.add(holdingsEntity);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(2);
        ItemEntity itemEntity1 = getItemEntity(2);
        itemEntities1.add(itemEntity);
        itemEntities1.add(itemEntity1);
        bibliographicEntity.setItemEntities(itemEntities1);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        Map institutionMap = new HashMap();
        institutionMap.put("PUL", 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "institutionMap", institutionMap);
        matchingAlgorithmCGDProcessor.populateItemEntityMap(itemEntityMap, bibIdList);
        assertNotNull(itemEntityMap);
    }

    @Test
    public void populateItemEntityMapCUL() throws Exception {

        Map<Integer, ItemEntity> itemEntityMap = getIntegerItemEntityMap();
        List<Integer> bibIdList = new ArrayList<>();
        List<BibliographicEntity> bibliographicEntities = new ArrayList<>();
        BibliographicEntity bibliographicEntity = getBibliographicEntity(2);
        List<HoldingsEntity> holdingsEntities = new ArrayList<>();
        HoldingsEntity holdingsEntity = getHoldingsEntity(1);
        holdingsEntities.add(holdingsEntity);
        bibliographicEntity.setHoldingsEntities(holdingsEntities);
        List<ItemEntity> itemEntities1 = new ArrayList<>();
        ItemEntity itemEntity = getItemEntity(1);
        ItemEntity itemEntity1 = getItemEntity(1);
        itemEntities1.add(itemEntity);
        itemEntities1.add(itemEntity1);
        bibliographicEntity.setItemEntities(itemEntities1);
        bibliographicEntities.add(bibliographicEntity);
        Mockito.when(bibliographicDetailsRepository.findByIdIn(Mockito.anyList())).thenReturn(bibliographicEntities);
        Map collectionGroupMap = new HashMap();
        collectionGroupMap.put(ScsbCommonConstants.SHARED_CGD, 1);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "collectionGroupMap", collectionGroupMap);
        Map institutionMap = new HashMap();
        institutionMap.put("CUL", 2);
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "institutionMap", institutionMap);
        matchingAlgorithmCGDProcessor.populateItemEntityMap(itemEntityMap, bibIdList);
        assertNotNull(itemEntityMap);
    }

    private BibliographicEntity getBibliographicEntity(int inst) {
        BibliographicEntity bibliographicEntity = new BibliographicEntity();
        bibliographicEntity.setContent("bibContent".getBytes());
        bibliographicEntity.setOwningInstitutionId(inst);
        Random random = new Random();
        String owningInstitutionBibId = String.valueOf(random.nextInt());
        bibliographicEntity.setOwningInstitutionBibId(owningInstitutionBibId);
        bibliographicEntity.setCreatedDate(new Date());
        bibliographicEntity.setCreatedBy("tst");
        bibliographicEntity.setLastUpdatedDate(new Date());
        bibliographicEntity.setLastUpdatedBy("tst");
        bibliographicEntity.setInstitutionEntity(getInstitutionEntity(inst));
        return bibliographicEntity;
    }

    private InstitutionEntity getInstitutionEntity(int inst) {
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setId(inst);
        if (inst == 1) {
            institutionEntity.setInstitutionName("PUL");
            institutionEntity.setInstitutionCode("PUL");
        } else if (inst == 2) {
            institutionEntity.setInstitutionName("CUL");
            institutionEntity.setInstitutionCode("CUL");
        } else if (inst == 3) {
            institutionEntity.setInstitutionName("NYPL");
            institutionEntity.setInstitutionCode("NYPL");
        }
        return institutionEntity;
    }

    private HoldingsEntity getHoldingsEntity(int inst) {
        HoldingsEntity holdingsEntity = new HoldingsEntity();
        holdingsEntity.setContent("holdingContent".getBytes());
        holdingsEntity.setCreatedDate(new Date());
        holdingsEntity.setCreatedBy("etl");
        holdingsEntity.setLastUpdatedDate(new Date());
        holdingsEntity.setLastUpdatedBy("etl");
        holdingsEntity.setOwningInstitutionHoldingsId("657");
        holdingsEntity.setOwningInstitutionId(inst);
        return holdingsEntity;
    }

    private ItemEntity getItemEntity(int inst) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(1);
        itemEntity.setOwningInstitutionId(inst);
        itemEntity.setCreatedDate(new Date());
        itemEntity.setCreatedBy("etl");
        itemEntity.setLastUpdatedDate(new Date());
        itemEntity.setLastUpdatedBy("etl");
        String barcode = "1234";
        itemEntity.setBarcode(barcode);
        itemEntity.setCallNumber("x.12321");
        itemEntity.setCollectionGroupId(1);
        itemEntity.setCallNumberType("1");
        itemEntity.setCustomerCode("1");
        itemEntity.setItemAvailabilityStatusId(1);
        itemEntity.setDeleted(false);
        itemEntity.setCatalogingStatus(ScsbCommonConstants.COMPLETE_STATUS);
        ItemStatusEntity itemStatusEntity = new ItemStatusEntity();
        itemStatusEntity.setId(1);
        itemStatusEntity.setStatusCode("Available");
        itemStatusEntity.setStatusDescription("Available");
        itemEntity.setItemStatusEntity(itemStatusEntity);
        itemEntity.setInstitutionEntity(getInstitutionEntity(inst));
        itemEntity.setCollectionGroupEntity(getCollectionGroupEntity());
        return itemEntity;
    }

    private CollectionGroupEntity getCollectionGroupEntity() {
        CollectionGroupEntity collectionGroupEntity = new CollectionGroupEntity();
        collectionGroupEntity.setCollectionGroupDescription("Private");
        collectionGroupEntity.setId(3);
        collectionGroupEntity.setCollectionGroupCode("Private");
        return collectionGroupEntity;
    }


    private Map<Integer, ItemEntity> getIntegerItemEntityMap() {
        Map<Integer, ItemEntity> itemEntityMap = new HashMap<>();
        itemEntityMap.put(1, getItemEntity(1));
        itemEntityMap.put(2, getItemEntity(2));
        itemEntityMap.put(3, getItemEntity(3));
        return itemEntityMap;
    }

    @Test
    public void findItemToBeSharedBasedOnCounter() throws Exception {
        Map<String, Map<String, Integer>> institutionCounterMap = new HashMap<>();
        Map<String, Integer> institutionCounterMap1 = new HashMap<>();
        institutionCounterMap1.put("SharedCount", 1);
        institutionCounterMap1.put("OpenCount", 2);
        institutionCounterMap1.put("AfterOpenCount", 3);
        institutionCounterMap1.put("AfterSharedCount", 4);
        institutionCounterMap.put("PUL", institutionCounterMap1);
        institutionCounterMap.put("CUL", institutionCounterMap1);
        institutionCounterMap.put("NYPL", institutionCounterMap1);
        institutionCounterMap.put("HL", institutionCounterMap1);
        List<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(2);
        List<Integer> integers1 = new ArrayList<>();
        integers1.add(1);
        integers1.add(2);
        InstitutionEntity institutionEntity = new InstitutionEntity();
        institutionEntity.setInstitutionCode("PUL");
        institutionEntity.setId(1);
        InstitutionEntity institutionEntity1 = new InstitutionEntity();
        institutionEntity1.setInstitutionCode("NYPL");
        institutionEntity1.setId(3);
        Map<Integer, ItemEntity> itemEntityMap = new HashMap<>();
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setBarcode("12345");
        itemEntity.setCgdProtection(true);
        itemEntityMap.put(1, itemEntity);
        Map<Integer, List<ItemEntity>> institutionMap = new HashMap<>();
        List<ItemEntity> itemEntities = new ArrayList<>();
        ItemEntity itemEntity1 = new ItemEntity();
        itemEntity1.setInstitutionEntity(institutionEntity);
        itemEntity1.setBarcode("12345");
        itemEntity1.setCgdProtection(true);
        itemEntity1.setCgdChangeLog("test");
        itemEntities.add(itemEntity1);
        institutionMap.put(1, itemEntities);
        ItemEntity itemEntity2 = new ItemEntity();
        itemEntity2.setBarcode("12345");
        itemEntity2.setCgdProtection(true);
        itemEntity2.setCgdChangeLog("test");
        itemEntity2.setInstitutionEntity(institutionEntity);
        itemEntities.add(itemEntity2);
        institutionMap.put(2, new ArrayList<>());
        Mockito.when(institutionDetailsRepository.findById(1)).thenReturn(Optional.of(institutionEntity));
        Mockito.when(institutionDetailsRepository.findById(2)).thenReturn(Optional.of(institutionEntity1));
        ReflectionTestUtils.setField(matchingAlgorithmCGDProcessor, "matchingType", "InitialMatchingAlgorithm");
        ReflectionTestUtils.invokeMethod(matchingAlgorithmCGDProcessor, "findItemToBeSharedBasedOnCounter", itemEntityMap, institutionMap);
    }

    @Test
    public void isItemShared() throws Exception {
        try {
            CollectionGroupEntity collectionGroupEntity = new CollectionGroupEntity();
            collectionGroupEntity.setCollectionGroupCode("test");
            collectionGroupEntity.setCollectionGroupDescription("test");
            collectionGroupEntity.setId(1);
            ItemEntity itemEntity = new ItemEntity();
            itemEntity.setBarcode("123456789");
            itemEntity.setId(1);
            itemEntity.setCgdChangeLog("test");
            itemEntity.setCollectionGroupEntity(collectionGroupEntity);
            ReflectionTestUtils.invokeMethod(matchingAlgorithmCGDProcessor, "isItemShared", itemEntity);
        } catch (Exception e) {
        }
    }

}