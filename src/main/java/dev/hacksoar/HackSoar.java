package dev.hacksoar;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import dev.hacksoar.api.events.EventManager;
import dev.hacksoar.api.events.EventTarget;
import dev.hacksoar.api.events.impl.*;
import dev.hacksoar.manages.BotManager;
import dev.hacksoar.manages.FileManager;
import dev.hacksoar.manages.TargetManager;
import dev.hacksoar.manages.component.ComponentManager;
import dev.hacksoar.modules.ModuleConfig;
import dev.hacksoar.modules.ModuleManager;
import dev.hacksoar.pvp.GuiEditHUD;
import dev.hacksoar.pvp.management.account.AccountManager;
import dev.hacksoar.pvp.management.colors.ColorManager;
import dev.hacksoar.pvp.management.config.ConfigManager;
import dev.hacksoar.pvp.management.cosmetics.CosmeticManager;
import dev.hacksoar.pvp.management.discord.DiscordManager;
import dev.hacksoar.pvp.management.gui.GuiManager;
import dev.hacksoar.pvp.management.image.ImageManager;
import dev.hacksoar.pvp.management.keybinds.KeyBindManager;
import dev.hacksoar.pvp.management.mods.ModManager;
import dev.hacksoar.pvp.management.mods.impl.ClientMod;
import dev.hacksoar.pvp.management.mods.impl.ForgeSpooferMod;
import dev.hacksoar.pvp.management.music.MusicManager;
import dev.hacksoar.pvp.management.quickplay.QuickPlayManager;
import dev.hacksoar.pvp.management.settings.SettingsManager;
import dev.hacksoar.utils.*;
import dev.hacksoar.utils.culling.CullTask;
import dev.hacksoar.utils.font.FontManager;
import dev.hacksoar.utils.font.FontUtils;
import dev.hacksoar.utils.server.HypixelUtils;
import dev.hacksoar.utils.server.ServerUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;

import java.util.Random;

/**
 * @author Liycxc
 */
public class HackSoar {

	public String getName() {
		return "HackSoar";
	}

	public String getVersion() {
		return "0.1.0";
	}

	public static HackSoar instance = new HackSoar();
	public static Minecraft mc = Minecraft.getMinecraft();
	public final boolean DEVELOPMENT_SWITCH = true;

	// Soar Client Managers
	public FileManager fileManager;
	public ImageManager imageManager;
	public DiscordManager discordManager;
	public KeyBindManager keyBindManager;
	public SettingsManager settingsManager;
	public EventManager eventManager;
	public ModManager modManager;
	public GuiManager guiManager;
	public ColorManager colorManager;
	public CosmeticManager cosmeticManager;
	public ConfigManager configManager;
	public MusicManager musicManager;
	public AccountManager accountManager;
	public QuickPlayManager quickPlayManager;

	// NekoCat Managers
	public ModuleManager moduleManager;
	public TargetManager targetManager;
	public BotManager botManager;
	public ComponentManager componentManager;
	public ModuleConfig moduleConfig;

	private boolean loaded;
	private long playTime;
	private final TimerUtils apiTimer = new TimerUtils();

	
	public void startClient() {
		
		OSType os = OSType.getType();

		// Managers initializing
		{
			// Soar
			fileManager = new FileManager();
			imageManager = new ImageManager();
			keyBindManager = new KeyBindManager();
			settingsManager = new SettingsManager();
			eventManager = new EventManager();
			modManager = new ModManager();
			guiManager = new GuiManager();
			colorManager = new ColorManager();
			cosmeticManager = new CosmeticManager();
			configManager = new ConfigManager();
			musicManager = new MusicManager();
			accountManager = new AccountManager();
			quickPlayManager = new QuickPlayManager();

			// NekoCat
			moduleManager = new ModuleManager();
			moduleConfig = new ModuleConfig();
			targetManager = new TargetManager();
			botManager = new BotManager();
			componentManager = new ComponentManager();
		}

		// Fonts initializing
		{
			FontUtils.init();
			FontManager.init();
			targetManager.init();
			botManager.init();
			componentManager.init();
		}

		eventManager.register(this);
		
		if(os == OSType.WINDOWS) {
			discordManager = new DiscordManager();
			discordManager.start();
			discordManager.update("Playing HackSoar Client v" + HackSoar.instance.getVersion(), "");
		}
		
		startCull();

		mc.gameSettings.loadOptions();
		DayEventUtils.resetHudDesign();
		moduleManager.registerModules();

		// First load file
		moduleConfig.load(fileManager.getHConfigFile());

		// Second register modules event
		moduleManager.EventRegister();

		// Verify.init();
	}
	
	public void stopClient() {
		
		OSType os = OSType.getType();
		
		eventManager.unregister(this);
		
		if(os == OSType.WINDOWS) {
			discordManager = new DiscordManager();
			discordManager.update("Playing HackSoar Client v" + HackSoar.instance.getVersion(), "");
		}

		moduleConfig.save(fileManager.getHConfigFile());
	}
    
