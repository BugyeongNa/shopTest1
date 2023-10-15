package com.springstudy.shop.controller;

import com.springstudy.shop.dto.upload.UploadFileDTO;
import com.springstudy.shop.dto.upload.UploadResultDTO;
import io.swagger.annotations.ApiOperation;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    // application.properties파일의 설정 정보를 읽어서 변수의 값으로
    // 사용할 수 있게 해주는 어노테이션
    @Value("${itemImgLocation}")
    private String uploadPath;

    @ApiOperation(value="Upload POST", notes = "POST방식으로 파일 등록")
    @PostMapping(value="/uplad",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){
        log.info("==> "+uploadFileDTO);

        final List<UploadResultDTO> list = new ArrayList<>();

        // 전송한(첨부파일) 파일있는지 체크
        if (uploadFileDTO.getFiles() != null){
            uploadFileDTO.getFiles().forEach(multipartFile -> {

                String originalName = multipartFile.getOriginalFilename();
                log.info("=> upload file name :"+originalName);

                // 이미지 이름 중복을 없애기 위해
                String uuid = UUID.randomUUID().toString();
                Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
                boolean image = false;

                try {
                    multipartFile.transferTo(savePath); // 파일 업로드 완료

                    // 이미지 파일인 경우 섬네일 파일 생성하기
                    if (Files.probeContentType(savePath).startsWith("image")){
                        image = true;
                        File thumbFile = new File(uploadPath, "s_"+uuid+"_"+originalName);
                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
                    }

                    list.add(UploadResultDTO.builder()
                                    .uuid(uuid)
                                    .fileName(originalName)
                                    .img(image)// true, false
                                    .build());

                } catch (IOException e){
                    e.printStackTrace();
                }
            });// end forEach()

            return list;
        } // end if

        return null;
    }

    // 첨부(이미지)파일 조회
    @ApiOperation(value="view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        HttpHeaders headers = new HttpHeaders();

        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }


    // 첨부파일 삭제
    @ApiOperation(value="remove 파일", notes = "DELETE방식으로 첨부파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String, Boolean> removeFile(@PathVariable String fileName){

        Resource resource = new FileSystemResource(uploadPath+File.separator+fileName);
        String resourceName = resource.getFilename();

        Map<String, Boolean> resultMap = new HashMap<>();
        boolean removed = false;


        try {
            // 컨텐츠 타입 추출
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete(); // 파일 삭제

            // 섬네일(이미지)파일이 존재하면
            if (contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                thumbnailFile.delete();
            }

        } catch (Exception e){
            log.error(e.getMessage());
        }

        // 파일 삭제 결과 값 저장
        resultMap.put("result", removed);

        return resultMap;
    }

}
