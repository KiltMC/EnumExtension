package xyz.bluspring.enumextension.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import xyz.bluspring.enumextension.IExtensibleEnum;
import xyz.bluspring.enumextension.extensions.EnchantmentCategoryExtension;

import java.util.function.Predicate;

@Mixin(EnchantmentCategory.class)
public class EnchantmentCategoryMixin implements EnchantmentCategoryExtension, IExtensibleEnum {
    @Unique
    private Predicate<Item> delegate;

    @Override
    public Predicate<Item> enumExtension$getDelegate() {
        return delegate;
    }

    @Override
    public void enumExtension$setDelegate(Predicate<Item> delegate) {
        this.delegate = delegate;
    }
}
