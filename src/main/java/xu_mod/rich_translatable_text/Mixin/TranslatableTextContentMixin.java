package xu_mod.rich_translatable_text.Mixin;

import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xu_mod.rich_translatable_text.Text.RichTextLanguageInterface;

import java.util.ArrayList;
import java.util.List;

@Mixin(TranslatableTextContent.class)
public abstract class TranslatableTextContentMixin {

    @Shadow
    private Language languageCache;

    @Shadow
    private List<StringVisitable> translations;

    @Shadow public abstract String getKey();

    @Inject(method = "updateTranslations", at = @At("HEAD"), cancellable = true)
    private void updateTranslationsWithRichText(CallbackInfo ci) {
        Language language = Language.getInstance();
        if (language == this.languageCache) {
            ci.cancel();
        }
        if (language instanceof RichTextLanguageInterface RTL) {
            Text text = RTL.richTranslatableText$GetText(this.getKey());
            if (text != null) {
                this.translations = new ArrayList<>();
                this.translations.add(text);
            }
        }
    }
}
