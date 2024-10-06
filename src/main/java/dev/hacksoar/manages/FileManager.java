package dev.hacksoar.manages;

import dev.hacksoar.HackSoar;
import dev.hacksoar.utils.FileUtils;
import lombok.Getter;
import net.minecraft.client.Minecraft;

import java.io.File;

public class FileManager {

	private final Minecraft mc = Minecraft.getMinecraft();
	
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

	// HackSoar
	@Getter
	private final File hackSoarDir;
	@Getter
	private final File hConfigDir;
	@Getter
	private final File hConfigFile;

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
		hackSoarDir = new File(mc.mcDataDir, "HackSoar");
		hConfigDir = new File(hackSoarDir, "Configs");
		hConfigFile = new File(hConfigDir, "DefaultLocal.cfg");

		FileUtils.createDir(soarDir);
		FileUtils.createDir(tempDir);
		FileUtils.createDir(imageDir);
		FileUtils.createDir(musicDir);
		FileUtils.createDir(configDir);
		
		FileUtils.createFile(configFile);
		FileUtils.createFile(accountFile);
		FileUtils.createFile(versionFile);

		FileUtils.createDir(hackSoarDir);
		FileUtils.createDir(hConfigDir);
		FileUtils.createFile(hConfigFile);
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
