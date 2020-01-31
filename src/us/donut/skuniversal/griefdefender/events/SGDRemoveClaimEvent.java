/*
 * This file is part of GriefDefender, licensed under the MIT License (MIT).
 *
 * Copyright (c) bloodmc
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package us.donut.skuniversal.griefdefender.events;

import com.griefdefender.GriefDefenderPlugin;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.event.RemoveClaimEvent;
import com.griefdefender.configuration.category.ClaimCategory;

import java.util.List;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SGDRemoveClaimEvent extends SGDClaimEvent {

    private boolean shouldRestore;

    public SGDRemoveClaimEvent(Claim claim) {
        super(claim);
        final ClaimCategory claimCategory = GriefDefenderPlugin.getActiveConfig(claim.getWorldUniqueId()).getConfig().claim;
        if (claimCategory.claimAutoNatureRestore || claimCategory.claimAutoSchematicRestore) {
            shouldRestore = true;
        } else {
            shouldRestore = false;
        }
    }

    public SGDRemoveClaimEvent(List<Claim> claims) {
        super(claims);
    }

    public static class Abandon extends SGDRemoveClaimEvent implements RemoveClaimEvent.Abandon {

        public Abandon(Claim claim) {
            super(claim);
        }

        public Abandon(List<Claim> claims) {
            super(claims);
        }
    }

    public static class Delete extends SGDRemoveClaimEvent implements RemoveClaimEvent.Delete {

        public Delete(Claim claim) {
            super(claim);
        }

        public Delete(List<Claim> claims) {
            super(claims);
        }
    }

    public static class Expire extends SGDRemoveClaimEvent implements RemoveClaimEvent.Expire {

        public Expire(Claim claim) {
            super(claim);
        }

        public Expire(List<Claim> claims) {
            super(claims);
        }
    }

    public boolean isRestoring() {
        return shouldRestore;
    }

    public void shouldRestore(boolean restore) {
        this.shouldRestore = restore;
    }

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
