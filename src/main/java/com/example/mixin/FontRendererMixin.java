package com.example.mixin;

import net.minecraft.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Keeps FontRenderer from reading past the end of its charWidth array.
 *
 * <p>Vanilla looks a character up in ChatAllowedCharacters.allowedCharacters and, unless
 * unicodeFlag is set, uses that index against charWidth and the 256-glyph default font texture
 * without a bounds check. That holds with the vanilla font.txt, but FishModLoader swaps in one
 * listing 28249 characters while charWidth stays 256 entries, so any character past index 224
 * throws. The language list triggers it on sight: every language is drawn under its native name
 * while the current language is still Latin, so unicodeFlag is false.
 *
 * <p>Forcing the index out of range makes vanilla fall through to its unicode font, which covers
 * the full BMP and is the path these characters were always meant to take. Widening charWidth
 * instead would stop the crash but sample past the edge of the default font texture.
 *
 * <p>One upstream overflow is left alone: renderStringAtPos reads charWidth[index + 32] when
 * picking a same-width replacement glyph for obfuscated (&sect;k) text.
 */
@Mixin(FontRenderer.class)
public class FontRendererMixin {
	@Shadow
	private int[] charWidth;

	/** Guards the width lookup in getCharWidth, which reads charWidth[index + 32]. */
	@ModifyVariable(method = "getCharWidth", at = @At("STORE"), ordinal = 0)
	private int guardCharWidthIndex(int index) {
		return isDefaultFontIndex(index) ? index : -1;
	}

	/**
	 * Guards the render path in renderCharAtPos, which passes index + 32 to renderDefaultChar.
	 * Vanilla treats any index of 0 or less as "not in the default font", so zero routes the
	 * character to renderUnicodeChar.
	 */
	@ModifyVariable(method = "renderCharAtPos", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private int guardRenderIndex(int index) {
		return isDefaultFontIndex(index) ? index : 0;
	}

	private boolean isDefaultFontIndex(int index) {
		return index + 32 < charWidth.length;
	}
}
