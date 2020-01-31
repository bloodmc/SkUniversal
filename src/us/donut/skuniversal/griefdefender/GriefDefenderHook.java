package us.donut.skuniversal.griefdefender;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.eclipse.jdt.annotation.Nullable;

import com.griefdefender.GDBootstrap;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.Tristate;
import com.griefdefender.api.User;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.ClaimManager;
import com.griefdefender.api.claim.TrustTypes;
import com.griefdefender.api.permission.flag.Flags;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.classes.YggdrasilSerializer;
import ch.njol.skript.hooks.regions.RegionsPlugin;
import ch.njol.skript.hooks.regions.classes.Region;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.lang.VariableString;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.skript.util.Getter;
import ch.njol.yggdrasil.Fields;
import us.donut.skuniversal.SkUniversalEvent;
import us.donut.skuniversal.griefdefender.events.SGDRemoveClaimEvent;

public class GriefDefenderHook extends RegionsPlugin<GDBootstrap> {

    public static GriefDefenderHook instance;

    public static GriefDefenderHook getInstance() {
        return instance;
    }

    static {
        Skript.registerEvent("GriefDefender - Claim Abandon", SkUniversalEvent.class, SGDRemoveClaimEvent.Delete.class, "[G[rief]D[efender]] claim aban(d|on)")
        .description("Called when a claim is abandoned.")
        .examples("on claim abandon:", "\tbroadcast \"Claim %claim% was abandoned!\"");
        EventValues.registerEventValue(SGDRemoveClaimEvent.Delete.class, Claim.class, new Getter<Claim, SGDRemoveClaimEvent.Delete>() {
            public Claim get(SGDRemoveClaimEvent.Delete e) {
                return e.getClaim();
            }
        }, 0);

        Skript.registerEvent("GriefDefender - Claim Deletion", SkUniversalEvent.class, SGDRemoveClaimEvent.Delete.class, "[G[rief]D[efender]] claim delet(e|ion)")
                .description("Called when a claim is deleted.")
                .examples("on claim deletion:", "\tbroadcast \"Claim %claim% was deleted!\"");
        EventValues.registerEventValue(SGDRemoveClaimEvent.Delete.class, Claim.class, new Getter<Claim, SGDRemoveClaimEvent.Delete>() {
            public Claim get(SGDRemoveClaimEvent.Delete e) {
                return e.getClaim();
            }
        }, 0);

        Skript.registerEvent("GriefDefender - Claim Expiration", SkUniversalEvent.class, SGDRemoveClaimEvent.Expire.class, "[G[rief]D[efender]] claim expir(e|ation)")
                .description("Called when a claim expires.")
                .examples("on claim expiration:", "\tbroadcast \"Claim %claim% has expired!\"");
        EventValues.registerEventValue(SGDRemoveClaimEvent.Expire.class, Claim.class, new Getter<Claim, SGDRemoveClaimEvent.Expire>() {
            public Claim get(SGDRemoveClaimEvent.Expire e) {
                return e.getClaim();
            }
        }, 0);
    }

