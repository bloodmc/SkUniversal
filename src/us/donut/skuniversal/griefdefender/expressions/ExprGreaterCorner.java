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
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.Event;

import com.flowpowered.math.vector.Vector3i;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import javax.annotation.Nullable;

import java.util.UUID;

@Name("GriefDefender - Greater Corner")
@Description("Returns the greater corner of a claim.")
@Examples({"send \"%the greater corner of the claim with id (id of the basic claim at player)%\""})
public class ExprGreaterCorner extends SimpleExpression<Location> {

    static {
        Skript.registerExpression(ExprGreaterCorner.class, Location.class, ExpressionType.COMBINED, "[the] greater [boundary] corner [loc[ation]] of [the] [G[rief]D[efender]] claim [with ID] %number%");
    }

    private Expression<UUID> id;

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public Class<? extends Location> getReturnType() {
        return Location.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult pr) {
        id = (Expression<UUID>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "greater boundary corner of claim with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected Location[] get(Event e) {
        Claim claim = GriefDefender.getCore().getClaim(id.getSingle(e));
        if (claim == null) return null;

        final Vector3i vec = claim.getGreaterBoundaryCorner();
        final World world = Bukkit.getWorld(claim.getWorldUniqueId());
        if (world == null) {
            return null;
        }
        return new Location[]{new Location(world, vec.getX(), vec.getY(), vec.getZ())};
    }

}