package dev.hacksoar.manages;

import dev.hacksoar.HackSoar;
import dev.hacksoar.utils.FileUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

	private Minecraft mc = Minecraft.getMinecraft();
	
	// Directory
	private final File soarDir;
	private final File tempDir;
	private final File imageDir;
	private final File musicDir;
	private final File configDir;
	
	// File
	private final File configFile;
	private final File versionFile;
	private final File accountFile;

	// NekoCat
	@Getter
	private final File nekocatDir;
	@Getter
	private final File nConfigDir;
	@Getter
	private final File nConfigFile;

	public FileManager() {
		
		soarDir = new File(mc.mcDataDir, "soar");
		tempDir = new File(soarDir, "temp");
		imageDir = new File(soarDir, "image");
		musicDir = new File(soarDir, "music");
		configDir = new File(soarDir, "config");
		
		configFile = new File(soarDir, "Config.txt");
		accountFile = new File(soarDir, "Accounts.txt");
		versionFile = new File(tempDir, HackSoar.instance.getVersion() + ".ver");

		// NekoCat
		nekocatDir = new File(mc.mcDataDir,"HackSoar");
		nConfigDir = new File(nekocatDir,"Configs");
		nConfigFile = new File(nConfigDir,"DefaultLocal.nekocat");

		FileUtils.createDir(soarDir);
		FileUtils.createDir(tempDir);
		FileUtils.createDir(imageDir);
		FileUtils.createDir(musicDir);
		FileUtils.createDir(configDir);
		
		FileUtils.createFile(configFile);
		FileUtils.createFile(accountFile);
		FileUtils.createFile(versionFile);

		FileUtils.createDir(nekocatDir);
		FileUtils.createDir(nConfigDir);
		FileUtils.createFile(nConfigFile);
	}

	public File getSoarDir() {
		return soarDir;
	}

	public File getConfigFile() {
		return configFile;
	}

	public File getTempDir() {
		return tempDir;
	}

	public File getVersionFile() {
		return versionFile;
	}

	public File getImageDir() {
		return imageDir;
	}

	public File getMusicDir() {
		return musicDir;
	}

	public File getAccountFile() {
		return accountFile;
	}

	public File getConfigDir() {
		return configDir;
	}
}