    public GriefDefenderHook() throws IOException {
        super();

        instance = this;
        Classes.registerClass(new ClassInfo<>(UUID.class, "uuid")
                .name(ClassInfo.NO_DOC)
                .parser(new Parser<UUID>() {
                    @Override
                    @Nullable
                    public UUID parse(final String s, final ParseContext context) {
                        try {
                            return UUID.fromString(s);
                        } catch (final NumberFormatException e) {
                            return null;
                        }
                    }
                    
                    @Override
                    public String toString(final UUID uuid, final int flags) {
                        return uuid.toString();
                    }
                    
                    @Override
                    public String toVariableNameString(final UUID uuid) {
                        return uuid.toString();
                    }
                    
                    @Override
                    public String getVariableNamePattern() {
                        return "\\b[0-9a-f]{8}\\b-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-\\b[0-9a-f]{12}\\b";
                    }
                }).serializer(new Serializer<UUID>() {
                    @Override
                    public Fields serialize(final UUID n) {
                        throw new IllegalStateException(); // serialised natively by Yggdrasil
                    }
                    
                    @Override
                    public boolean canBeInstantiated() {
                        return true;
                    }
                    
                    @Override
                    public void deserialize(final UUID o, final Fields f) throws StreamCorruptedException {
                        assert false;
                    }

                    @Override
                    @Nullable
                    public UUID deserialize(final String s) {
                        try {
                            return UUID.fromString(s);
                        } catch (final NumberFormatException e) {
                            return null;
                        }
                    }
                    
                    @Override
                    public boolean mustSyncDeserialization() {
                        return false;
                    }
                }));

        Classes.registerClass(new ClassInfo<>(SGDClaim.class, "claim")
                .name("Claim")
                .description("A GriefDefender claim.")
                .usage("\"claim uuid\"")
                .examples("")
                .after("string", "world", "offlineplayer", "player")
                .user("claims?")
                .parser(new Parser<SGDClaim>() {
                    @Override
                    @Nullable
                    public SGDClaim parse(String s, final ParseContext context) {
                        final boolean quoted;
                        switch (context) {
                            case DEFAULT:
                            case EVENT:
                            case SCRIPT:
                                quoted = true;
                                break;
                            case COMMAND:
                            case CONFIG:
                                quoted = false;
                                break;
                            default:
                                assert false;
                                return null;
                        }
                        if (!VariableString.isQuotedCorrectly(s, quoted))
                            return null;
                        s = VariableString.unquote(s, quoted);
                        UUID uuid = null;
                        try {
                            uuid = UUID.fromString(s);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                        Claim r = null;
                        for (final World w : Bukkit.getWorlds()) {
                            final Claim r2 = GriefDefender.getCore().getClaimManager(w.getUID()).getClaimByUUID(uuid).orElse(null);
                            if (r2 == null)
                                continue;
                            r = r2;
                        }
                        return new SGDClaim(r);
                    }
                    
                    @Override
                    public String toString(final SGDClaim r, final int flags) {
                        return r.toString();
                    }
                    
                    @Override
                    public String toVariableNameString(final SGDClaim r) {
                        return r.toString();
                    }
                    
                    @Override
                    public String getVariableNamePattern() {
                        return ".*";
                    }
                })
                .serializer(new YggdrasilSerializer<SGDClaim>() {
                    @Override
                    public boolean mustSyncDeserialization() {
                        return true;
                    }
                }));

        GriefDefender.getEventManager().register(new GDEventListener());
    }

    @Override
    public boolean canBuild_i(Player player, Location loc) {
        final User user = GriefDefender.getCore().getUser(player.getUniqueId());
        if (user == null) {
            return true;
        }
        final ClaimManager claimManager = GriefDefender.getCore().getClaimManager(loc.getWorld().getUID());
        if (claimManager == null) {
            return true;
        }

        final Claim claim = claimManager.getClaimAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        if (claim == null) {
            return true;
        }

        final Tristate placeResult = claim.getActiveFlagPermissionValue(Flags.BLOCK_PLACE, user, new HashSet<>(), TrustTypes.BUILDER);
        final Tristate breakResult = claim.getActiveFlagPermissionValue(Flags.BLOCK_BREAK, user, new HashSet<>(), TrustTypes.BUILDER);
        if (placeResult == Tristate.UNDEFINED && breakResult == Tristate.UNDEFINED) {
            return true;
        }
        return placeResult == Tristate.TRUE && breakResult == Tristate.TRUE;
    }

    @Override
    protected Class<? extends Region> getRegionClass() {
        return SGDClaim.class;
    }

    @Override
    @Nullable
    public Region getRegion_i(World world, String name) {
        final ClaimManager claimManager = GriefDefender.getCore().getClaimManager(world.getUID());
        if (claimManager == null) {
            return null;
        }

        final List<Claim> claims = claimManager.getClaimsByName(name);
        if (!claims.isEmpty()) {
            final Claim claim = claims.get(0);
            return new SGDClaim(claim);
        }
        return null;
    }

    @Override
    public Collection<? extends Region> getRegionsAt_i(Location location) {
        final ClaimManager claimManager = GriefDefender.getCore().getClaimManager(location.getWorld().getUID());
        final List<SGDClaim> claims = new ArrayList<>();
        if (claimManager == null) {
            return claims;
        }

        final Claim claim = claimManager.getClaimAt(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        if (claim != null) {
            claims.add(new SGDClaim(claim));
        }
        return claims;
    }

    @Override
    public boolean hasMultipleOwners_i() {
        return false;
    }

    @Override
    public String getName() {
        return "GriefDefender";
    }
}
