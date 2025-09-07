package xu_mod.rich_translatable_text.Command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;


public class RTTCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("rich_translatable_text")
                        .then(CommandManager.literal("log_test_text").executes(RTTCommand::LogTestText)
                )
        );
    }

    private static int LogTestText(CommandContext<ServerCommandSource> commandContext) throws CommandSyntaxException {
        commandContext.getSource().sendMessage(Text.translatable("text.rich_translatable_text.test_lang"));
        return 1;
    }

}
