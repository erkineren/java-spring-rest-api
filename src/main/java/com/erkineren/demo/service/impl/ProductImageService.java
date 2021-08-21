package com.erkineren.demo.service.impl;

import com.erkineren.demo.persistence.model.entity.ProductImage;
import com.erkineren.demo.persistence.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductImageRepository productImageRepository;

    public List<ProductImage> saveAll(List<ProductImage> productImages){
        return productImageRepository.saveAll(productImages);
    }

//    public List<ProductImage> saveRemoteImages(Product product, List<String> imageUrls) {
//        ClassLoader classLoader = getClass().getClassLoader();
//
//
//        List<ProductImage> productImages = new ArrayList<ProductImage>();
//
//        imageUrls.forEach((im) -> {
//
//            try {
//                URI imageUri = new URI(im);
//
//                String newFileName = new File(imageUri.getPath()).getName();
////                String uniqueID = UUID.randomUUID().toString();
//
////                File file = new File(classLoader.getResource(".").getFile() + "/static/images/" + newFileName);
//                File file = new File("D:\\static\\images\\" + newFileName);
//
////                File folder = new ClassPathResource("static/images").getFile();
////                System.out.println("folder: " + folder.toURI());
////                System.out.println("newFileName: " + newFileName);
////
////                InputStream in = new URL(im).openStream();
////                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
//                try (BufferedInputStream in = new BufferedInputStream(new URL(im).openStream());
//                     FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//                    byte dataBuffer[] = new byte[1024];
//                    int bytesRead;
//                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//                        fileOutputStream.write(dataBuffer, 0, bytesRead);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                ProductImage productImage = new ProductImage();
//                productImage.setProduct(product);
//                productImage.setPath("images/" + newFileName);
//                productImageRepository.save(productImage);
//                productImages.add(productImage);
//            } catch ( URISyntaxException e) {
//                e.printStackTrace();
//            }
//
//        });
//
//        return productImages;
//    }
}
