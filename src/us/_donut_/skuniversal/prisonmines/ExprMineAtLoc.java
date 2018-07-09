package us._donut_.skuniversal.prisonmines;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.lightshard.prisonmines.MineAPI;
import org.bukkit.Location;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("PrisonMines - Mine at Location")
@Description("Returns the name of a mine at a location.")
@Examples({"send \"%the mine at player%\""})
public class ExprMineAtLoc extends SimpleExpression<String> {

    private Expression<Location> loc;

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
        loc = (Expression<Location>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "name of mine at location " + loc.toString(e, b);
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        return new String[]{new MineAPI.PrisonMinesAPI().getByLocation(loc.getSingle(e)).getName()};
    }
}