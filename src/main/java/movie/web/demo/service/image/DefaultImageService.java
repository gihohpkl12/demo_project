package movie.web.demo.service.image;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.domain.image.Image;
import movie.web.demo.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultImageService implements ImageService {

    private final String LOCAL_LOCATION = "C:\\Users\\gihoj\\IdeaProjects\\demo\\demo\\src\\main\\resources\\static\\upload_photo\\";
    private final String IMAGE_TAG_PRE_STRING = "<figure class=\"image\"><img src=\"https://demo-test-jjj2.s3.ap-northeast-2.amazonaws.com";
    private final String IMAGE_TAG_END_STRING = "\"></figure>";

    private final String BUCKED_DOMAIN = "https://demo-test-jjj2.s3.ap-northeast-2.amazonaws.com/";
    public final String IMAGE_PRE_STRING = "<figure class=\"image\"><img src=\"";

    private final ImageRepository imageRepository;

    @Override
    public String imageUpload(MultipartRequest request) throws IOException {
        return null;
    }

    /**
     * 이미지 명 저장 - DB
     * @param content
     */
    @Transactional
    public void imageUpload(String content) {
        List<Image> images = createImage(extractImageSrc(content));
        for (Image image : images) {
            imageRepository.save(image);
            deleteLocalImage(image);
        }
    }

    /**
     * 로컬에 이미지 삭제
     * @param image
     */
    private void deleteLocalImage(Image image) {
        String fileName = image.getS3Url().replace(BUCKED_DOMAIN, "");
        File localFolder = new File(LOCAL_LOCATION+fileName);
        File loaclFile = new File(LOCAL_LOCATION+fileName+"\\"+fileName);

        loaclFile.delete();
        localFolder.delete();

        image.getS3Url();
    }

    private List<Image> createImage(List<String> images) {
        List<Image> result = new ArrayList<>();
        for (String image : images) {
            result.add(new Image(image));
        }
        return result;
    }

    /**
     * 게시글의 내용에서 이미지 명 추출.
     * @param content
     * @return
     */
    private List<String> extractImageSrc(String content) {
        List<String> images = new ArrayList<>();

        int imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING);
        while(imageTagIndex != -1) {
            String image = content.substring(imageTagIndex+IMAGE_PRE_STRING.length(), content.indexOf(IMAGE_TAG_END_STRING, imageTagIndex));
            images.add(image);
            imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING, imageTagIndex+1);
        }

        return images;
    }

    public boolean isContainImage(String content) {
        if (content.indexOf(IMAGE_TAG_PRE_STRING) != -1) {
            return true;
        }

        return false;
    }

    @Transactional
    public void deleteImage(String content) {
        List<Image> result = imageRepository.findByImage(extractImageSrc(content));
        for (Image image : result) {
            imageRepository.delete(image);
        }
    }
}
