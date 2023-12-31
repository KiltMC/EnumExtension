package xyz.bluspring.enumextension.extensions;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelReader;
import xyz.bluspring.enumextension.EnumUtils;
import xyz.bluspring.enumextension.mixin.SpawnPlacementsTypeAccessor;
import xyz.bluspring.enumextension.util.TriPredicate;

public interface SpawnPlacementsTypeExtension {
    static SpawnPlacements.Type create(String name, TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> predicate) {
        var value = EnumUtils.addEnumToClass(
                SpawnPlacements.Type.class,
                SpawnPlacementsTypeAccessor.getValues(),
                name, (size) -> SpawnPlacementsTypeAccessor.createType(name, size),
                (values) -> SpawnPlacementsTypeAccessor.setValues(values.toArray(new SpawnPlacements.Type[0]))
        );

        ((SpawnPlacementsTypeExtension) (Object) value).enumExtension$setPredicate(predicate);
        return value;
    }

    default boolean enumExtension$canSpawnAt(LevelReader world, BlockPos pos, EntityType<? extends Mob> type) {
        throw new IllegalStateException();
    }

    default void enumExtension$setPredicate(TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> predicate) {
        throw new IllegalStateException();
    }

    default TriPredicate<LevelReader, BlockPos, EntityType<? extends Mob>> enumExtension$getPredicate() {
        throw new IllegalStateException();
    }
}
