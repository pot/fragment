package dev.nateweisz.fragment.glyphs;

import team.unnamed.creative.font.FontProvider;
import team.unnamed.creative.texture.Texture;

public record Glyph(
        String name,
        String character,
        Texture internalTexture,
        FontProvider internalFontProvider
) { }