	@EventTarget
	public void onTick(EventTick event) {
		
    	boolean isRandom = HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Random").getValBoolean();
    	boolean isLoop = HackSoar.instance.settingsManager.getSettingByClass(ClientMod.class, "Loop").getValBoolean();

		if(isRandom || isLoop) {
			if(!loaded) {
				if(musicManager.getCurrentMusic() != null && musicManager.getCurrentMusic().mediaPlayer != null && musicManager.getCurrentMusic().mediaPlayer.getCurrentTime().toSeconds() >= musicManager.getCurrentMusic().mediaPlayer.getTotalDuration().toSeconds()) {
					loaded = true;
					new Thread(() -> {
						loaded = false;
						musicManager.getCurrentMusic().mediaPlayer.stop();

						if(isRandom) {
							Random random = new Random();
							int randomIndex = random.nextInt(HackSoar.instance.musicManager.getMusics().size() + 1);
							musicManager.setCurrentMusic(HackSoar.instance.musicManager.getMusics().get(randomIndex == 10 ? randomIndex - 1 : randomIndex));
						}

						musicManager.getCurrentMusic().playMusic();
					}).start();
				}
			}
		}else {
			loaded = false;
			if(musicManager.getCurrentMusic() != null && musicManager.getCurrentMusic().mediaPlayer != null && musicManager.getCurrentMusic().mediaPlayer.getCurrentTime().toSeconds() == musicManager.getCurrentMusic().mediaPlayer.getTotalDuration().toSeconds()) {
				musicManager.getCurrentMusic().mediaPlayer.stop();
			}
		}

		// try turn off optifine fast render
		try {
			ClientUtils.gameSettings_ofFastRender.set(mc.gameSettings, false);
		} catch (Exception ignored) {}
		
		mc.gameSettings.useVbo = true;
		mc.gameSettings.fboEnable = true;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		TargetUtils.onUpdate();
	}
	
	private void startCull() {
		CullTask cullingTask = new CullTask(new OcclusionCullingInstance(128, new DataProvider() {

			private WorldClient world;

			@Override
			public boolean prepareChunk(int x, int z) {
				return (world = mc.theWorld) != null;
			}

			@Override
			public boolean isOpaqueFullCube(int x, int y, int z) {
				return world.isBlockNormalCube(new BlockPos(x, y, z), false);
			}

		}));

		Thread generalUpdateThread = new Thread(() -> {
			while(mc.running) {
				try {
					Thread.sleep(10);
				}
				catch(InterruptedException error) {
					return;
				}

				cullingTask.run();
			}
		}, "Async Updates");
		generalUpdateThread.setUncaughtExceptionHandler((thread, error) -> {

		});
		generalUpdateThread.start();
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
        if (event.getPacket() instanceof C17PacketCustomPayload) {
        	
            C17PacketCustomPayload packet = (C17PacketCustomPayload) event.getPacket();
            
            if(modManager.getModByClass(ForgeSpooferMod.class).isToggled()) {
                (packet).setData(new PacketBuffer(Unpooled.buffer()).writeString("FML"));
            } else {
                (packet).setData(new PacketBuffer(Unpooled.buffer()).writeString("Lunar-Client"));
            }
        }
	}
    
    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
    	if(ServerUtils.isHypixel()) {
            if (event.getPacket() instanceof S02PacketChat) {
                final S02PacketChat chatPacket = (S02PacketChat)event.getPacket();
                final String chatMessage = chatPacket.getChatComponent().getUnformattedText();
                if (chatMessage.matches("Your new API key is ........-....-....-....-............")) {
                    event.setCancelled(true);
                    HypixelUtils.setApiKey(chatMessage.replace("Your new API key is ", ""));
                }
            }
    	}
    }
    
    @EventTarget
    public void onRespawn(EventRespawn event) {
    	if(ServerUtils.isHypixel()) {
        	HypixelUtils.setApiKey(null);
    	}
    }
    
    @EventTarget
    public void onPreUpdate(EventPreMotion event) {
//        if (ServerUtils.isHypixel() && apiTimer.delay(3000) && HypixelUtils.getApiKey() == null) {
//            mc.thePlayer.sendChatMessage("/api new");
//            apiTimer.reset();
//        }
    }
    
    @EventTarget
    public void onKey(EventKey event) {
    	
    	if(mc.thePlayer != null && mc.theWorld != null) {
        	if(event.getKey() == keyBindManager.EDITHUD.getKeyCode()) {
        		mc.displayGuiScreen(new GuiEditHUD(false));
        	}
    	}
    }

	public long getPlayTime() {
		return playTime;
	}

	public void setPlayTime(long playTime) {
		this.playTime = playTime;
	}
}
