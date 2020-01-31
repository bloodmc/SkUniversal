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
import com.griefdefender.api.User;

import javax.annotation.Nullable;

@Name("GriefDefender - Claim Block Limit")
@Description("Returns the claim block limit of a player.")
@Examples({"send \"%the claim block limit of player%\""})
public class ExprClaimBlockLimit extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprClaimBlockLimit.class, Number.class, ExpressionType.COMBINED,
                "[the] [G[rief]D[efender]] [claim] block limit of %offlineplayer%",
                "%offlineplayer%'s [G[rief]D[efender]] [claim] block limit");
    }

    private Expression<OfflinePlayer> player;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Number> getReturnType() {
        return Number.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        player = (Expression<OfflinePlayer>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "claim block limit of player " + player.toString(e, b);
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        final OfflinePlayer offlinePlayer = player.getSingle(e);
        if (offlinePlayer == null) {
            return null;
        }

        final User user = GriefDefender.getCore().getUser(offlinePlayer.getUniqueId());
        if (user == null) {
            return null;
        }
        return new Number[]{user.getPlayerData().getMaxAccruedClaimBlocks()};
    }

}