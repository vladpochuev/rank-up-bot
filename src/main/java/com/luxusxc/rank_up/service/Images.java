package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.ImageEntity;
import com.luxusxc.rank_up.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Images {
    private final ImageRepository imageRepository;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String SEPARATOR = "\n";

    public Images(RankUpConfig config, StringSplitter stringSplitter, StringJoiner stringJoiner) {
        imageRepository = config.getImageRepository();
        this.stringSplitter = stringSplitter;
        this.stringJoiner = stringJoiner;
    }

    public void importImages(String imagesUrl) {
        imageRepository.deleteAll();
        List<String> urls = stringSplitter.split(imagesUrl, SEPARATOR);
        List<ImageEntity> images = getImagesFromUrls(urls);
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
        return stringJoiner.join(urls, SEPARATOR);
    }

    private List<String> getUrlsFromImages(List<ImageEntity> images) {
        return images.stream().map(ImageEntity::getImageUrl).toList();
    }
}
