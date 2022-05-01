package com.item.arrangement.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.item.arrangement.common.NonStaticResourceHttpRequestHandler;
import com.item.arrangement.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * 文件存储
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Value("9090")
    private String port;
    private static final String ip="http://localhost";

    @Autowired
    private NonStaticResourceHttpRequestHandler nonStaticResourceHttpRequestHandler;

    /**
     * 视频上传接口，直接保存到target目录
     * @param file
     * @return
     * @throws IOException
     */
    @CrossOrigin //允许跨域
    @PostMapping("/videoUpload")
    public Result<?> uploadVideo(MultipartFile file) {
        String fileName=file.getOriginalFilename();
        String uuid= IdUtil.fastSimpleUUID(); //生成uuid
        String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        String realPath = sourcePath +"files/"+uuid+"_"+fileName;

        try {
            FileUtil.writeBytes(file.getBytes(),realPath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("-1",e.getMessage());
        }

        return Result.success(ip+":"+port+"/files/video/"+uuid);
    }

    /**
     * 文件上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @CrossOrigin //允许跨域
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) {
        String fileName=file.getOriginalFilename();
        String uuid= IdUtil.fastSimpleUUID(); //生成uuid
        String rootFilePath =System.getProperty("user.dir")+"/springboot/src/main/resources/files/"+uuid+"_"+fileName;

        try {
            FileUtil.writeBytes(file.getBytes(),rootFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("-1",e.getMessage());
        }

        return Result.success(ip+":"+port+"/files/"+uuid);
    }

    /**
     * 文章图片上传接口
     * @param file
     * @return
     * @throws IOException
     */
    @CrossOrigin //允许跨域
    @PostMapping("/blogImageUpload")
    public JSON blogImageUpload(MultipartFile file) {
        //生成文件名
        String fileName=file.getOriginalFilename();
        String uuid= IdUtil.fastSimpleUUID();
        String rootFilePath =System.getProperty("user.dir")+"/springboot/src/main/resources/files/"+uuid+"_"+fileName;
        String url=ip+":"+port+"/files/"+uuid;

        JSONObject data=new JSONObject();
        JSONArray arr=new JSONArray();
        JSONObject json=new JSONObject();

        try{
            FileUtil.writeBytes(file.getBytes(),rootFilePath);
        } catch (IOException e) {
            e.printStackTrace();
            json.set("errno",1);
            json.set("message",e.getMessage());
            return json;
        }

        //返回JSON结果
        data.set("url",url);
        arr.add(data);
        json.set("errno",0);
        json.set("data",arr);
        return json;
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
            System.out.println("下载文件失败"+e.getMessage());
        }
    }

    /**
     * 视频下载接口
     * @param response
     * @param uuid
     */
    @GetMapping("/video/{uuid}")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, @PathVariable String uuid) throws IOException, ServletException {
        //String basePath=System.getProperty("user.dir")+"/springboot/src/main/resources/files/"; //获取上传路径
        //List<String> fileNames=FileUtil.listFileNames(basePath);
        //String fileName=fileNames.stream().filter(name->name.contains(uuid)).findAny().orElse("");

        String sourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1);
        List<String> fileNames=FileUtil.listFileNames(sourcePath+"/files/");
        String fileName=fileNames.stream().filter(name->name.contains(uuid)).findAny().orElse("");

        if(StrUtil.isNotEmpty(fileName)){
            //获取编译后 resources 文件夹的绝对地址
            String realPath = sourcePath +"files/"+fileName;
            Path filePath = Paths.get(realPath);

            if (Files.exists(filePath)) {
                String mimeType = Files.probeContentType(filePath);
                if (StrUtil.isNotEmpty(mimeType)) {
                    response.setContentType(mimeType);
                }
                request.setAttribute(NonStaticResourceHttpRequestHandler.ATTR_FILE, filePath);
                nonStaticResourceHttpRequestHandler.handleRequest(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
            }
        }
        else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
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
        String uuid=url.substring(url.lastIndexOf('/')+1);
        //根据uuid删除旧头像文件
        String fileName=fileNames.stream().filter(name->name.contains(uuid)).findAny().orElse("");
        if(fileName!="") FileUtil.del(basePath+fileName);
    }
}
