package xu_mod.rich_translatable_text;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xu_mod.rich_translatable_text.Command.RTTCommand;

public class RichTranslatableText implements ModInitializer {
	public static final String MOD_ID = "rich_translatable_text";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RTTCommand.register(dispatcher));
	}
}