package se.walkercrou.peeps;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.NamedCause;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.util.Identifiable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import se.walkercrou.peeps.cmd.CommandRegistrar;
import se.walkercrou.peeps.data.ImmutableNpcData;
import se.walkercrou.peeps.data.NpcData;
import se.walkercrou.peeps.data.impl.NpcDataManipulatorBuilder;
import se.walkercrou.peeps.data.impl.PeepsNpcData;
import se.walkercrou.peeps.trait.NpcTrait;
import se.walkercrou.peeps.trait.NpcTraitRegistryModule;
import se.walkercrou.peeps.trait.NpcTraits;

import java.util.Optional;

import javax.inject.Inject;

@Plugin(id = "peeps", authors = { "windy" })
public final class Peeps {

    @Inject public Logger log;
    @Inject public PluginContainer self;
    @Inject public Game game;

    private final CommandRegistrar commands = new CommandRegistrar(this);
    private final EntityListener entityListener = new EntityListener();

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.log.info("Initializing...");
        this.game.getDataManager().register(NpcData.class, ImmutableNpcData.class, new NpcDataManipulatorBuilder());
    }

    @Listener
    public void onStart(GameStartedServerEvent event) {
        this.log.info("Starting...");
        this.game.getRegistry().registerModule(NpcTrait.class, new NpcTraitRegistryModule());
        System.out.println(this.game.getRegistry().getAllOf(NpcTrait.class));
        System.out.println("id = " + NpcTraits.INVULNERABLE.getId());
        if (init())
            this.log.info("Started.");
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        this.log.info("Reloading...");
        this.commands.deregister();
        this.game.getEventManager().unregisterListeners(this.entityListener);
        if (init())
            this.log.info("Reloaded.");
    }

    public boolean init() {
        this.game.getEventManager().registerListeners(this, this.entityListener);
        this.commands.register();
        return true;
    }

    @Listener
    public void onStop(GameStoppedEvent event) {
        this.log.info("Stopped.");
    }

    public Living createAndSpawn(EntityType entityType, Location<World> location, Cause cause)
        throws NpcSpawnException {

        checkNotNull(entityType, "null entity type");
        checkArgument(Living.class.isAssignableFrom(entityType.getEntityClass()), "entity type must be living");
        checkNotNull(location, "null location");
        checkNotNull(cause, "null cause");
        Optional<Identifiable> owner = cause.get(NamedCause.OWNER, Identifiable.class);
        checkArgument(owner.isPresent(), "cause owner must be identifiable");

        World world = location.getExtent();
        Living entity = (Living) world.createEntity(entityType, location.getPosition());
        if (!world.spawnEntity(entity, cause))
            throw new NpcSpawnException("could not spawn NPC");

        entity.offer(new PeepsNpcData(owner.get().getUniqueId(), null, Sets.newHashSet()));

        return entity;
    }

}
