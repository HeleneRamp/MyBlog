package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.ImageDTO;
import org.wildcodeschool.myblog.exception.ImageNotFoundException;
import org.wildcodeschool.myblog.mapper.ImageMapper;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageService(ImageRepository imageRepository, ImageMapper imageMapper) {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;
    }

    //DTO for get all images
    public List<ImageDTO> getAllImages() {
        List<Image> images = imageRepository.findAll();
        return images.stream().map(imageMapper::convertToDTO).collect(Collectors.toList());
    }

    //DTO for get image by id
    public ImageDTO getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ImageNotFoundException("L'image avec l'id " + id + " n'existe pas :("));
        if (image == null) {
            return null;
        }
        return imageMapper.convertToDTO(image);
    }

    //DTO for create an image
    public ImageDTO createImage(Image image) {
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    //DTO for update an image
    public ImageDTO updateImage(Long id, Image imageDetails) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ImageNotFoundException("L'image avec l'id " + id + " n'existe pas :("));
        image.setUrl(imageDetails.getUrl());
        Image savedImage = imageRepository.save(image);
        return imageMapper.convertToDTO(savedImage);
    }

    //DTO for delete image
    public boolean deleteImage(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(()-> new ImageNotFoundException("L'image avec l'id " + id + " n'existe pas :("));
        imageRepository.delete(image);
        return true;
    }
}
