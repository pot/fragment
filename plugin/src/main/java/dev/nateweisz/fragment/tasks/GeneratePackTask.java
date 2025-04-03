package dev.nateweisz.fragment.tasks;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dev.nateweisz.fragment.config.FragmentConfiguration;
import dev.nateweisz.fragment.glyphs.Glyph;
import dev.nateweisz.fragment.glyphs.GlyphParser;
import dev.nateweisz.fragment.glyphs.GlyphWriter;
import net.kyori.adventure.key.Key;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Task;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;
import org.jetbrains.annotations.NotNull;
import team.unnamed.creative.ResourcePack;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.metadata.pack.PackFormat;
import team.unnamed.creative.serialize.minecraft.MinecraftResourcePackWriter;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

public abstract class GeneratePackTask extends DefaultTask {
    @Internal
    abstract public RegularFileProperty getPackFile();

    @Internal
    abstract public RegularFileProperty getOutputFile();

    @Internal
    abstract public RegularFileProperty getGeneratedDirectory();

    @TaskAction
    public void execute() {
        /*
        if (!config.isPresent() || !config.get().getPackFile().isPresent()) {

            throw new IllegalStateException("Pack file is required to be set.");
        }

         */

        Config configFile = ConfigFactory.parseFile(getPackFile().get().getAsFile());
        ResourcePack pack = ResourcePack.resourcePack();

        // required configuration
        pack.packMeta(
                configFile.getInt("version"),
                configFile.getString("description")
        );

        // Types of pack config

        // Glyphs
        GlyphParser glyphParser = new GlyphParser(configFile, getPackFile().get().getAsFile());
        List<Glyph> glyphs = glyphParser.parseGlyphs();

        // write them to pack
        glyphs.forEach(glyph -> {
                    pack.texture(glyph.internalTexture());
                    pack.font(Key.key("default"), glyph.internalFontProvider());
                });

        // write the static code for accessing them
        new GlyphWriter(getGeneratedDirectory()).writeGlyphs(glyphs);

        // we can try and squash thing's that have the same height later on for some optimization

        // make sure the dir exists if not already
        File outputFile = getOutputFile().get().getAsFile();
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
        }

        MinecraftResourcePackWriter.minecraft()
                .writeToZipFile(
                        outputFile,
                        pack
                );

    }
}
