package com.btbatux.dream_shops.service.image;

import com.btbatux.dream_shops.dto.ImageDto;
import com.btbatux.dream_shops.dto.ProductDto;
import com.btbatux.dream_shops.exception.ResourceNotFoundException;
import com.btbatux.dream_shops.model.Image;
import com.btbatux.dream_shops.model.Product;
import com.btbatux.dream_shops.repository.ImageRepository;
import com.btbatux.dream_shops.service.product.IProductService;

import com.btbatux.dream_shops.service.product.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService implements IImageSerice {

    private final ImageRepository imageRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;


    public ImageService(ImageRepository imageRepository, ProductService productService, ModelMapper modelMapper) {
        this.imageRepository = imageRepository;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }


    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Image not found " + id));
    }


    @Override
    public void deleteImageById(Long id) {
        imageRepository.delete(getImageById(id));
    }


    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        ProductDto productDto = productService.getProductById(productId);


        List<ImageDto> savedImagesDto = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                Image image = new Image();

                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(modelMapper.map(productDto,Product.class));

                Image savedImage = imageRepository.save(image);

                String downloadUrl = "/api/v1/images/image/download/" + savedImage.getId();
                savedImage.setDownloadUrl(downloadUrl);
                imageRepository.save(savedImage);

                ImageDto imageDto = new ImageDto();
                imageDto.setImageId(savedImage.getId());
                imageDto.setImageName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());

                savedImagesDto.add(imageDto);

            } catch (IOException | SQLException exception) {
                throw new RuntimeException(exception.getMessage());
            }
        }
        return savedImagesDto;
    }


    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        try {
            Image image = getImageById(imageId);
            image.setFileName(file.getOriginalFilename());
            image.setImage(new SerialBlob(file.getBytes()));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
