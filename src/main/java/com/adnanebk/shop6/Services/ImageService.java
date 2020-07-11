//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.adnanebk.shop6.Services;

import com.adnanebk.shop6.Models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.processing.Filer;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class ImageService {


    private ResourceLoader resourceLoader;


    @Autowired
    public ImageService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    public  String HandleImage(String imageUrl, String name) {
        if (!imageUrl.isEmpty() && !imageUrl.toLowerCase().endsWith("jpg") && !imageUrl.toLowerCase().endsWith("png") && !imageUrl.toLowerCase().endsWith("jpeg")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, name + " is not a correct url");
        }
        if(!imageUrl.isEmpty() && !imageUrl.startsWith("/uploadingDir") &&!imageUrl.toLowerCase().startsWith("http") && !imageUrl.toLowerCase().startsWith("//") && !imageUrl.toLowerCase().startsWith("www."))
           return "/uploadingDir/"+imageUrl;
        return imageUrl;
    }

   @Async
    public  void cleanResources( List<Product> products) throws IOException {

        String resoureString = resourceLoader.getResource("classpath:/static").getFile().getAbsolutePath() + "/uploadingDir/";
        // Stream paths = Files.walk(Paths.get(resoureString));
        List<String> prodUrls=   products.stream().
                flatMap(p-> Stream.of(p.getImageUrl(),p.getImageUrl2(),p.getImageUrl3(),p.getImageUrl4())).collect(Collectors.toList());
        Files.list(Paths.get(resoureString)).
                forEach(file-> {

                    boolean test=prodUrls.stream().anyMatch(p->p.toLowerCase().endsWith(file.getFileName().toString().toLowerCase()));



                    if(!test)
                        try {
                            Files.deleteIfExists(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                });
    }



    public void CreateImages(MultipartFile[] images) throws IOException {
        String resoureString;
        MultipartFile[] var3 = images;
        int var4 = images.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            MultipartFile uploadedFile = var3[var5];
            String fileName = uploadedFile.getOriginalFilename();

                String dir = resourceLoader.getResource("classpath:/static").getFile().getAbsolutePath();
                resoureString = dir + "/uploadingDir/" + fileName;
                if (fileName != null && !fileName.isEmpty()) {
                            uploadedFile.transferTo(Paths.get(resoureString));
                }


        }
    }



}
