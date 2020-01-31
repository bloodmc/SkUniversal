package us.donut.skuniversal.griefdefender;

import org.bukkit.Bukkit;

import com.griefdefender.api.User;
import com.griefdefender.api.event.BorderClaimEvent;
import com.griefdefender.api.event.RemoveClaimEvent;
import com.griefdefender.permission.GDPermissionUser;

import net.kyori.event.method.annotation.Subscribe;
import us.donut.skuniversal.griefdefender.events.SGDBorderClaimEvent;
import us.donut.skuniversal.griefdefender.events.SGDRemoveClaimEvent;

public class GDEventListener {

    @Subscribe
    public void onBorderClaimEvent(BorderClaimEvent event) {
        final User user = event.getUser().orElse(null);
        if (user == null) {
            return;
        }

        final GDPermissionUser gdUser = (GDPermissionUser) user;
        Bukkit.getServer().getPluginManager().callEvent(new SGDBorderClaimEvent(gdUser.getOnlinePlayer(), event.getEnterClaim(), event.getExitClaim()));
    }

    @Subscribe
    public void onRemoveClaimAbandonEvent(RemoveClaimEvent.Abandon event) {
        Bukkit.getServer().getPluginManager().callEvent(new SGDRemoveClaimEvent.Abandon(event.getClaims()));
    }

    @Subscribe
    public void onRemoveClaimDeleteEvent(RemoveClaimEvent.Delete event) {
        Bukkit.getServer().getPluginManager().callEvent(new SGDRemoveClaimEvent.Delete(event.getClaims()));
    }

    @Subscribe
    public void onRemoveClaimAbandonEvent(RemoveClaimEvent.Expire event) {
        Bukkit.getServer().getPluginManager().callEvent(new SGDRemoveClaimEvent.Expire(event.getClaims()));
    }
}
