package us._donut_.skuniversal.bedwars;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import me.MineHome.Bedwars.Game.GameAPI;
import me.MineHome.Bedwars.Game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("Bedwars - Game of Player")
@Description("Returns the current game of a player.")
@Examples({"send \"Your current game is %player's bedwars game%.\""})
public class ExprGameOfPlayer extends SimpleExpression<String> {

    private Expression<Player> player;

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
        player = (Expression<Player>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "Bedwars game of player " + player.toString(e, b);
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        GameAPI game = GameManager.getGame(player.getSingle(e));
        return game == null ? null : new String[]{GameManager.getGame(player.getSingle(e)).getName()};
    }
}
