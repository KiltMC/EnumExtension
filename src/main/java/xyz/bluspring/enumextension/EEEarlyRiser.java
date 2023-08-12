package xyz.bluspring.enumextension;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class EEEarlyRiser implements Runnable {
    @Override
    public void run() {
        // EnchantmentCategory has an abstract method that doesn't exactly play nicely with enum extension.
        // So, we need to modify it to have a method body that works with the Forge API.

        var mappingResolver = FabricLoader.getInstance().getMappingResolver();

        var enchantmentCategory = mappingResolver.mapClassName("intermediary", "net.minecraft.class_1886").replace(".", "/");
        ClassTinkerers.addTransformation(enchantmentCategory, (classNode) -> {
            classNode.access = Opcodes.ACC_PUBLIC | Opcodes.ACC_ENUM;

            var canEnchantName = mappingResolver.mapMethodName("intermediary", "net.minecraft.class_1886", "method_8177", "(Lnet/minecraft/class_1792;)Z");
            // remove it first
            classNode.methods.removeIf(it -> it.name.equals(canEnchantName));

            var item = mappingResolver.mapClassName("intermediary", "net.minecraft.class_1792").replace(".", "/");
            var enchCategoryInjection = "xyz/bluspring/enumextension/extensions/EnchantmentCategoryExtension";

            // This method should become:
                    /*
                    public boolean canEnchant(Item item) {
                        return ((EnchantmentCategoryInjection) this).getPredicate() != null && ((EnchantmentCategoryInjection) this).getPredicate().test(item);
                    }
                     */
            var canEnchant = classNode.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    canEnchantName, "(L" + item.replace(".", "/") + ";)Z",
                    null, null
            );

            canEnchant.visitCode();

            var label0 = new Label();
            var label1 = new Label();
            var label2 = new Label();
            var label3 = new Label();

            canEnchant.visitLabel(label0);
            // if (((EnchantmentCategoryInjection) this).getDelegate() != null)
            canEnchant.visitVarInsn(Opcodes.ALOAD, 0);
            canEnchant.visitTypeInsn(Opcodes.CHECKCAST, enchCategoryInjection);
            canEnchant.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    enchCategoryInjection,
                    "enumExtension$getDelegate",
                    "()Ljava/util/function/Predicate;",
                    true
            );
            canEnchant.visitJumpInsn(Opcodes.IFNULL, label1);

            // ((EnchantmentCategoryInjection) this).getDelegate().test(item)
            canEnchant.visitVarInsn(Opcodes.ALOAD, 0);
            canEnchant.visitTypeInsn(Opcodes.CHECKCAST, enchCategoryInjection);
            canEnchant.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    enchCategoryInjection,
                    "enumExtension$getDelegate",
                    "()Ljava/util/function/Predicate;",
                    true
            );
            canEnchant.visitVarInsn(Opcodes.ALOAD, 1);
            canEnchant.visitMethodInsn(
                    Opcodes.INVOKEINTERFACE,
                    "java/util/function/Predicate",
                    "test",
                    "(Ljava/lang/Object;)Z",
                    true
            );
            canEnchant.visitJumpInsn(Opcodes.IFEQ, label1);
            canEnchant.visitInsn(Opcodes.ICONST_1);
            canEnchant.visitJumpInsn(Opcodes.GOTO, label2);

            // &&
            canEnchant.visitLabel(label1);
            canEnchant.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            canEnchant.visitInsn(Opcodes.ICONST_0);

            canEnchant.visitLabel(label2);
            canEnchant.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Integer[] { Opcodes.INTEGER });
            canEnchant.visitInsn(Opcodes.IRETURN);

            canEnchant.visitLabel(label3);
            canEnchant.visitLocalVariable(
                    "this",
                    "L" + enchantmentCategory.replace(".", "/") + ";",
                    null,
                    label0,
                    label3,
                    0
            );
            canEnchant.visitLocalVariable("item", "L" + item + ";", null, label0, label3, 1);

            canEnchant.visitMaxs(0, 0);

            canEnchant.visitEnd();
        });
    }
}
