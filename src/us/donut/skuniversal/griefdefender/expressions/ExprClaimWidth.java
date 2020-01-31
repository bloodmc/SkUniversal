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
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import javax.annotation.Nullable;

import java.util.UUID;

@Name("GriefDefender - Claim Width")
@Description("Returns the width of a claim.")
@Examples({"send \"%the width of the claim with id (id of the basic claim at player)%\""})
public class ExprClaimWidth extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprClaimWidth.class, Number.class, ExpressionType.COMBINED, "[the] width of [the] [G[rief]D[efender]] claim [with ID] %number%");
    }

    private Expression<UUID> id;

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
        id = (Expression<UUID>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "width of claim with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        Claim claim = GriefDefender.getCore().getClaim(id.getSingle(e));
        return claim == null ? null : new Integer[]{claim.getWidth()};
    }

}