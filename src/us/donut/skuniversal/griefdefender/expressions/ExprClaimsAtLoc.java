package us.donut.skuniversal.griefdefender.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;

import com.flowpowered.math.vector.Vector3i;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import java.util.UUID;

import javax.annotation.Nullable;

@Name("GriefDefender - Claim IDs at Location")
@Description("Returns the claim IDs of claims at a location.")
@Examples({"send \"%the ids of the basic claims at player%\""})
public class ExprClaimsAtLoc extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprClaimsAtLoc.class, UUID.class, ExpressionType.COMBINED, "[(all [[of] the]|the)]] [IDs of [all] [the]] [G[rief]D[efender]] (0¦(basic|normal) |1¦admin |2¦sub[division]|3¦town|4¦)claim[s] at %location%");
    }

    private Expression<Location> location;
    private String claimType;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends UUID> getReturnType() {
        return UUID.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        switch (pr.mark) {
            case 0: claimType = "basic"; break;
            case 1: claimType = "admin"; break;
            case 2: claimType = "subdivision"; break;
            case 3: claimType = "town"; break;
            default: claimType = "all"; break;
        }
        location = (Expression<Location>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "claim IDs at location " + location.toString(e, b);
    }

    @Override
    @Nullable
    protected UUID[] get(Event e) {
        if (location.getSingle(e) == null) return null;
        final Location loc = location.getSingle(e);
        return GriefDefender.getCore().getAllClaims()
                .stream()
                .filter(claim -> claim.contains(new Vector3i(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())))
                .filter(claim -> {
                    switch (claimType) {
                        case "basic":
                            return claim.isBasicClaim();
                        case "admin":
                            return claim.isAdminClaim();
                        case "subdivision":
                            return claim.isSubdivision();
                        case "town":
                            return claim.isTown();
                        default:
                            return true;
                    }
                })
                .map(Claim::getUniqueId).toArray(UUID[]::new);
    }
}
