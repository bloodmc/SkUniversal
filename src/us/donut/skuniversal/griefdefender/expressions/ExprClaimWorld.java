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

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import javax.annotation.Nullable;

import java.util.UUID;

@Name("GriefDefender - Claim World")
@Description("Returns the world of a claim.")
@Examples({"send \"%the world of the claim with id (id of the basic claim at player)%\""})
public class ExprClaimWorld extends SimpleExpression<World> {

    static {
        Skript.registerExpression(ExprClaimWorld.class, World.class, ExpressionType.COMBINED, "[the] world of [the] [G[rief]D[efender]] claim [with ID] %number%");
    }

    private Expression<UUID> id;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends World> getReturnType() {
        return World.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        id = (Expression<UUID>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "world of claim with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected World[] get(Event e) {
        Claim claim = GriefDefender.getCore().getClaim(id.getSingle(e));
        if (claim == null) return null;

        final World world = Bukkit.getWorld(claim.getWorldUniqueId());
        if (world == null) return null;
        return new World[] {world};
    }

}