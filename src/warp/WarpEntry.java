package warp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WarpEntry
{
	public World world;
	public double x, y, z;
	public float yaw, pitch;
	public String creator;
	public String name;
	public boolean isGlobal;

	public WarpEntry(FileConfiguration config, String name)
	{
		this.name = name;

		String worldName = config.getString("warps." + name + ".world");
		if (worldName == null) throw new NullPointerException("World is null");

		this.world = Bukkit.getWorld(worldName);

		this.x = config.getDouble("warps." + name + ".x");
		this.y = config.getDouble("warps." + name + ".y");
		this.z = config.getDouble("warps." + name + ".z");

		this.yaw = (float) config.getDouble("warps." + name + ".yaw");
		this.pitch = (float) config.getDouble("warps." + name + ".pitch");

		this.isGlobal = config.getBoolean("warps." + name + ".isGlobal");

		this.creator = config.getString("warps." + name + ".creator");
	}

	public WarpEntry(Player player, String name)
	{
		this.name = name;

		this.world = player.getLocation().getWorld();

		this.x = player.getLocation().getX();
		this.y = player.getLocation().getY();
		this.z = player.getLocation().getZ();

		this.yaw = player.getLocation().getYaw();
		this.pitch = player.getLocation().getPitch();

		this.isGlobal = false;

		this.creator = player.getUniqueId().toString();
	}

	public void save(FileConfiguration config)
	{
		setWarpProperty(config, "world", world.getName());

		setWarpProperty(config, "x", x);
		setWarpProperty(config, "y", y);
		setWarpProperty(config, "z", z);

		setWarpProperty(config, "yaw", yaw);
		setWarpProperty(config, "pitch", pitch);

		setWarpProperty(config, "creator", creator);
		setWarpProperty(config, "isGlobal", isGlobal);
	}

	private void setWarpProperty(FileConfiguration config, String warpProperty, Object value)
	{
		config.set("warps." + name + "." + warpProperty, value);
	}

	@Override
	public String toString()
	{
		return "WarpEntry{" + "world=" + world + ", x=" + x + ", y=" + y + ", z=" + z + ", yaw=" + yaw + ", pitch=" + pitch + ", creator='" + creator + '\'' + ", name='" + name + '\'' + ", isGlobal=" + isGlobal + '}';
	}

}