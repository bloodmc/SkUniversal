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

import com.google.common.collect.ImmutableList;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.event.EventCause;
import com.griefdefender.event.GDCauseStackManager;

import net.kyori.text.Component;

import java.util.List;
import java.util.Optional;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class SGDClaimEvent extends Event {

    private List<Claim> claims;
    private Component message;
    private boolean isCancelled = false;

    public SGDClaimEvent(Claim claim) {
        this.claims = ImmutableList.of(claim);
    }

    public SGDClaimEvent(List<Claim> claims) {
        this.claims = ImmutableList.copyOf(claims);
    }

    public boolean cancelled() {
        return this.isCancelled;
    }

    public void cancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public Claim getClaim() {
        return this.getClaims().get(0);
    }

    public List<Claim> getClaims() {
        return this.claims;
    }

    public void setMessage(Component message) {
        this.message = message;
    }

    public Optional<Component> getMessage() {
        return Optional.ofNullable(this.message);
    }

    public EventCause getCause() {
        return GDCauseStackManager.getInstance().getCurrentCause();
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
