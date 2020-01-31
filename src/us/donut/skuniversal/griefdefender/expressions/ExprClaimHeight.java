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

@Name("GriefDefender - Claim Height")
@Description("Returns the height of a claim.")
@Examples({"send \"%the height of the claim with id (id of the basic claim at player)%\""})
public class ExprClaimHeight extends SimpleExpression<Integer> {

    static {
        Skript.registerExpression(ExprClaimHeight.class, Integer.class, ExpressionType.COMBINED, "[the] height of [the] [G[rief]D[efender]] claim");
    }

    private Expression<UUID> id;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Integer> getReturnType() {
        return Integer.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        id = (Expression<UUID>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "height of claim with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected Integer[] get(Event e) {
        Claim claim = GriefDefender.getCore().getClaim(id.getSingle(e));
        return claim == null ? null : new Integer[]{claim.getHeight()};
    }

}