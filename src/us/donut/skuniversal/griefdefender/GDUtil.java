package us.donut.skuniversal.griefdefender;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.flowpowered.math.vector.Vector3i;

public class GDUtil {

    public static Location toLocation(UUID uuid, Vector3i pos) {
        final World world = Bukkit.getWorld(uuid);
        if (world == null) {
            return null;
        }
        return new Location(world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static Location toLocation(World world, Vector3i pos) {
        return new Location(world, pos.getX(), pos.getY(), pos.getZ());
    }
}
