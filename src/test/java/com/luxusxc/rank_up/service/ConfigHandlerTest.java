package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.web.mapper.WebConfigMapper;
import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.web.model.WebConfig;
import com.luxusxc.rank_up.common.service.ConfigHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigHandlerTest {
    private ConfigHandler configHandler;

    @BeforeEach
    void getConfig() {
        configHandler = new ConfigHandler(WebConfigMapper.INSTANCE);
        configHandler.setConfigPath("src/main/resources/rankUpConfig.ser");
        configHandler.exportConfig();
    }

    @Test
    void testImportConfig() {
        String tmpConfigPath = "src/test/java/com/luxusxc/rank_up/rankUpConfig.ser";
        configHandler.setConfigPath(tmpConfigPath);

        Config config = new Config();
        configHandler.setConfig(config);
        configHandler.importConfig();

        File file = new File(tmpConfigPath);
        assertTrue(file.exists());

        Config desConfig = deserializeConfig(file);
        assertThat(config, equalTo(desConfig));

        assertTrue(file.delete());
    }

    @Test
    void testExportConfig() {
        Config config = configHandler.getConfig();
        File file = new File("src/main/resources/rankUpConfig.ser");
        Config desConfig = deserializeConfig(file);
        assertThat(config, equalTo(desConfig));
    }

    @Test
    void testExportFileDoNotExists() {
        String randomPath = "src/VU1JtD/rankUpConfig.ser";
        File randomFile = new File(randomPath);
        assertFalse(randomFile.exists());

        configHandler.setConfigPath(randomPath);
        configHandler.exportConfig();
        assertThat(configHandler.getConfig(), equalTo(new Config()));
    }

    @Test
    void testImportInvalidPath() {
        String invalidPath = "";
        File invalidFile = new File(invalidPath);
        assertFalse(invalidFile.exists());
        configHandler.setConfigPath(invalidPath);
        assertThrows(RuntimeException.class, () -> configHandler.importConfig());
    }

    private Config deserializeConfig(File file) {
        try (InputStream stream = new FileInputStream(file);
             ObjectInputStream objectInputStream = new ObjectInputStream(stream)) {
            return (Config) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFillFromWebConfig() {
        WebConfig webConfig = new WebConfig();
        webConfig.setEnableAll(true);
        webConfig.setEnableCustomRanks(false);
        webConfig.setEnableCustomLevels(true);
        webConfig.setAnnounceLevelUp(false);
        webConfig.setLevelUpMessage("name: {name}");
        configHandler.fillFromWebConfig(webConfig);

        Config config = configHandler.getConfig();
        assertThat(config.isEnableAll(), equalTo(webConfig.isEnableAll()));
        assertThat(config.isEnableCustomRanks(), equalTo(webConfig.isEnableCustomRanks()));
        assertThat(config.isEnableCustomLevels(), equalTo(webConfig.isEnableCustomLevels()));
        assertThat(config.isAnnounceLevelUp(), equalTo(webConfig.isAnnounceLevelUp()));
        assertThat(config.getLevelUpMessageFormat(), equalTo(webConfig.getLevelUpMessage()));
    }

    @Test
    void testFillFromWebConfigNull() {
        assertThrows(NullPointerException.class, () -> configHandler.fillFromWebConfig(null));
    }

    @Test
    void testFillFromWebConfigNullFields() {
        WebConfig webConfig = new WebConfig();
        webConfig.setLevelUpMessage(null);
        configHandler.fillFromWebConfig(webConfig);

        Config config = configHandler.getConfig();
        assertThat(config.getLevelUpMessageFormat(), nullValue());
    }
}
