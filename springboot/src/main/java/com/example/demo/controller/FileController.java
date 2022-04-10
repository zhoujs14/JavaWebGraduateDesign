package com.example.demo.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.example.demo.common.Result;
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
    private static final String port="9090";
    private static final String ip="http://localhost";

    /**
     * 文件上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @CrossOrigin //允许跨域
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

    /**
     * 删除旧的用户头像
     * @param url 旧头像访问地址
     */
    public static void removeOldAvatar(String url){
        //获取头像存储路径
        String basePath=System.getProperty("user.dir")+"/springboot/src/main/resources/files/";
        List<String> fileNames=FileUtil.listFileNames(basePath);
        //根据url提取旧头像uuid
        String uuid=StrUtil.removePrefix(url,ip+":"+port+"/files/");
        //根据uuid删除旧头像文件
        String fileName=fileNames.stream().filter(name->name.contains(uuid)).findAny().orElse("");
        if(fileName!="") FileUtil.del(basePath+fileName);
    }
}
