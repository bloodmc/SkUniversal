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
import org.bukkit.World;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.data.PlayerData;

import javax.annotation.Nullable;

@Name("GriefDefender - Remaining Claim Blocks")
@Description("Returns the remaining claim blocks of a player.")
@Examples({"send \"%the remaining claim blocks of player%\""})
public class ExprRemainingClaimBlocks extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprRemainingClaimBlocks.class, Number.class, ExpressionType.COMBINED,
                "[the] remaining [G[rief]D[efender]] [claim] blocks of %offlineplayer%",
                "%offlineplayer%'s remaining [G[rief]D[efender]] [claim] blocks");
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
        return "remaining claim blocks of player " + player.toString(e, b);
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        final OfflinePlayer offlinePlayer = player.getSingle(e);
        if (offlinePlayer == null) return null;

        final World world = offlinePlayer.isOnline() ? offlinePlayer.getPlayer().getWorld() : null;
        final PlayerData playerData = GriefDefender.getCore().getPlayerData(world == null ? null : world.getUID(), offlinePlayer.getUniqueId()).orElse(null);
        if (playerData == null) {
            return null;
        }
        return new Number[]{playerData.getRemainingClaimBlocks()};
    }

}