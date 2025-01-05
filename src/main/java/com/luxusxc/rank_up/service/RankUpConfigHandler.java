package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.mapper.WebRankUpConfigMapper;
import com.luxusxc.rank_up.model.LogTags;
import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Getter
@Setter
@Slf4j
public class RankUpConfigHandler {
    private static final String IMPORT_LOG = "New config was successfully saved";
    private static final String EXPORT_EMPTY_LOG = "New empty config was created";
    private static final String EXPORT_LOG_TEMPLATE = "Config was successfully received from path %s";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    private WebRankUpConfigMapper webRankUpConfigMapper;
    private String configPath = "src/main/resources/rankUpConfig.ser";
    private RankUpConfig config;

    public RankUpConfigHandler(WebRankUpConfigMapper webRankUpConfigMapper) {
        this.webRankUpConfigMapper = webRankUpConfigMapper;
        exportConfig();
    }

    public void importConfig() {
        try (FileOutputStream fileOut = new FileOutputStream(configPath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(config);
            log.info(LOG_MARKER, IMPORT_LOG);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportConfig() {
        try (FileInputStream fis = new FileInputStream(configPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            config = (RankUpConfig) ois.readObject();
            log.info(LOG_MARKER, EXPORT_LOG_TEMPLATE.formatted(configPath));
        } catch (FileNotFoundException e) {
            config = new RankUpConfig();
            log.info(LOG_MARKER, EXPORT_EMPTY_LOG);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillFromWebConfig(WebRankUpConfig webConfig) {
        if (webConfig == null) {
            throw new NullPointerException();
        }
        config = webRankUpConfigMapper.toRankUpConfig(webConfig);
    }
}
