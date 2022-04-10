package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * 文件存储
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${server.port}")
    private String port;

    private static final String ip="http://localhost";

    /**
     * 文件上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws IOException {
        String fileName=file.getOriginalFilename();
        String uuid= IdUtil.fastSimpleUUID(); //生成uuid
        String rootFilePath =System.getProperty("user.dir")+"/springboot/src/main/resources/files/"+uuid+"_"+fileName;
        FileUtil.writeBytes(file.getBytes(),rootFilePath);

        return Result.success(ip+":"+port+"/files/"+uuid);
    }

    /**
     * 文件下载接口
     * @param response
     * @param uuid
     */
    @GetMapping("/{uuid}")
    public void getFiles(HttpServletResponse response,@PathVariable String uuid){
        OutputStream os;
        String basePath=System.getProperty("user.dir")+"/springboot/src/main/resources/files/"; //获取上传路径
        List<String> fileNames=FileUtil.listFileNames(basePath);
        String fileName=fileNames.stream().filter(name->name.contains(uuid)).findAny().orElse("");
        try{
            if(StrUtil.isNotEmpty(fileName)){
                response.addHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(fileName,"UTF-8"));
                response.setContentType("application/octet-stream");
                //读取文件字节流
                byte[] bytes=FileUtil.readBytes(basePath+fileName);
                os=response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        }catch (Exception e){
            System.out.println("下载文件失败");
        }
    }
}
