package us.donut.skuniversal.griefdefender.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;

import javax.annotation.Nullable;

import java.util.UUID;

@Name("GriefDefender - Delete Claim")
@Description("Deletes claim(s).")
@Examples({"delete claims with ids (ids of the claims at player)"})
public class EffDeleteClaim extends Effect {

    static {
        Skript.registerEffect(EffDeleteClaim.class, "(delete|remove) [the] [G[rief]D[efender]] claim[s] [with ID[s]] %numbers%");
    }

    private Expression<UUID> id;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult p) {
        id = (Expression<UUID>) e[0];
        return true;
    }
    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "delete claim(s) with id(s) " + id.getSingle(e);
    }

    @Override
    protected void execute(Event e) {
        final UUID[] uuids = id.getArray(e);
        if (uuids == null) return;
        for (UUID claimID : uuids) {
            Claim claim = GriefDefender.getCore().getClaim(claimID);
            if (claim == null) return;
            GriefDefender.getCore().getClaimManager(claim.getWorldUniqueId()).deleteClaim(claim);
        }
    }

}