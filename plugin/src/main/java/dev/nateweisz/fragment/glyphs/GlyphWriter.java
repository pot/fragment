package dev.nateweisz.fragment.glyphs;

import com.palantir.javapoet.FieldSpec;
import com.palantir.javapoet.JavaFile;
import com.palantir.javapoet.TypeSpec;
import org.gradle.api.file.RegularFileProperty;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GlyphWriter {
    private final File generatedDir;

    public GlyphWriter(RegularFileProperty generatedDirectory) {
        this.generatedDir = generatedDirectory.get().getAsFile();
    }

    public void writeGlyphs(List<Glyph> glyphs) {
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder("Glyphs");

        for (Glyph glyph : glyphs) {
            typeSpec.addField(
                    FieldSpec.builder(String.class, convertToVariableName(glyph.name()))
                            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
                            .initializer("$S", glyph.character())
                            .build()
            );
        }

        JavaFile glyphsFile = JavaFile.builder("dev.nateweisz.fragment.glyphs", typeSpec.build())
                .addFileComment("""
                        This code is auto generated, DO NOT EDIT! Any chances you
                        make to this file will be overwritten.""")
                .build();

        try {
            glyphsFile.writeTo(generatedDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write glyphs to " + generatedDir.getAbsolutePath(), e);
        }
    }

    private String convertToVariableName(String input) {
        return input.toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }
}
