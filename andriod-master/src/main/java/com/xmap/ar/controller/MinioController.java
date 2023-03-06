package com.xmap.ar.controller;

import com.xmap.ar.model.Fileinfo;
import com.xmap.ar.util.MinioUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

@Api(tags = "文件操作接口")
@RestController
@RequestMapping(value = "api/minio")
public class MinioController {

    @Autowired
    MinioUtils minioUtil;

    @ApiOperation("上传一个文件")
    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public ResponseEntity<String> fileupload(@RequestParam MultipartFile uploadfile, @RequestParam String bucket,
                                             @RequestParam(required = false) String objectName) throws Exception {
        minioUtil.createBucket(bucket);
        if (objectName != null) {
            minioUtil.uploadFile(uploadfile.getInputStream(), bucket, objectName + "/" + uploadfile.getOriginalFilename());
        } else {
            minioUtil.uploadFile(uploadfile.getInputStream(), bucket, uploadfile.getOriginalFilename());
        }
        return new ResponseEntity<>("uploaded!", HttpStatus.OK);
    }

    @ApiOperation("列出所有的桶")
    @RequestMapping(value = "/listBuckets", method = RequestMethod.GET)
    public ResponseEntity<List<String>> listBuckets() throws Exception {
        return new ResponseEntity<>(minioUtil.listBuckets(), HttpStatus.OK);
    }

    @ApiOperation("递归列出一个桶中的所有文件和目录")
    @RequestMapping(value = "/listFiles", method = RequestMethod.GET)
    public ResponseEntity<List<Fileinfo>> listFiles(@RequestParam String bucket) throws Exception {
        return new ResponseEntity<>(minioUtil.listFiles(bucket), HttpStatus.OK);
    }

    @ApiOperation("下载一个文件")
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam String bucket, @RequestParam String objectName,
                             HttpServletResponse response) throws Exception {
        InputStream stream = minioUtil.download(bucket, objectName);
        ServletOutputStream output = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(objectName.substring(objectName.lastIndexOf("/") + 1), "UTF-8"));
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(stream, output);
    }


    @ApiOperation("删除一个文件")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public ResponseEntity<String> deleteFile(@RequestParam String bucket, @RequestParam String objectName) throws Exception {
        minioUtil.deleteObject(bucket, objectName);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }

    @ApiOperation("删除一个桶")
    @RequestMapping(value = "/deleteBucket", method = RequestMethod.GET)
    public ResponseEntity<String> deleteBucket(@RequestParam String bucket) throws Exception {
        minioUtil.deleteBucket(bucket);
        return new ResponseEntity<>("deleted!", HttpStatus.OK);
    }


    @ApiOperation("复制一个文件")
    @GetMapping("/copyObject")
    public ResponseEntity<String> copyObject(@RequestParam String sourceBucket, @RequestParam String sourceObject, @RequestParam String targetBucket, @RequestParam String targetObject) throws Exception {
        minioUtil.copyObject(sourceBucket, sourceObject, targetBucket, targetObject);
        return  new ResponseEntity<>("copyed!", HttpStatus.OK);
    }

    @GetMapping("/getObjectInfo")
    @ApiOperation("获取文件信息")
    public ResponseEntity<String> getObjectInfo(@RequestParam String bucket, @RequestParam String objectName) throws Exception {

        return new ResponseEntity<>(minioUtil.getObjectInfo(bucket, objectName), HttpStatus.OK);
    }

    @GetMapping("/getPresignedObjectUrl")
    @ApiOperation("获取一个连接以供下载")
    public ResponseEntity<String> getPresignedObjectUrl(@RequestParam String bucket, @RequestParam String objectName, @RequestParam Integer expires) throws Exception {

        return new ResponseEntity<>(minioUtil.getPresignedObjectUrl(bucket, objectName, expires), HttpStatus.OK);
    }

    @GetMapping("/listAllFile")
    @ApiOperation("获取minio中所有的文件")
    public ResponseEntity<List<Fileinfo>> listAllFile() throws Exception {
        return new ResponseEntity<>(minioUtil.listAllFile(), HttpStatus.OK);
    }



}
