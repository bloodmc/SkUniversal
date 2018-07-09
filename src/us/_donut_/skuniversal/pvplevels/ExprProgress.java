package us._donut_.skuniversal.pvplevels;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.MathiasMC.PvPLevels.PvPLevelsAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("PvPLevels - XP Progress of Player")
@Description("Returns the XP progress of a player.")
@Examples({"send \"%pvp xp progress of player%\""})
public class ExprProgress extends SimpleExpression<String> {

    private Expression<OfflinePlayer> player;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean kl, SkriptParser.ParseResult pr) {
        player = (Expression<OfflinePlayer>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "progress of player " + player.toString(e, b);
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        return new String[]{new PvPLevelsAPI().CurrentXPProgressOfflinePlayer(player.getSingle(e))};
    }
}
