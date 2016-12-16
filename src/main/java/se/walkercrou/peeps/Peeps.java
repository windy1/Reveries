package se.walkercrou.peeps;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.GameRegistry;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.EventManager;
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
import se.walkercrou.peeps.data.base.ImmutableNpcData;
import se.walkercrou.peeps.data.base.NpcData;
import se.walkercrou.peeps.data.impl.base.NpcDataManipulatorBuilder;
import se.walkercrou.peeps.data.impl.base.PeepsNpcData;
import se.walkercrou.peeps.data.impl.sight.SightdataManipulatorBuilder;
import se.walkercrou.peeps.data.sight.ImmutableSightData;
import se.walkercrou.peeps.data.sight.SightData;
import se.walkercrou.peeps.event.EntityListener;
import se.walkercrou.peeps.event.NpcListener;
import se.walkercrou.peeps.property.NpcProperty;
import se.walkercrou.peeps.property.NpcPropertyRegistryModule;
import se.walkercrou.peeps.trait.NpcTrait;
import se.walkercrou.peeps.trait.NpcTraitRegistryModule;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.inject.Inject;

@Plugin(id = "peeps", authors = { "windy" })
public final class Peeps {

    public static Peeps INSTANCE;

    @Inject public Logger log;
    @Inject public PluginContainer self;
    @Inject public Game game;

    private final CommandRegistrar commands = new CommandRegistrar(this);
    private final EntityListener entityListener = new EntityListener();
    private final NpcListener npcListener = new NpcListener();

    private final Map<UUID, NpcMonitor> monitors = Maps.newHashMap();

    @Listener
    public void onPreInit(GamePreInitializationEvent event) {
        this.log.info("Initializing...");
        INSTANCE = this;
        DataManager data = this.game.getDataManager();
        data.register(NpcData.class, ImmutableNpcData.class, new NpcDataManipulatorBuilder());
        data.register(SightData.class, ImmutableSightData.class, new SightdataManipulatorBuilder());
    }

    @Listener
    public void onStart(GameStartedServerEvent event) {
        this.log.info("Starting...");

        GameRegistry reg = this.game.getRegistry();
        reg.registerModule(NpcTrait.class, new NpcTraitRegistryModule());
        reg.registerModule(NpcProperty.class, new NpcPropertyRegistryModule());

        System.out.println("properties");
        reg.getAllOf(NpcProperty.class).forEach(p -> this.log.info(p.getId()));

        if (init())
            this.log.info("Started.");
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        this.log.info("Reloading...");
        this.commands.deregister();
        EventManager events = this.game.getEventManager();
        events.unregisterListeners(this.entityListener);
        events.unregisterListeners(this.npcListener);
        if (init())
            this.log.info("Reloaded.");
    }

    public boolean init() {
        EventManager events = this.game.getEventManager();
        events.registerListeners(this, this.entityListener);
        events.registerListeners(this, this.npcListener);
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
        checkNotNull(location, "null location");
        checkNotNull(cause, "null cause");
        Optional<Identifiable> owner = cause.get(NamedCause.OWNER, Identifiable.class);
        checkArgument(owner.isPresent(), "cause owner must be identifiable");

        World world = location.getExtent();
        Living entity = (Living) world.createEntity(entityType, location.getPosition());
        if (!world.spawnEntity(entity, cause) || !Living.class.isAssignableFrom(entityType.getEntityClass()))
            throw new NpcSpawnException("could not spawn NPC");

        entity.offer(new PeepsNpcData(owner.get().getUniqueId(), null, Sets.newHashSet()));

        Sponge.getScheduler().createTaskBuilder()
            .delayTicks(1)
            .intervalTicks(1)
            .execute(new NpcMonitor(this, entity))
            .submit(this);

        return entity;
    }

    public boolean isMonitoring(Living living) {
        return this.monitors.containsKey(living.getUniqueId());
    }

    public void monitor(Living living) {
        checkArgument(living.get(NpcData.class).isPresent(), "entity is not NPC");
        checkArgument(!isMonitoring(living), "entity being monitored already");
        NpcMonitor monitor = new NpcMonitor(this, living);
        this.monitors.put(living.getUniqueId(), monitor);
        Sponge.getScheduler().createTaskBuilder()
            .delayTicks(1)
            .intervalTicks(1)
            .execute(monitor)
            .submit(this);
    }

}
