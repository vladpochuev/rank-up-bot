package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Getter
@Setter
public class RankUpConfigHandler {
    private String configPath = "src/main/resources/rankUpConfig.ser";
    private RankUpConfig config;

    public RankUpConfigHandler() {
        exportConfig();
    }

    public void importConfig() {
        try (FileOutputStream fileOut = new FileOutputStream(configPath);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void exportConfig() {
        try (FileInputStream fis = new FileInputStream(configPath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            config = (RankUpConfig) ois.readObject();
        } catch (FileNotFoundException e) {
            config = new RankUpConfig();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillFromWebConfig(WebRankUpConfig webConfig) {
        config.setEnableAll(webConfig.isEnableAll());
        config.setEnableCustomRanks(webConfig.isEnableCustomRanks());
        config.setEnableCustomLevels(webConfig.isEnableCustomLevels());
        config.setAnnounceLevelUp(webConfig.isAnnounceLevelUp());
        config.setLevelUpMessageFormat(webConfig.getLevelUpMessage());
    }
}
