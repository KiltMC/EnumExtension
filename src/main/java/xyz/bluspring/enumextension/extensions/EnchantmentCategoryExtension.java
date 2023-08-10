package xyz.bluspring.enumextension.extensions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import xyz.bluspring.enumextension.EnumUtils;
import xyz.bluspring.enumextension.mixin.EnchantmentCategoryAccessor;

import java.util.function.Predicate;

public interface EnchantmentCategoryExtension {
    static EnchantmentCategory create(String name, Predicate<Item> predicate) {
        var value = EnumUtils.addEnumToClass(
                EnchantmentCategory.class,
                EnchantmentCategoryAccessor.getValues(),
                name, (size) -> EnchantmentCategoryAccessor.createEnchantmentCategory(name, size),
                (values) -> EnchantmentCategoryAccessor.setValues(values.toArray(new EnchantmentCategory[0]))
        );

        ((EnchantmentCategoryExtension) (Object) value).enumExtension$setDelegate(predicate);

        return value;
    }

    Predicate<Item> enumExtension$getDelegate();
    void enumExtension$setDelegate(Predicate<Item> value);
}
