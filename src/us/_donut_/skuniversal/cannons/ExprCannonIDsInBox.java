package us._donut_.skuniversal.cannons;

import at.pavlov.cannons.cannon.CannonManager;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.Location;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("Cannons - Cannon IDs within Box")
@Description("Returns all cannon IDs within a box.")
@Examples({"send \"%ids of the cannons within the box centered at player with length 5, width 5, and height 5%\""})
public class ExprCannonIDsInBox extends SimpleExpression<String> {

    private Expression<Location> center;
    private Expression<Number> length;
    private Expression<Number> width;
    private Expression<Number> height;

    @Override
    public boolean isSingle() {
        return false;
    }

    @Override
    public Class<? extends String> getReturnType() {
        return String.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int i, Kleenean kl, SkriptParser.ParseResult pr) {
        center = (Expression<Location>) e[0];
        length = (Expression<Number>) e[1];
        width = (Expression<Number>) e[1];
        height = (Expression<Number>) e[1];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "the IDs of the cannons within the box centered at " + center.toString(e, b) + " with length " + length.toString(e, b) + " with width " + width.getSingle(e) + " with height " + height.toString(e, b);
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        return CannonManager.getCannonsInBox(center.getSingle(e), length.getSingle(e).doubleValue(), width.getSingle(e).doubleValue(), height.getSingle(e).doubleValue()).stream().map(cannon -> cannon.getUID().toString()).toArray(String[]::new);
    }

}