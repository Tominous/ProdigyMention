package fr.cocoraid.prodigymention.general;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cocoraid on 19/07/2017.
 */
public class ProdigyMentionConfig {


    private transient File file;
    public ProdigyMentionConfig(String folderPath) {
        file = new File(folderPath, "config" + ".yml");
        load();
    }


    public void load() {
        if (!file.exists()) {
            try {
                file.createNewFile();
                PrintWriter writer = new PrintWriter(file, "UTF-8");

                for (Field field : getClass().getDeclaredFields()) {
                    //because of file field inside
                    if (!Modifier.isTransient(field.getModifiers())) {

                        if (field.getType().equals(String.class)) {
                            String s = (String) field.get(this);
                            writer.println(field.getName() + ": " + s.replace("§", "&"));
                        } else if (field.getType().equals(List.class)) {
                            //we do not have list yet
                        /*    List<String> list = new ArrayList<>();
                            ((List<String>) field.get(language)).forEach(s -> list.add(s.replace("§", "&")));
                            c.set(field.getName(), list);*/
                        } else {
                            writer.println(field.getName() + ": " + field.get(this));
                        }
                    }
                }
                writer.close();
                //do the tricks
            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            try {
                List<String> list = null;
                if(VersionChecker.isHigherOrEqualThan(VersionChecker.v1_14_R1))
                    org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils.readLines(file, "utf-8");
                else list = org.apache.commons.io.FileUtils.readLines(file, "utf-8");


                Map<String, Object> map = new HashMap<>();
                list.forEach(value -> {
                    String[] crypted = StringUtils.deleteWhitespace(value).split(":");
                    map.put(crypted[0], crypted[1]);
                });

                for (Field field : getClass().getDeclaredFields()) {
                    //because of file field inside
                    if (!Modifier.isTransient(field.getModifiers())) {
                        if (map.containsKey(field.getName())) {
                            if (field.getType().equals(String.class)) {
                                String s = (String) map.get(field.getName());
                                field.set(this, s.replace("&", "§"));
                            } else {
                                field.set(this, map.get(field.getName()));
                            }
                        }
                    }

                }


            } catch (IOException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean needAtToMention = false;
    public boolean isParticleEnabled = true;
    public boolean isSoundEnabled = true;
    public String customSound = "BLOCK_NOTE_BLOCK_PLING";
    public boolean antispam = true;
    public boolean antiSpamMessageEnable = false;
    public String antispamMessage = "&cWait %time seconds to mention someone";
    public int antiSpamTime = 5;
    public boolean isBypassPermission = true;
    public boolean isChatMessageEnabled = false;
    public boolean isTitleMessageEnabled = true;
    public boolean isColoredName = true;
    public String nameColor = "&b&l";
    public String toggleOnMessage = "&aPlayers can now mention you";
    public String toggleOffMessage = "&cPlayers can no longer mention you";
    public String permissionMessage = "&cYou do not have the permission";

    public String mentionMessage = "&b%player &3mentioned you !";

}
