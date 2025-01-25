package com.luxusxc.rank_up.common.service;

import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.common.model.LogTags;
import com.luxusxc.rank_up.web.mapper.WebConfigMapper;
import com.luxusxc.rank_up.web.model.WebConfig;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Getter
@Setter
@Slf4j
public class ConfigHandler {
    private static final String IMPORT_LOG = "New config was successfully saved";
    private static final String EXPORT_EMPTY_LOG = "New empty config was created";
    private static final String EXPORT_LOG_TEMPLATE = "Config was successfully received from path %s";
    private static final Marker LOG_MARKER = MarkerFactory.getMarker(LogTags.CONFIG);

    @Value("${rank-up-config.path}")
    private String configPath;
    private Config config;

    private WebConfigMapper webConfigMapper;

    public ConfigHandler(WebConfigMapper webConfigMapper) {
        this.webConfigMapper = webConfigMapper;
    }

    @PostConstruct
    private void init() {
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
            config = (Config) ois.readObject();
            log.info(LOG_MARKER, EXPORT_LOG_TEMPLATE.formatted(configPath));
        } catch (FileNotFoundException e) {
            config = new Config();
            log.info(LOG_MARKER, EXPORT_EMPTY_LOG);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillFromWebConfig(WebConfig webConfig) {
        if (webConfig == null) {
            throw new NullPointerException();
        }
        config = webConfigMapper.toRankUpConfig(webConfig);
    }
}
