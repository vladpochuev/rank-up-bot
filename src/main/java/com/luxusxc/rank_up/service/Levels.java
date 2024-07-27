package com.luxusxc.rank_up.service;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import com.luxusxc.rank_up.repository.DefaultRankRepository;
import com.luxusxc.rank_up.repository.RankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Levels {
    private final RankRepository repository;
    private final DefaultRankRepository defaultRepository;
    private final RankUpConfig config;
    private final StringSplitter stringSplitter;
    private final StringJoiner stringJoiner;
    private static final String SEPARATOR = ", ";

    public Levels(DefaultRankRepository defaultRepository, RankRepository repository, RankUpConfig config, StringSplitter stringSplitter, StringJoiner stringJoiner) {
        this.defaultRepository = defaultRepository;
        this.repository = repository;
        this.config = config;
        this.stringSplitter = stringSplitter;
        this.stringJoiner = stringJoiner;
    }

    public void importLevels(boolean enableCustom, String customLevels) {
        if (enableCustom) {
            setCustomLevels(customLevels);
        } else {
            setDefaultLevels();
        }
    }

    private void setCustomLevels(String customLevels) {
        config.setEnableCustomLevels(true);
        List<Long> levels = getLevelsFromString(customLevels);
        setLevels(levels);
    }

    private List<Long> getLevelsFromString(String levels) {
        List<String> levelsStr = stringSplitter.split(levels, SEPARATOR);
        return levelsStr.stream().mapToLong(Long::parseLong).boxed().toList();
    }

    private void setDefaultLevels() {
        config.setEnableCustomLevels(false);
        List<Long> levels = getDefaultLevels();
        setLevels(levels);
    }

    private List<Long> getDefaultLevels() {
        List<DefaultRankEntity> defaultRanks = (List<DefaultRankEntity>) defaultRepository.findAll();
        return defaultRanks.stream().map(DefaultRankEntity::getExperience).toList();
    }

    private void setLevels(List<Long> levels) {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        for (int i = 0; i < ranks.size(); i++) {
            RankEntity rank = ranks.get(i);
            rank.setExperience(levels.get(i));
            rank.setLevel(i + 1);
            repository.save(rank);
        }
    }

    public String exportLevels() {
        List<RankEntity> ranks = (List<RankEntity>) repository.findAll();
        List<String> expStrings = getExpFromRanks(ranks);
        return stringJoiner.join(expStrings, SEPARATOR);
    }

    private List<String> getExpFromRanks(List<RankEntity> ranks) {
        return ranks.stream().map(RankEntity::getExperience).map(String::valueOf).toList();
    }
}
