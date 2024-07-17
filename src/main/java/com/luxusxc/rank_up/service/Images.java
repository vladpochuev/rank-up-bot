package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.ImageEntity;
import com.luxusxc.rank_up.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Images {
    private final ImageRepository imageRepository;

    public Images(RankUpConfig config) {
        imageRepository = config.getImageRepository();
    }

    public void importImages(String imagesUrl) {
        String[] imageUrlSplit = imagesUrl.split("\n");
        imageRepository.deleteAll();
        for (String imageUrl : imageUrlSplit) {
            ImageEntity imageEntity = new ImageEntity(imageUrl);
            imageRepository.save(imageEntity);
        }
    }

    public String exportImages() {
        StringBuilder builder = new StringBuilder();
        Iterable<ImageEntity> imageEntities = imageRepository.findAll();
        for (ImageEntity imageEntity : imageEntities) {
            builder.append(imageEntity.getImageUrl()).append("\n");
        }
        String result = builder.toString();
        return result.substring(0, result.length() - 2);
    }
}
