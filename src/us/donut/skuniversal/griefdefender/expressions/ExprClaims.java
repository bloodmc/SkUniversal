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
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import java.util.UUID;

import javax.annotation.Nullable;

@Name("GriefDefender - All Claim UUIDs")
@Description("Returns the all claim UUIDs")
@Examples({"send \"%the ids of all basic claims\""})
public class ExprClaims extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprClaims.class, UUID.class, ExpressionType.COMBINED, "[(all [[of] the]|the)]] [IDs of [all] [the]] [G[rief]D[efender]] (0¦(basic|normal) |1¦admin |2¦sub[(-| )]|3¦)claim[s]");
    }

    private String claimType;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends UUID> getReturnType() {
        return UUID.class;
    }

    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        switch (pr.mark) {
            case 0: claimType = "basic"; break;
            case 1: claimType = "admin"; break;
            case 2: claimType = "subdivision"; break;
            case 3: claimType = "town"; break;
            default: claimType = "all"; break;
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "all claim IDs";
    }

    @Override
    @Nullable
    protected UUID[] get(Event e) {
        return GriefDefender.getCore().getAllClaims()
                .stream()
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
