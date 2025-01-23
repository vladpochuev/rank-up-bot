package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.common.model.ImageEntity;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.repository.ImageRepository;
import com.luxusxc.rank_up.common.service.StringJoiner;
import com.luxusxc.rank_up.common.service.StringSplitter;
import com.luxusxc.rank_up.web.service.Images;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ImagesTest {
    private final ImageRepository repository;
    private final Images images;

    public ImagesTest() {
        repository = mock();
        images = new Images(repository, new StringSplitter(), new StringJoiner());
    }

    @Test
    void testExportImage() {
        List<ImageEntity> imageList = new ArrayList<>();
        imageList.add(new ImageEntity("https://imgur.com/cOQTe7i"));

        when(repository.findAll()).thenReturn(imageList);
        assertThat(images.exportImages(), equalTo("https://imgur.com/cOQTe7i"));
    }

    @Test
    void testExportImages() {
        List<ImageEntity> imageList = new ArrayList<>();
        imageList.add(new ImageEntity("https://imgur.com/cOQTe7i"));
        imageList.add(new ImageEntity("https://imgur.com/tgONOc3"));

        when(repository.findAll()).thenReturn(imageList);
        assertThat(images.exportImages(), equalTo("https://imgur.com/cOQTe7i\nhttps://imgur.com/tgONOc3"));
    }

    @Test
    void testExportImagesEmpty() {
        when(repository.findAll()).thenReturn(List.of());
        assertThat(images.exportImages(), equalTo(""));
    }

    @Test
    void testExportImagesNull() {
        List<ImageEntity> imageList = new ArrayList<>();
        imageList.add(new ImageEntity(null));
        when(repository.findAll()).thenReturn(imageList);
        assertThrows(NullPointerException.class, images::exportImages);
    }

    @Test
    void testImportImages() {
        WebConfig webConfig = new WebConfig();
        webConfig.setAttachedImagesUrl("https://imgur.com/cOQTe7i\nhttps://imgur.com/tgONOc3");
        images.importImages(webConfig);

        ArgumentCaptor<List<ImageEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        assertThat(captor.getValue().get(0), equalTo(new ImageEntity("https://imgur.com/cOQTe7i")));
        assertThat(captor.getValue().get(1), equalTo(new ImageEntity("https://imgur.com/tgONOc3")));
    }

    @Test
    void testImportImagesNull() {
        assertThrows(NullPointerException.class, () -> images.importImages(null));
    }

    @Test
    void testImportImagesNullField() {
        WebConfig webConfig = new WebConfig();
        webConfig.setAttachedImagesUrl(null);
        assertThrows(IllegalArgumentException.class, () -> images.importImages(webConfig));
    }

    @Test
    void testImportImagesEmpty() {
        WebConfig webConfig = new WebConfig();
        webConfig.setAttachedImagesUrl("");
        images.importImages(webConfig);

        ArgumentCaptor<List<ImageEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(repository).saveAll(captor.capture());

        assertThat(captor.getValue(), hasSize(0));
    }
}
