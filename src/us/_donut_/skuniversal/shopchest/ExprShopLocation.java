package us._donut_.skuniversal.shopchest;

import ch.njol.skript.doc.Description;
import ch.njol.skript.doc.Examples;
import ch.njol.skript.doc.Name;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import de.epiceric.shopchest.shop.Shop;
import org.bukkit.Location;
import org.bukkit.event.Event;

import javax.annotation.Nullable;

@Name("ShopChest - Shop Location")
@Description("Returns the location of a shop.")
@Examples({"send \"%the location of shop with id (shop at player)%\""})
public class ExprShopLocation extends SimpleExpression<Location> {

    private Expression<Number> id;

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
    public boolean init(Expression<?>[] e, int i, Kleenean kl, SkriptParser.ParseResult pr) {
        id = (Expression<Number>) e[0];
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean b) {
        return "the location of the shop with id " + id.toString(e, b);
    }

    @Override
    @Nullable
    protected Location[] get(Event e) {
        Shop shop = ShopChestRegister.getShop(id.getSingle(e).intValue());
        return shop == null ? null : new Location[]{shop.getLocation()};
    }
}