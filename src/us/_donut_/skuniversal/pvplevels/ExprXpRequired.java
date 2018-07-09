package us._donut_.skuniversal.pvplevels;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.MathiasMC.PvPLevels.PvPLevelsAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("PvPLevels - Required XP")
@Description("Returns the XP needed for a player to level up.")
@Examples({"send \"%pvp xp required for player to level up%\""})
public class ExprXpRequired extends SimpleExpression<Number> {

    private Expression<Player> player;

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
    public boolean init(Expression<?>[] e, int i, Kleenean kl, SkriptParser.ParseResult pr) {
        player = (Expression<Player>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "xp required to for player " + player.toString(e, b) + " to level up";
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        return new Number[]{new PvPLevelsAPI().CurrentXPRequired(player.getSingle(e))};
    }
}
