package us._donut_.skuniversal.skywars_daboross;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.daboross.bukkitdev.skywars.api.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

@Name("SkyWars (Daboross) - Next Arena")
@Description("Returns the name of the next arena.")
@Examples({"send \"%next skywars arena%\""})
public class ExprNextArena extends SimpleExpression<String> {

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
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "next arena";
    }

    @Override
    @Nullable
    protected String[] get(Event e) {
        SkyWars sw = (SkyWars) Bukkit.getPluginManager().getPlugin("SkyWars");
        return new String[]{sw.getGameQueue().getPlannedArena().getArenaName()};
    }
}
