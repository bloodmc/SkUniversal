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
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustType;
import com.griefdefender.api.claim.TrustTypes;

import javax.annotation.Nullable;

import java.util.UUID;

@Name("GriefDefender - Trusted Players")
@Description("Returns the trusted players of a claim.")
@Examples({"send \"%the trusted players of the claim with id (id of the basic claim at player)%\""})
public class ExprTrustedPlayers extends SimpleExpression<OfflinePlayer> {

    static {
        Skript.registerExpression(ExprTrustedPlayers.class, OfflinePlayer.class, ExpressionType.COMBINED, "(0¦builder|1¦container|2¦access|3¦manager) trusted [players] (of|on) [G[rief]D[efender]] claim [with ID] %number%");
    }

    private Expression<UUID> id;
    private TrustType trustType;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends OfflinePlayer> getReturnType() {
        return OfflinePlayer.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        id = (Expression<UUID>) e[0];
        switch (pr.mark) {
            case 0: trustType = TrustTypes.BUILDER;
                    break;
            case 1: trustType = TrustTypes.CONTAINER;
                    break;
            case 2: trustType = TrustTypes.ACCESSOR;
                    break;
            case 3: trustType = TrustTypes.MANAGER;
                    break;
        }
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "trusted players of claim with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected OfflinePlayer[] get(Event e) {
        Claim claim = GriefDefender.getCore().getClaim(id.getSingle(e));
        if (claim == null) return null;

        return claim.getUserTrusts(trustType).stream().map(Bukkit::getOfflinePlayer).toArray(OfflinePlayer[]::new);
    }

}