package com.xmap.v04.controller;

import com.alibaba.fastjson.JSON;
import com.xmap.v04.entity.DataBlock;
import com.xmap.v04.entity.Folder;
import com.xmap.v04.entity.User;
import com.xmap.v04.exception.NotFoundException;
import com.xmap.v04.models.AddDataBlock;
import com.xmap.v04.service.DataBlockService;
import org.bouncycastle.asn1.x9.DHValidationParms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.xml.crypto.Data;

@RestController
@RequestMapping("/api/datablock")
public class DataBlockController {
    Logger logger = LoggerFactory.getLogger(DataBlockController.class);

    @Autowired
    private DataBlockService dataBlockService;

    @PostMapping()
    public ResponseEntity<DataBlock> addDataBlock(@RequestBody @Validated AddDataBlock addDataBlock) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        DataBlock dataBlock = dataBlockService.addDataBlock(addDataBlock, user.getId());
        return new ResponseEntity<>(dataBlock, HttpStatus.OK);
    }

    @PutMapping(value = "{id}/setProgress")
    public ResponseEntity<DataBlock> setDataBlockProgress(@PathVariable("id") Integer id, @RequestBody JSON progress) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getById(id);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        String progressJson = progress.toJSONString();
        DataBlock dataBlock = dataBlockService.setProgress(progressJson, id);
        return new ResponseEntity<>(dataBlock, HttpStatus.OK);
    }

    @PutMapping(value = "uuid/{uuid}/setProgress")
    public ResponseEntity<DataBlock> setDataBlockProgressByUuid(@PathVariable("uuid") String uuid, @RequestBody JSON progress) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getByUuid(uuid);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        String progressJson = progress.toJSONString();
        DataBlock dataBlock = dataBlockService.setProgress(progressJson, dataBlock2.getId());
        return new ResponseEntity<>(dataBlock, HttpStatus.OK);
    }

    @PutMapping(value = "{id}/setStatus")
    public ResponseEntity<DataBlock> setDataBlockStatus(@PathVariable("id") Integer id, @RequestParam Integer status) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getById(id);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        DataBlock dataBlock = dataBlockService.setStatus(status, id);;
        return new ResponseEntity<>(dataBlock, HttpStatus.OK);
    }

    @PutMapping(value = "uuid/{uuid}/setStatus")
    public ResponseEntity<DataBlock> setDataBlockStatusByUuid(@PathVariable("uuid") String uuid, @RequestParam Integer status) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getByUuid(uuid);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        DataBlock dataBlock = dataBlockService.setStatus(status, dataBlock2.getId());;
        return new ResponseEntity<>(dataBlock, HttpStatus.OK);
    }
    @PutMapping(value = "{id}/setMetaData")
    public ResponseEntity<DataBlock> setDataBlockMetaData(@PathVariable("id") Integer id, @RequestBody DataBlock dataBlock) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getById(id);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        DataBlock updataDataBlock = dataBlockService.setMetaData(dataBlock, id);
        return new ResponseEntity<>(updataDataBlock, HttpStatus.OK);
    }

    @PutMapping(value = "uuid/{uuid}/setMetaData")
    public ResponseEntity<DataBlock> setDataBlockMetaData(@PathVariable("uuid") String uuid, @RequestBody DataBlock dataBlock) throws Exception{
        DataBlock dataBlock2 = dataBlockService.getByUuid(uuid);
        if (dataBlock2 == null)
            throw new NotFoundException("DataBlock not found!");
        DataBlock updataDataBlock = dataBlockService.setMetaData(dataBlock, dataBlock2.getId());
        return new ResponseEntity<>(updataDataBlock, HttpStatus.OK);
    }
}
