package com.btbatux.dream_shops.service.image;
import com.btbatux.dream_shops.dto.ImageDto;
import com.btbatux.dream_shops.model.Image;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface IImageSerice {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);


    void updateImage(MultipartFile file, Long imageId);


}
