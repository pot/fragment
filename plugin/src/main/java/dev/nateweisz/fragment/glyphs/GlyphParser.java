package dev.nateweisz.fragment.glyphs;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import dev.nateweisz.fragment.utils.CharacterUtils;
import dev.nateweisz.fragment.utils.FileUtils;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import team.unnamed.creative.base.Writable;
import team.unnamed.creative.font.Font;
import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.metadata.Metadata;
import team.unnamed.creative.metadata.MetadataPart;
import team.unnamed.creative.texture.Texture;

import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GlyphParser {
    private final Config config;
    private final File baseFile;

    public GlyphParser(Config config, File baseFile) {
        this.config = config;
        this.baseFile = baseFile;
    }

    public List<Glyph> parseGlyphs() {
        return config.getObject("glyphs")
                .keySet()
                .stream()
                .map(this::parseGlyph)
                .collect(Collectors.toList());
    }

    private Glyph parseGlyph(String glyphSectionKey) {
        Config glyph = config.getConfig("glyphs").getConfig(glyphSectionKey);
        String namespace = config.hasPathOrNull("namespace")
                ? glyph.getString("namespace")
                : config.getString("default_namespace");

        String path = glyph.getString("path");
        Key key = Key.key(namespace, FileUtils.fileNameWithExtension(path, "png"));

        Path texturePath = baseFile.getParentFile().toPath().resolve(FileUtils.fileNameWithExtension(path, "png"));
        Texture texture = Texture.texture(key, Writable.path(
                texturePath
        ));

        String character = String.valueOf(CharacterUtils.generateNextCharacter());

        FontProvider fontProvider = FontProvider.bitMap()
                .file(texture.key())
                .ascent(glyph.getInt("ascent"))
                .height(glyph.getInt("height"))
                .characters(character)
                .build();

        return new Glyph(glyphSectionKey, character, texture, fontProvider);
    }
}
