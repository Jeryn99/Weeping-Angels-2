package mc.jeryn.dev.angels.config;

import blue.endless.jankson.Comment;
import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;
import io.wispforest.owo.config.annotation.SectionHeader;
import mc.jeryn.dev.angels.WAConstants;
import mc.jeryn.dev.angels.util.HurtUtil;
import org.jetbrains.annotations.Range;

import java.util.List;

@Config(name = "weeping_angels", wrapperName = "WAConfig")
@Modmenu(modId = WAConstants.MOD_ID)
public class WAConfigModel {

    @SectionHeader("behaviour")
    @Range(from = 1, to = 100)
    @Comment("Range for player visibility check.")
    public Integer stalkRange = 65;

    @Comment("Control what can damage a Weeping Angel")
    public HurtUtil.HurtType hurtType = HurtUtil.HurtType.PICKAXE_AND_GENERATOR;

    @Comment("Allow angels to break light-emitting blocks?")
    public Boolean blockBreaking = true;

    @Comment("Allow teleporting across dimensions?")
    public Boolean interdimensionalTeleporting = true;

    @Comment("Allow Angel theft?")
    public Boolean angelTheft = true;

    @SectionHeader("teleporting")
    @Range(from = 1, to = 1L)
    @Comment("Teleportation Range")
    public Integer teleportRange = 400;

    @Range(from = 1, to = 100)
    @Comment("Chance of teleportation occurring.")
    public Integer teleportChance = 50;

    @Comment("Dimensions where teleportation is banned.")
    public List<String> bannedDimensions = List.of("minecraft:the_end");

    @SectionHeader("seasonal")
    @Comment("Show Santa hats on angels at Xmas?")
    public Boolean santaHats = true;
}
