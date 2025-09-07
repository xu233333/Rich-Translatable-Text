package xu_mod.rich_translatable_text.Mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xu_mod.rich_translatable_text.RichTranslatableText;
import xu_mod.rich_translatable_text.Text.RichTextLanguageInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.BiConsumer;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin implements RichTextLanguageInterface {
    @Unique
    private static Map<String, Text> richTranslatableText$RichTranslations_Building;
    @Unique
    private Map<String, Text> richTranslatableText$RichTranslations;

    @Override
    public Text richTranslatableText$GetText(String key) {
        return this.richTranslatableText$RichTranslations.get(key);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void richTranslatableText$oninit(Map translations, boolean rightToLeft, CallbackInfo ci) {
        this.richTranslatableText$RichTranslations = richTranslatableText$RichTranslations_Building;
        richTranslatableText$RichTranslations_Building = null;
    }

    @Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;", at = @At("HEAD"))
    private static void richTranslatableText$onload(ResourceManager resourceManager, List<String> definitions, boolean rightToLeft, CallbackInfoReturnable<TranslationStorage> cir) {
        richTranslatableText$loadRichLang(resourceManager, definitions, rightToLeft);
    }

    @Unique
    private static void richTranslatableText$loadRichLang(ResourceManager resourceManager, List<String> definitions, boolean rightToLeft) {
        richTranslatableText$RichTranslations_Building = new HashMap<>();
        for (String Lang : definitions) {
            String RichLangPath = String.format(Locale.ROOT, "rich_lang/%s.json", Lang);
            for (String NameSpace : resourceManager.getAllNamespaces()) {
                try {
                    Identifier identifier = new Identifier(NameSpace, RichLangPath);
                    richTranslatableText$loadRichLang(Lang, resourceManager.getAllResources(identifier), richTranslatableText$RichTranslations_Building);
                } catch (Exception exception) {
                    RichTranslatableText.LOGGER.warn("Skipped rich language file: {}:{} ({})", new Object[]{NameSpace, RichLangPath, exception.toString()});
                }
            }
        }
    }

    @Unique
    private static void richTranslatableText$loadRichLang(String langCode, List<Resource> resourceRefs, Map<String, Text> translations) {

        for (Resource resource : resourceRefs) {
            try {
                InputStream inputStream = resource.getInputStream();

                try {
                    Objects.requireNonNull(translations);
                    richTranslatableText$loadText(inputStream, translations::put);
                } catch (Throwable exception) {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var8) {
                            exception.addSuppressed(var8);
                        }
                    }
                    throw exception;
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException iOException) {
                RichTranslatableText.LOGGER.warn("Failed to load rich translations for {} from pack {}", new Object[]{langCode, resource.getResourcePackName(), iOException});
            }
        }
    }

    @Unique
    private static void richTranslatableText$loadText(InputStream inputStream, BiConsumer<String, Text> entryConsumer) {
        JsonObject jsonObject = (JsonObject)Language.GSON.fromJson(new InputStreamReader(inputStream, StandardCharsets.UTF_8), JsonObject.class);

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : jsonObject.entrySet()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry) stringJsonElementEntry;
            JsonElement Value = entry.getValue();
            if (JsonHelper.isString(Value)) {
                String string = Language.TOKEN_PATTERN.matcher(JsonHelper.asString(Value, (String) entry.getKey())).replaceAll("%$1s");
                entryConsumer.accept((String) entry.getKey(), Text.of(string));
            }
            else {
                MutableText mutableText = Text.Serializer.fromJson(Value);
                entryConsumer.accept((String) entry.getKey(), mutableText);
            }
        }
    }

    @Inject(method = "hasTranslation", at = @At("HEAD"), cancellable = true)
    private void richTranslatableText$hasTranslation(String key, CallbackInfoReturnable<Boolean> cir) {
        if (this.richTranslatableText$RichTranslations.containsKey(key)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private void richTranslatableText$get(String key, String fallback, CallbackInfoReturnable<String> cir) {
        if (this.richTranslatableText$RichTranslations.containsKey(key)) {
            cir.setReturnValue(this.richTranslatableText$RichTranslations.get(key).getString());
        }
    }
}