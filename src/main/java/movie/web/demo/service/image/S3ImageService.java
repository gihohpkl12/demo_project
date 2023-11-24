package movie.web.demo.service.image;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import movie.web.demo.aws.AwsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AWS S3 서비스.
 * 이미지 저장.
 */
@RequiredArgsConstructor
@Service
public class S3ImageService implements ImageService {

    private final AwsConfig awsConfig;

    public final String IMAGE_TAG_PRE_STRING = "<figure class=\"image\"><img src=\"https://demo-test-jjj2.s3.ap-northeast-2.amazonaws.com/";

    public final String IMAGE_TAG_END_STRING = "\"></figure>";

    private final String LOCAL_LOCATION = "C:\\Users\\gihoj\\IdeaProjects\\demo\\demo\\src\\main\\resources\\static\\upload_photo\\";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 게시글에 사진을 첨부하면 로컬과 S3에 저장.
     * @param request
     * @return
     * @throws IOException
     */
    public String imageUpload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = LOCAL_LOCATION + uuidFileName + "\\"+uuidFileName;
        String localFolder = LOCAL_LOCATION + uuidFileName;

        File folder = new File(localFolder);
        folder.mkdir();

        File localFile = new File(localPath);
        file.transferTo(localFile);

        awsConfig.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = awsConfig.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        return s3Url;
    }

    @Override
    public void imageUpload(String content) {}

    @Override
    public boolean isContainImage(String content) {
        return false;
    }

    private List<String> extractImageSrc(String content) {
        List<String> images = new ArrayList<>();

        int imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING);
        while(imageTagIndex != -1) {
            String image = content.substring(imageTagIndex + IMAGE_TAG_PRE_STRING.length(), content.indexOf(IMAGE_TAG_END_STRING, imageTagIndex));
            images.add(image);
            imageTagIndex = content.indexOf(IMAGE_TAG_PRE_STRING, imageTagIndex+1);
        }

        return images;
    }

    @Override
    @Transactional
    public void deleteImage(String content) {
        List<String> images = extractImageSrc(content);
        try {
            for (String image : images) {
                awsConfig.amazonS3Client().deleteObject(bucket, image);
            }
        } catch (Exception e) {
            System.out.println("error "+e.getMessage());
        }
    }

    /**
     * 게시글에 이미지를 첨부하고, 게시글을 저장하지 않아서 남은 이미지들은
     * 정각 12시 5분에 생성 시간 기준 하루가 지났으면 로컬과 S3에서 삭제
     */
    @Scheduled(cron = "0 5 0 * * *")
    public void deleteImageBatch() {
        File defaultPosition = new File(LOCAL_LOCATION);

        for (File parent : defaultPosition.listFiles()) {
            for (File child : parent.listFiles()) {
                try {
                    FileTime fileTime = (FileTime) Files.getAttribute(Paths.get(child.getPath()), "creationTime");
                    LocalDateTime creationTime = LocalDateTime.ofInstant(fileTime.toInstant(), ZoneId.systemDefault());

                    if (LocalDateTime.now().isAfter(creationTime.plusDays(1))) {
                        awsConfig.amazonS3Client().deleteObject(bucket, child.getName());
                        child.delete();
                    }
                } catch (Exception e) {}

            }
            if (parent.listFiles().length == 0) {
                parent.delete();
            }
        }
    }
}
