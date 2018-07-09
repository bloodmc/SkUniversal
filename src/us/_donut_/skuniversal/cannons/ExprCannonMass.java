package us._donut_.skuniversal.cannons;

import at.pavlov.cannons.Cannons;
import at.pavlov.cannons.cannon.Cannon;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import javax.annotation.Nullable;
import java.util.UUID;

@Name("Cannons - Cannon Mass")
@Description("Returns the mass of a cannon.")
@Examples({"send \"%the mass of the cannon with id (id of cannon at player)%\""})
public class ExprCannonMass extends SimpleExpression<Number> {

    private Expression<String> id;

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
        id = (Expression<String>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "the mass of the cannon with ID " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        Cannon cannon = Cannons.getPlugin().getCannon(UUID.fromString(id.getSingle(e)));
        return cannon == null ? null : new Number[]{cannon.getCannonDesign().getMassOfCannon()};
    }
}