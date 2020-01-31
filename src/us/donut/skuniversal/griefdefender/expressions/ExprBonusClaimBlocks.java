package us.donut.skuniversal.griefdefender.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.User;

import javax.annotation.Nullable;

@Name("GriefDefender - Bonus Claim Blocks")
@Description("Returns the bonus claim blocks of a player.")
@Examples({"send \"%the bonus claim blocks of player%\""})
public class ExprBonusClaimBlocks extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprBonusClaimBlocks.class, Number.class, ExpressionType.COMBINED,
                "[the] bonus [G[rief]D[efender]] [claim] blocks of %offlineplayer%",
                "%offlineplayer%'s bonus [G[rief]D[efender]] [claim] blocks");
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
        return "bonus claim blocks of player " + player.toString(e, b);
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
        return new Number[]{user.getPlayerData().getBonusClaimBlocks()};
    }

    @Override
    public void change(Event e, Object[] delta, Changer.ChangeMode mode){
        int blockChange = ((Number) delta[0]).intValue();
        final OfflinePlayer offlinePlayer = player.getSingle(e);
        if (offlinePlayer == null) {
            return;
        }

        final User user = GriefDefender.getCore().getUser(offlinePlayer.getUniqueId());
        if (user == null) {
            return;
        }
        if (mode == Changer.ChangeMode.SET) {
            user.getPlayerData().setBonusClaimBlocks(blockChange);
        } else if (mode == Changer.ChangeMode.ADD) {
            user.getPlayerData().setBonusClaimBlocks(user.getPlayerData().getBonusClaimBlocks() + blockChange);
        } else if (mode == Changer.ChangeMode.REMOVE) {
           user.getPlayerData().setBonusClaimBlocks(user.getPlayerData().getBonusClaimBlocks() - blockChange);
        }
    }
    @Override
    public Class<?>[] acceptChange(final Changer.ChangeMode mode) {
        return (mode == Changer.ChangeMode.SET || mode == Changer.ChangeMode.REMOVE || mode == Changer.ChangeMode.ADD) ? CollectionUtils.array(Number.class) : null;
    }

}