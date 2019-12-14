package warp;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

/**********************
 * Created by steve6472 (Mirek Jozefek)
 * On date: 01.08.2019
 * Project: SJP
 *
 ***********************/
public class UUIDCache
{
	HashMap<String, String> uuidNameMap;
	HashMap<String, String> nameUuidMap;

	public UUIDCache()
	{
		uuidNameMap = new HashMap<>();
		nameUuidMap = new HashMap<>();
	}

	public void update()
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			if (!uuidNameMap.containsKey(p.getUniqueId().toString()))
				uuidNameMap.put(p.getUniqueId().toString(), p.getName());
			if (!nameUuidMap.containsKey(p.getName()))
				nameUuidMap.put(p.getName(), p.getUniqueId().toString());
		}
	}

	public void load(FileConfiguration config)
	{
		ConfigurationSection cs = config.getConfigurationSection("cache");
		if (cs != null)
		{
			Set<String> uuids = cs.getKeys(false);
			for (String s : uuids)
			{
				uuidNameMap.put(s, config.getString("cache." + s));
				nameUuidMap.put(config.getString("cache." + s), s);
			}
		}
	}

	public void save(FileConfiguration config)
	{
		for (String s : uuidNameMap.keySet())
		{
			config.set("cache." + s, uuidNameMap.get(s));
		}
	}

	public HashMap<String, String> getUuidNameMap()
	{
		return uuidNameMap;
	}

	public String getUUID(String playername)
	{
		return nameUuidMap.get(playername);
	}

	public String getPlayerName(UUID uuid)
	{
		return getPlayerName(uuid.toString());
	}

	public String getPlayerName(String UUID)
	{
		return uuidNameMap.get(UUID);
	}
}
