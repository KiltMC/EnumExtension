package xyz.bluspring.enumextension.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import xyz.bluspring.enumextension.extensions.SpawnPlacementsTypeExtension;
import xyz.bluspring.enumextension.util.TriPredicate;

@Mixin(SpawnPlacements.Type.class)
public class SpawnPlacementsTypeMixin implements SpawnPlacementsTypeExtension {
    @Unique
    private TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> predicate;

    @Shadow
    @Final
    public static SpawnPlacements.Type NO_RESTRICTIONS;

    @Override
    public boolean enumExtension$canSpawnAt(LevelReader world, BlockPos pos, EntityType<? extends Mob> type) {
        if ((Object) this == NO_RESTRICTIONS)
            return true;

        if (predicate == null)
            return NaturalSpawner.isSpawnPositionOk((SpawnPlacements.Type) (Object) this, world, pos, type);

        return predicate.test(world, pos, type);
    }

    @Override
    public TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> enumExtension$getPredicate() {
        return predicate;
    }

    @Override
    public void enumExtension$setPredicate(TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> predicate) {
        this.predicate = predicate;
    }
}
