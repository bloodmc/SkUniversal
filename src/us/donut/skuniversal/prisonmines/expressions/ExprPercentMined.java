package us.donut.skuniversal.prisonmines.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import net.lightshard.prisonmines.mine.Mine;
import org.bukkit.event.Event;
import javax.annotation.Nullable;

import static us.donut.skuniversal.prisonmines.PrisonMinesHook.*;

@Name("PrisonMines - Percent Mined")
@Description("Returns the percentage of a mine that has been mined.")
@Examples({"send \"%the percentage of the blocks mined in the mine named (name of mine at player)%\""})
public class ExprPercentMined extends SimpleExpression<Number> {

    static {
        Skript.registerExpression(ExprPercentMined.class, Number.class, ExpressionType.COMBINED, "[the] percent[age] of [the] blocks [already] mined in [the] [PrisonMines] mine [(named|with name)] %string%");
    }

    private Expression<String> name;

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
        name = (Expression<String>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "percentage of blocks mined in mine named " + name.toString(e, b);
    }

    @Override
    @Nullable
    protected Number[] get(Event e) {
        Mine mine;
        if (name.getSingle(e) == null || (mine = mineAPI.getByName(name.getSingle(e))) == null) return null;
        return new Number[]{mineAPI.getPercentMined(mine)};
    }
}
