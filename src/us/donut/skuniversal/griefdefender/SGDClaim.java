package us.donut.skuniversal.griefdefender;

import java.io.NotSerializableException;
import java.io.StreamCorruptedException;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import ch.njol.skript.hooks.regions.RegionsPlugin;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.util.AABB;
import ch.njol.util.coll.iterator.EmptyIterator;
import ch.njol.yggdrasil.Fields;

public class SGDClaim extends Region {

    private transient Claim claim;

    public SGDClaim(Claim claim) {
        this.claim = claim;
    }

    @Override
    public void deserialize(@NonNull Fields f) throws StreamCorruptedException, NotSerializableException {
        final UUID uuid = f.getAndRemoveObject("uuid", UUID.class);
        final Claim c = GriefDefender.getCore().getClaim(uuid);
        if (c == null)
            throw new StreamCorruptedException("Invalid claim " + uuid);
        this.claim = c;
    }

    @Override
    public Fields serialize() throws NotSerializableException {
        final Fields f = new Fields();
        f.putObject("uuid", this.claim.getUniqueId());
        return f;
    }

    @Override
    public boolean contains(Location loc) {
        return this.claim.contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @Override
    public boolean equals(@Nullable Object otherClaim) {
        return this.claim.equals(otherClaim);
    }

    @Override
    public Iterator<Block> getBlocks() {
        final Location lower = GDUtil.toLocation(this.claim.getWorldUniqueId(), this.claim.getLesserBoundaryCorner());
        final Location upper = GDUtil.toLocation(this.claim.getWorldUniqueId(), this.claim.getGreaterBoundaryCorner());
        if (lower == null || upper == null || lower.getWorld() == null || upper.getWorld() == null || lower.getWorld() != upper.getWorld())
            return EmptyIterator.get();
        upper.setY(upper.getWorld().getMaxHeight());
        upper.setX(upper.getBlockX() + 1);
        upper.setZ(upper.getBlockZ() + 1);
        return new AABB(lower, upper).iterator();
    }

    @Override
    public Collection<OfflinePlayer> getMembers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<OfflinePlayer> getOwners() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RegionsPlugin<?> getPlugin() {
        return GriefDefenderHook.getInstance();
    }

    @Override
    public int hashCode() {
        return this.claim.hashCode();
    }

    @Override
    public boolean isMember(OfflinePlayer player) {
        return this.claim.isTrusted(player.getUniqueId());
    }

    @Override
    public boolean isOwner(OfflinePlayer player) {
        return player.getUniqueId().equals(this.claim.getOwnerUniqueId());
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return null;
    }
}
