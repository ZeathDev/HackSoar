package dev.hacksoar.modules;

import dev.hacksoar.HackSoar;
import dev.hacksoar.api.value.Value;
import dev.hacksoar.api.value.impl.BoolValue;
import dev.hacksoar.api.value.impl.FloatValue;
import dev.hacksoar.api.value.impl.IntValue;
import dev.hacksoar.api.value.impl.ListValue;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ModuleConfig {
    private ArrayList<File> configs = new ArrayList<File>();

    private int prevConfigs;
    private double scrollY;

    public ModuleConfig() {
        this.loadConfigs();
        this.load();
    }

    public void loadConfigs() {
        if(prevConfigs != Objects.requireNonNull(HackSoar.instance.fileManager.getNConfigDir().listFiles()).length) {

            prevConfigs = Objects.requireNonNull(HackSoar.instance.fileManager.getNConfigDir().listFiles()).length;

            configs.clear();

            scrollY = 0;

            FilenameFilter filter = (file, str) -> str.endsWith("nekocat");

            File[] fileArray = HackSoar.instance.fileManager.getNConfigDir().listFiles(filter);

            if (fileArray != null) {
                Collections.addAll(configs, fileArray);
            }
        }
    }

    public void save(File file) {
        ArrayList<String> toSave = new ArrayList<String>();

        for (Module module : HackSoar.instance.moduleManager.getModules()) {
            toSave.add("ModuleName:" + module.moduleName + ":" + module.getToggled() + ":" + module.keybind);
            // toSave.add("ModulePos:");
        }

        for (Module module : HackSoar.instance.moduleManager.getModules()) {
            if (module.getValues().size() > 0) {
                for (Value<?> value : module.getValues()) {
                    toSave.add("SET:" + module.moduleName + ":" + value.getName() + ":" + value.get());
                }
            }
        }

        try {
            PrintWriter pw = new PrintWriter(file);
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            // Don't give crackers clues...
            if (HackSoar.instance.DEVELOPMENT_SWITCH)
                e.printStackTrace();
        }
    }

    public void load(File file) {

        ArrayList<String> lines = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            // Don't give crackers clues...
            if (HackSoar.instance.DEVELOPMENT_SWITCH)
                e.printStackTrace();
        }

        try {
            for (String s : lines) {

                String[] args = s.split(":");

                if (s.toLowerCase().startsWith("modulename:")) {
                    Module m = HackSoar.instance.moduleManager.getModule(args[1]);
                    if (m != null) {
                        m.setToggled(Boolean.parseBoolean(args[2]));
                        // Load config is later than init modules
                        HackSoar.instance.moduleManager.setKeybind(m,Integer.parseInt(args[3]));
                    }
                } else if (s.toLowerCase().startsWith("set:")) {
                    Module m = HackSoar.instance.moduleManager.getModule(args[1]);
                    if (m != null) {
                        Value<?> set = m.getValueByName(args[2]);
                        if (set != null) {
                            if (set instanceof BoolValue) {
                                ((BoolValue) set).set(Boolean.parseBoolean(args[3]));
                            }
                            if (set instanceof ListValue) {
                                ((ListValue) set).set(args[3]);
                            }
                            if (set instanceof FloatValue) {
                                ((FloatValue) set).set(Float.parseFloat(args[3]));
                            }
                            if (set instanceof IntValue) {
                                ((IntValue) set).set(Integer.parseInt(args[3]));
                            }
                        }
                    }
                    // PlayerUtils.tellPlayer("args[1]: " + args[1] + " args[2]: " + args[2] + " args[3]: " + args[3]);
                }
            }
        }catch (Exception exception) {
            if (HackSoar.instance.DEVELOPMENT_SWITCH) {
                exception.printStackTrace();
            }
        }
    }

    public void save() {
        this.save(HackSoar.instance.fileManager.getNConfigFile());
    }

    public void load() {
        this.load(HackSoar.instance.fileManager.getNConfigFile());
    }

    public ArrayList<File> getConfigs() {
        return configs;
    }

    public double getScrollY() {
        return scrollY;
    }

    public void setScrollY(double scrollY) {
        this.scrollY = scrollY;
    }
}
