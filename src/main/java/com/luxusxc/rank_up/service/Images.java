package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.ImageEntity;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import com.luxusxc.rank_up.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Images {
    private final ImageRepository imageRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String DELIMITER = "\n";

    public Images(ImageRepository imageRepository, StringSplitter stringSplitter, StringJoiner stringJoiner) {
        this.imageRepository = imageRepository;
        this.stringSplitter = stringSplitter;
        this.stringJoiner = stringJoiner;
    }

    public void importImages(WebRankUpConfig webConfig) {
        String imagesUrl = webConfig.getAttachedImagesUrl();
        List<String> urls = stringSplitter.split(imagesUrl, DELIMITER);
        List<ImageEntity> images = getImagesFromUrls(urls);
        imageRepository.deleteAll();
        saveImages(images);
    }

    private List<ImageEntity> getImagesFromUrls(List<String> urls) {
        return urls.stream().map(ImageEntity::new).toList();
    }

    private void saveImages(List<ImageEntity> images) {
        imageRepository.saveAll(images);
    }

    public String exportImages() {
        List<ImageEntity> images = (List<ImageEntity>) imageRepository.findAll();
        List<String> urls = getUrlsFromImages(images);
        return stringJoiner.join(urls, DELIMITER);
    }

    private List<String> getUrlsFromImages(List<ImageEntity> images) {
        List<String> urls = images.stream().map(ImageEntity::getImageUrl).toList();
        if (urls.contains(null)) {
            throw new NullPointerException();
        }
        return urls;
    }
}
