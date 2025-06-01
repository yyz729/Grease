package com.yyz.grease.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.item.Items;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;

public class GreaseConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int fire_tick = 160;
    public int poison_tick = 400;
    public int freezing_tick = 160;
    public int lightning_cooldown = 100;
    public int grease_tick = 600;
    public int sacred_damage = 2;
    public int magic_change = 100;


    public Set<String> grease_item = Set.of(
            "minecraft:stick"
    );

    public static GreaseConfig loadConfig(File file) {
        GreaseConfig config;

        if (file.exists() && file.isFile()) {
            try (
                    FileInputStream fileInputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ) {
                config = GSON.fromJson(bufferedReader, GreaseConfig.class);
            } catch (IOException e) {
                throw new RuntimeException("Failed to load config", e);
            }
        } else {
            config = new GreaseConfig();
        }

        config.saveConfig(file);

        return config;
    }

    public void saveConfig(File config) {
        try (
                FileOutputStream stream = new FileOutputStream(config);
                Writer writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
        ) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config", e);
        }
    }



}
