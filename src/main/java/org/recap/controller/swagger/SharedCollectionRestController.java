package org.recap.controller.swagger;

import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.recap.RecapConstants;
import org.recap.service.ItemAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by chenchulakshmig on 6/10/16.
 */
@RestController
@RequestMapping("/sharedCollection")
@Api(value = "sharedCollection", description = "Shared Collection", position = 1)
public class SharedCollectionRestController {

    @Value("${server.protocol}")
    String serverProtocol;

    @Value("${scsb.persistence.url}")
    String scsbPersistenceUrl;

    @Autowired
    private ItemAvailabilityService itemAvailabilityService;

    @RequestMapping(value = "/itemAvailabilityStatus", method = RequestMethod.GET)
    @ApiOperation(value = "itemAvailabilityStatus",
            notes = "Item Availability Status", nickname = "itemAvailabilityStatus", position = 0)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @ResponseBody
    public ResponseEntity itemAvailabilityStatus(@ApiParam(value = "Item Barcode", required = true, name = "itemBarcode") @RequestParam String itemBarcode) {
        String itemStatus = null;
        try {
            itemStatus = itemAvailabilityService.getItemStatusByBarcodeAndIsDeletedFalse(itemBarcode);
        } catch (Exception exception) {
            ResponseEntity responseEntity = new ResponseEntity("Scsb Persistence Service is Unavailable.", getHttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE);
            return responseEntity;
        }
        if (StringUtils.isEmpty(itemStatus)) {
            ResponseEntity responseEntity = new ResponseEntity(RecapConstants.ITEM_BARCDE_DOESNOT_EXIST, getHttpHeaders(), HttpStatus.OK);
            return responseEntity;
        } else {
            ResponseEntity responseEntity = new ResponseEntity(itemStatus, getHttpHeaders(), HttpStatus.OK);
            return responseEntity;
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(RecapConstants.RESPONSE_DATE, new Date().toString());
        return responseHeaders;
    }
}
