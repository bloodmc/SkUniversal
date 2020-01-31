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
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import java.util.UUID;

import javax.annotation.Nullable;

@Name("GriefDefender - Claim IDs of Player")
@Description("Returns the claim IDs of the claims of a player.")
@Examples({"send \"%the ids of the basic claims of player%\""})
public class ExprClaimsOfPlayer extends SimpleExpression<UUID> {

    static {
        Skript.registerExpression(ExprClaimsOfPlayer.class, UUID.class, ExpressionType.COMBINED, "[(all [[of] the]|the)]] [IDs of [all] [the]] [G[rief]D[efender]] (0¦(basic|normal) |1¦admin |2¦sub[(-| )]|3¦)claim[s] of %offlineplayer%");
    }

    private Expression<OfflinePlayer> player;
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
            case 0: claimType = "normal"; break;
            case 1: claimType = "admin"; break;
            case 2: claimType = "sub-claim"; break;
            default: claimType = "all"; break;
        }
        player = (Expression<OfflinePlayer>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "claim IDs of player " + player.toString(e, b);
    }

    @Override
    @Nullable
    protected UUID[] get(Event e) {
        final OfflinePlayer offlinePlayer = player.getSingle(e);
        if (offlinePlayer == null) return null;
        return GriefDefender.getCore().getAllPlayerClaims(offlinePlayer.getUniqueId())
                .stream()
                .filter(claim -> {
                    switch (claimType) {
                        case "normal":
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
