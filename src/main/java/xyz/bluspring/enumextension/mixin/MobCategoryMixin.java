package xyz.bluspring.enumextension.mixin;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.bluspring.enumextension.IExtensibleEnum;
import xyz.bluspring.enumextension.extensions.MobCategoryExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(MobCategory.class)
public abstract class MobCategoryMixin implements MobCategoryExtension, IExtensibleEnum {
    @Shadow
    @Final
    @Mutable
    public static Codec<MobCategory> CODEC;

    @Shadow public abstract String getName();

    @Override
    public void init() {
        BY_NAME.put(this.getName(), (MobCategory) (Object) this);
    }

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void enumExtension$replaceCodec(CallbackInfo ci) {
        CODEC = IExtensibleEnum.createCodecForExtensibleEnum(MobCategory::values, MobCategoryExtension::byName);

        var values = Arrays.stream(MobCategory.values()).collect(Collectors.toMap(MobCategory::getName, mobCategory -> mobCategory));
        MobCategoryExtension.BY_NAME.putAll(values);
    }
}
