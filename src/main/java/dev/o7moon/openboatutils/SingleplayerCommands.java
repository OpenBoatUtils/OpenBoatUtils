package dev.o7moon.openboatutils;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.o7moon.openboatutils.network.FriendlyByteBufWriter;
import io.github.openboatutils.protocol.channels.OBUSettingsPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.minecraft.commands.Commands.*;

public interface SingleplayerCommands {

    static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        var setting = getSettingNode();
        var context = getContextNode();

        dispatcher.register(literal("obu")
                .then(setting)
                .then(context));

        dispatcher.register(setting);
        dispatcher.register(context);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> getSettingNode() {
        return getBoatUtilsNode(literal("setting"), (context, packet) -> {
            if (context.getSource().getPlayer() instanceof ServerPlayer player) {
                FriendlyByteBufWriter writer = new FriendlyByteBufWriter();

                try {
                    packet.write(writer);
                    OpenBoatUtils.SETTING_CHANNEL.sendPacketS2C(player, writer.getByteBuf());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return Command.SINGLE_SUCCESS;
        });
    }

    private static LiteralArgumentBuilder<CommandSourceStack> getContextNode() {
        return getBoatUtilsNode(literal("context"), (context, packet) -> {
            if (context.getSource().getPlayer() instanceof ServerPlayer player) {
                FriendlyByteBufWriter writer = new FriendlyByteBufWriter();

                try {
                    packet.write(writer);
                    OpenBoatUtils.SETTING_CHANNEL.sendPacketS2C(player, writer.getByteBuf());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return Command.SINGLE_SUCCESS;
        });
    }

    @FunctionalInterface
    interface PacketConsumer {
        int consume(CommandContext<CommandSourceStack> context, OBUSettingsPacket packet) throws CommandSyntaxException;
    }

    // this is kinda reflection BS but this is considerably better that what we used to have

    private static LiteralArgumentBuilder<CommandSourceStack> getBoatUtilsNode(
            LiteralArgumentBuilder<CommandSourceStack> root, PacketConsumer consumer) {
        for (Class<?> subclass : OBUSettingsPacket.class.getPermittedSubclasses()) {
            String name = subclass.getSimpleName().toLowerCase();
            LiteralArgumentBuilder<CommandSourceStack> top = literal(name);

            List<Field> fields = new ArrayList<>();
            List<ArgumentType<?>> types = new ArrayList<>();
            for (Field declaredField : subclass.getDeclaredFields()) {
                ArgumentType<?> argumentType = resolveArgumentType(declaredField.getType());
                if (argumentType == null)
                    continue;
                declaredField.setAccessible(true);
                fields.add(declaredField);
                types.add(argumentType);
            }

            if (fields.isEmpty()) {
                top.executes(ctx -> {
                    OBUSettingsPacket packet = buildPacket(subclass, ctx, fields);

                    return consumer.consume(ctx, packet);
                });
            } else {
                int last = fields.size() - 1;
                RequiredArgumentBuilder<CommandSourceStack, ?> chain =
                        createArgument(fields.get(last), types.get(last));
                chain.executes(ctx -> {
                    OBUSettingsPacket packet = buildPacket(subclass, ctx, fields);
                    return consumer.consume(ctx, packet);
                });

                for (int i = last - 1; i >= 0; i--) {
                    RequiredArgumentBuilder<CommandSourceStack, ?> node = createArgument(fields.get(i), types.get(i));
                    node.then(chain);
                    chain = node;
                }

                top.then(chain);
            }

            root.then(top);
        }
        return root;
    }

    private static RequiredArgumentBuilder<CommandSourceStack, ?> createArgument(Field field, ArgumentType<?> type) {
        RequiredArgumentBuilder<CommandSourceStack, ?> arg = argument(field.getName(), type);
        if (field.getType().isEnum()) {
            arg.suggests(enumSuggestions(field.getType()));
        }
        return arg;
    }

    private static SuggestionProvider<CommandSourceStack> enumSuggestions(Class<?> enumType) {
        Object[] constants = enumType.getEnumConstants();
        return (ctx, builder) -> {
            String remaining = builder.getRemaining().toLowerCase();
            for (Object constant : constants) {
                String constantName = ((Enum<?>) constant).name().toLowerCase();
                if (constantName.startsWith(remaining)) {
                    builder.suggest(constantName);
                }
            }
            return builder.buildFuture();
        };
    }

    private static ArgumentType<?> resolveArgumentType(Class<?> type) {
        if (type.isEnum())
            return StringArgumentType.word();
        return switch (type.getSimpleName()) {
            case "String" -> StringArgumentType.string();
            case "List" -> StringArgumentType.greedyString();
            case "byte", "Byte" -> IntegerArgumentType.integer(Byte.MIN_VALUE, Byte.MAX_VALUE);
            case "int", "Integer" -> IntegerArgumentType.integer();
            case "float", "Float" -> FloatArgumentType.floatArg();
            case "double", "Double" -> DoubleArgumentType.doubleArg();
            case "long", "Long" -> LongArgumentType.longArg();
            case "boolean", "Boolean" -> BoolArgumentType.bool();
            case "short", "Short" -> IntegerArgumentType.integer(Short.MIN_VALUE, Short.MAX_VALUE);
            case "CompoundPacketPayload", "CompoundPayload" -> null;
            default -> throw new IllegalStateException("Unexpected value: " + type.getSimpleName());
        };
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Object extractValue(CommandContext<CommandSourceStack> ctx, Field field) throws CommandSyntaxException {
        String argName = field.getName();
        Class<?> t = field.getType();

        if (t.isEnum()) {
            String raw = StringArgumentType.getString(ctx, argName);
            try {
                return Enum.valueOf((Class<? extends Enum>) t.asSubclass(Enum.class), raw.toUpperCase());
            } catch (IllegalArgumentException e) {
                String valid = String.join(", ", Arrays.stream(t.getEnumConstants())
                        .map(c -> ((Enum<?>) c).name().toLowerCase())
                        .toList());
                throw new SimpleCommandExceptionType(
                        Component.literal("Unknown value '" + raw + "' for " + argName + ". Valid values: " + valid)
                ).create();
            }
        }

        return switch (t.getSimpleName()) {
            case "String" -> StringArgumentType.getString(ctx, argName);
            case "List" -> Arrays.asList(StringArgumentType.getString(ctx, argName).split(","));
            case "byte", "Byte" -> (byte) IntegerArgumentType.getInteger(ctx, argName);
            case "int", "Integer" -> IntegerArgumentType.getInteger(ctx, argName);
            case "float", "Float" -> FloatArgumentType.getFloat(ctx, argName);
            case "double", "Double" -> DoubleArgumentType.getDouble(ctx, argName);
            case "long", "Long" -> LongArgumentType.getLong(ctx, argName);
            case "boolean", "Boolean" -> BoolArgumentType.getBool(ctx, argName);
            case "short", "Short" -> (short) IntegerArgumentType.getInteger(ctx, argName);
            default -> throw new IllegalStateException("Unexpected value: " + t.getSimpleName());
        };
    }

    private static OBUSettingsPacket buildPacket(Class<?> subclass, CommandContext<CommandSourceStack> ctx, List<Field> fields)
            throws CommandSyntaxException {
        try {
            Object instance = subclass.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                Object value = extractValue(ctx, field);
                field.set(instance, value);
            }
            return (OBUSettingsPacket) instance;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to build packet for " + subclass.getSimpleName(), e);
        }
    }
}