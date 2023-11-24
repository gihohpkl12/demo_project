package movie.web.demo.service.image;

import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ImageService {

//    public final String IMAGE_TAG_PRE_STRING = "<figure class=\"image\"><img src=\"https://demo-test-jjj2.s3.ap-northeast-2.amazonaws.com";
//
//    public final String IMAGE_TAG_END_STRING = "\"></figure>";
//
//    public final String IMAGE_PRE_STRING = "<figure class=\"image\"><img src=\"";

    public String imageUpload(MultipartRequest request) throws IOException;

    public void imageUpload(String content);

    public boolean isContainImage(String content);

    public void deleteImage(String content);

//    public default List<String> extractImageSrc(String content) {
//        List<String> images = new ArrayList<>();
//
//        int imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING);
//        while(imageTagIndex != -1) {
//            String image = content.substring(imageTagIndex+IMAGE_PRE_STRING.length(), content.indexOf(IMAGE_TAG_END_STRING, imageTagIndex));
//            images.add(image);
//            imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING, imageTagIndex+1);
//        }
//
//        return images;
//    }


}
